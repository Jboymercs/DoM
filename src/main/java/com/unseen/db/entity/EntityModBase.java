package com.unseen.db.entity;

import com.unseen.db.entity.ai.MobGroundNavigate;
import com.unseen.db.util.ModUtils;
import com.unseen.db.util.ServerScaleUtil;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.PriorityQueue;

public abstract class EntityModBase extends EntityCreature {
    private float regenTimer;
    public boolean iAmBossMob = false;
    protected static final DataParameter<Boolean> IMMOVABLE = EntityDataManager.<Boolean>createKey(EntityModBase.class, DataSerializers.BOOLEAN);

    public EntityModBase(World worldIn) {
        super(worldIn);
        this.experienceValue = 5;
    }

    public EntityModBase (World worldIn, float x, float y, float z) {
        super(worldIn);
        this.setPosition(x, y, z);

    }
    protected double healthScaledAttackFactor = 0.0; // Factor that determines how much attack is affected by health
    public float getAttack() {
        return ModUtils.getMobDamage(this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue(), healthScaledAttackFactor, this.getMaxHealth(),
                this.getHealth());
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return super.attackEntityFrom(source, amount);
    }
    private PriorityQueue<TimedEvent> events = new PriorityQueue<TimedEvent>();

    private static float regenStartTimer = 20;

    private Vec3d initialPosition = null;

    protected boolean isImmovable() {
        return this.dataManager == null ? false : this.dataManager.get(IMMOVABLE);
    }

    protected void setImmovable(boolean immovable) {
        this.dataManager.set(IMMOVABLE, immovable);
    }

    public void setImmovablePosition(Vec3d pos) {
        this.initialPosition = pos;
        this.setPosition(0, 0, 0);
    }

    public static class TimedEvent implements Comparable<TimedEvent> {
        Runnable callback;
        int ticks;

        public TimedEvent(Runnable callback, int ticks) {
            this.callback = callback;
            this.ticks = ticks;
        }

        @Override
        public int compareTo(TimedEvent event) {
            return event.ticks < ticks ? 1 : -1;
        }
    }



    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(0);

    }
    @Override
    protected PathNavigate createNavigator(World worldIn) {
        return new MobGroundNavigate(this, worldIn);
    }

    @SideOnly(Side.CLIENT)
    protected void initAnimation() {
    }

    @Override
    public void move(MoverType type, double x, double y, double z) {
        if(!this.isImmovable()) {
            super.move(type, x, y, z);
        }
    }

    protected boolean hasStartedScaling = false;
    protected int checkNearbyPlayers = 250;

    @Override
    public void onLivingUpdate() {

        EntityLivingBase target = this.getAttackTarget();

        if(this.iAmBossMob && target != null) {
            if(!this.hasStartedScaling && target instanceof EntityPlayer && !this.world.isRemote) {
                double changeAttackDamage = ServerScaleUtil.scaleAttackDamageInAccordanceWithPlayers(this, world);
                float healthCurrently = ServerScaleUtil.changeHealthAccordingToPlayers(this, world);
                double maxHealthCurrently = ServerScaleUtil.setMaxHealthWithPlayers(this, world);
                //This is change the Health in accordance with how many players are currently nearby // TEST
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(maxHealthCurrently);
                this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(changeAttackDamage);
                this.setHealth(this.getMaxHealth());
                hasStartedScaling = true;
            }
        }

        if(this.iAmBossMob) {
            List<EntityEnderCrystal> nearbyEyes = this.world.getEntitiesWithinAABB(EntityEnderCrystal.class, this.getEntityBoundingBox().grow(30D), e -> !e.getIsInvulnerable());
            if(!nearbyEyes.isEmpty()) {
                for(EntityEnderCrystal eye: nearbyEyes) {
                    eye.setDead();
                }
            }
        }

        //This is where target Switching occurs for bosses
        if(this.iAmBossMob && checkNearbyPlayers <= 0 && target != null) {
            if(target instanceof EntityPlayer) {
                //Makes sure it's a player for the second time in here, just as a double check.
                this.setAttackTarget(ServerScaleUtil.targetSwitcher(this, world));
                this.checkNearbyPlayers = 250;
            }
        } else {
            checkNearbyPlayers--;
        }

        if (!isDead && this.getHealth() > 0) {
            boolean foundEvent = true;
            while (foundEvent) {
                TimedEvent event = events.peek();
                if (event != null && event.ticks <= this.ticksExisted) {
                    events.remove();
                    event.callback.run();
                } else {
                    foundEvent = false;
                }
            }
        }


        if (!world.isRemote) {
            if (this.getAttackTarget() == null) {
                if (this.regenTimer > this.regenStartTimer) {
                    if (this.ticksExisted % 20 == 0) {
                        this.heal(this.getMaxHealth() * 0.015f);
                    }
                } else {
                    this.regenTimer++;
                }
            } else {
                this.regenTimer = 0;
            }
        }
        super.onLivingUpdate();
    }

    public void doRender(RenderManager renderManager, double x, double y, double z, float entityYaw, float partialTicks) {
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
    }

    /**
     * Adds an event to be executed at a later time. Negative ticks are executed immediately.
     *
     * @param runnable
     * @param ticksFromNow
     */
    public void addEvent(Runnable runnable, int ticksFromNow) {
        events.add(new TimedEvent(runnable, this.ticksExisted + ticksFromNow));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(IMMOVABLE, Boolean.valueOf(false));
    }

}
