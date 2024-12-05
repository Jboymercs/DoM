package com.unseen.db.entity;

import com.unseen.db.config.MobConfig;
import com.unseen.db.config.ModConfig;
import com.unseen.db.entity.ai.EntityAiTimedAttack;
import com.unseen.db.init.ModSoundHandler;
import com.unseen.db.util.IAttack;
import com.unseen.db.util.ModDamageSource;
import com.unseen.db.util.ModRandom;
import com.unseen.db.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityThrall extends EntityModBase implements IAnimatable, IAttack {
    private final String ANIM_IDLE_LEGS = "idle_legs";
    private final String ANIM_IDLE_ARMS = "idle_arms";
    private final String ANIM_WALKING_ARMS = "walk_arms";
    private final String ANIM_WALKING_LEGS = "walk_legs";
    private final String ANIM_SWIMMING_ARMS_IDLE = "swim_idle_arms";
    private final String ANIM_SWIMMING_LEGS_IDLE = "swim_idle_legs";
    private final String ANIM_SWIMMING_LEGS_MOVE = "swim_walk_legs";
    private final String ANIM_SWIMMING_ARMS_MOVE = "swim_walk_arms";
    private final String ANIM_SWING_ATTACK = "attack";
    private final String ANIM_SUMMON = "summon";
    private final String ANIM_PRAYING = "pray";
    private static final DataParameter<Boolean> FIGHT_MODE = EntityDataManager.createKey(EntityThrall.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAS_GROUND = EntityDataManager.createKey(EntityThrall.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON = EntityDataManager.createKey(EntityThrall.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PRAYING = EntityDataManager.createKey(EntityThrall.class, DataSerializers.BOOLEAN);
    public void setFightMode(boolean value) {this.dataManager.set(FIGHT_MODE, Boolean.valueOf(value));}
    public boolean isFightMode() {return this.dataManager.get(FIGHT_MODE);}
    public void setHasGround(boolean value) {this.dataManager.set(HAS_GROUND, Boolean.valueOf(value));}
    public boolean isOnGround() {return this.dataManager.get(HAS_GROUND);}
    public void setSummon(boolean value) {this.dataManager.set(SUMMON, Boolean.valueOf(value));}
    public boolean isSummon() {return this.dataManager.get(SUMMON);}
    public boolean isPraying() {return this.dataManager.get(PRAYING);}
    public void setPraying(boolean value) {this.dataManager.set(PRAYING, Boolean.valueOf(value));}
    private AnimationFactory factory = new AnimationFactory(this);
    public EntityThrall(World worldIn) {
        super(worldIn);
        this.moveHelper = new EntityThrall.ThrallMoveHelper(this);
        this.setSize(0.7f, 1.9f);
        this.setSummon(true);
        if(this.isSummon()) {
            addEvent(()-> this.setSummon(false), 40);
        }

    }

    protected PathNavigate createNavigator(World worldIn) {
        return new PathNavigateSwimmer(this, worldIn);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        if(!this.isPraying()) {
            this.tasks.addTask(4, new EntityAiTimedAttack<>(this, 1.0, 10, 2F, 0.2f));
            this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
            this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        }
        this.tasks.addTask(4, new EntityAiTimedAttack<>(this, 1.0, 40, 3F, 0.2f));
        this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));


    }

    @Override
    public void onUpdate() {
        EntityLivingBase target = this.getAttackTarget();
        //Hover Function
        if(!this.isFightMode() && target == null) {
            Vec3d toFor = this.getPositionVector();
            BlockPos staticPos = new BlockPos(toFor.x, toFor.y - 2, toFor.z);
            if(!world.getBlockState(staticPos).isFullBlock()) {
                this.motionY = -0.07;
            }

        }
        if(target != null) {
            Vec3d targetLocationFeet = target.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, -1,0)));
            Vec3d currentLoc = this.getPositionVector();
            if(currentLoc.y > targetLocationFeet.y) {
                this.motionY = -0.07;
            }
            if(currentLoc.y < targetLocationFeet.y) {
                this.motionY = 0.07;
            }
        }

        if(this.isInWater()) {
            this.setAIMoveSpeed(1.8f);
        }

        if(!world.isAirBlock(getPosition().down()) && this.isInWater()) {
            this.setHasGround(false);
        } else {
            this.setHasGround(true);
        }

        if(this.isSummon()) {
            motionX = 0;
            motionY = 0;
            motionZ = 0;
        }
        if(this.isPraying()) {
            motionX = 0;
            motionZ = 0;
        }

        List<EntityHeirophant> prayedFor = this.world.getEntitiesWithinAABB(EntityHeirophant.class, this.getEntityBoundingBox().grow(8D), e -> !e.getIsInvulnerable());
        List<EntityThrall> nearbyThralls = this.world.getEntitiesWithinAABB(EntityThrall.class, this.getEntityBoundingBox().grow(16D), e -> !e.getIsInvulnerable());

        for(EntityThrall thrall : nearbyThralls) {

        }


        super.onUpdate();

    }

    @Override
    public void fall(float p_180430_1_, float p_180430_2_) {
    }

    @Override
    public void entityInit() {
        this.dataManager.register(FIGHT_MODE, Boolean.valueOf(false));
        this.dataManager.register(HAS_GROUND, Boolean.valueOf(false));
        this.dataManager.register(SUMMON, Boolean.valueOf(false));
        this.dataManager.register(PRAYING, Boolean.valueOf(false));
        super.entityInit();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Fight_Mode", this.isFightMode());
        nbt.setBoolean("Has_Ground", this.isOnGround());
        nbt.setBoolean("Summon", this.isSummon());
        nbt.setBoolean("Praying", this.isPraying());
    }


    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setFightMode(nbt.getBoolean("Fight_Mode"));
        this.setHasGround(nbt.getBoolean("Has_Ground"));
        this.setSummon(nbt.getBoolean("Summon"));
        this.setPraying(nbt.getBoolean("Praying"));
        super.readEntityFromNBT(nbt);

    }

    @Override
    public boolean isPushedByWater()
    {
        return false;
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    private Consumer<EntityLivingBase> prevAttack;
    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isSummon() && !this.isFightMode() && !this.isPraying()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(meleeAttack));
            double[] weights = {
                    (distance < 3) ? 1 / distance : 0 // Basic Melee Strike
            };

            prevAttack = ModRandom.choice(attacks, rand, weights).next();

            prevAttack.accept(target);
        }
        return 10;
    }

    private final Consumer<EntityLivingBase> meleeAttack = (target) -> {
      this.setFightMode(true);

      addEvent(()-> {
          this.playSound(ModSoundHandler.THRALL_ATTACK, 1.4f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.0, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.4f, 0, false);
      }, 15);
      addEvent(()-> {
          this.setFightMode(false);
      }, 30);
    };

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller_t", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "arms_controller_t", 0, this::predicateArms));
        animationData.addAnimationController(new AnimationController(this, "attack_handler_t", 0, this::predicateAttack));
    }
    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isSummon()) {
            if (!this.isOnGround()) {
                if (event.isMoving()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWIMMING_LEGS_MOVE, true));
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWIMMING_LEGS_IDLE, true));
                }
            }
            if (this.isOnGround()) {
                if (event.isMoving()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALKING_LEGS, true));
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_LEGS, true));
                }
            }

            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if(this.isPraying()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PRAYING, true));
            return PlayState.CONTINUE;
        }
        if(!this.isFightMode() && !this.isSummon() && !this.isPraying()) {
            if(!this.isOnGround()) {
                if(event.isMoving()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWIMMING_ARMS_MOVE, true));
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWIMMING_ARMS_IDLE, true));
                }
            }
            if(this.isOnGround() && !this.isPraying()) {
                if (event.isMoving()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALKING_ARMS, true));
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_ARMS, true));
                }
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    private <E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_ATTACK, false));
            return PlayState.CONTINUE;
        }
        if(this.isSummon()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.thrall_health);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.43590D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.thrall_attack_damage);
    }

    public void travel(float strafe, float vertical, float forward) {
        if (this.isServerWorld() && this.isInWater()) {
            this.moveRelative(strafe, vertical, forward, 0.01F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double)0.9F;
            this.motionY *= (double)0.5F;
            this.motionZ *= (double)0.9F;
            if (this.getAttackTarget() == null) {
                this.motionY -= 0.005D;
            }
        } else {
            super.travel(strafe, vertical, forward);
        }

    }
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundHandler.THRALL_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundHandler.THRALL_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundHandler.THRALL_DEATH;
    }

    static class ThrallMoveHelper extends EntityMoveHelper {
        private final EntityThrall fish;

        ThrallMoveHelper(EntityThrall p_i48857_1_) {
            super(p_i48857_1_);
            this.fish = p_i48857_1_;
        }

        public void onUpdateMoveHelper() {

            if (this.action == EntityMoveHelper.Action.MOVE_TO && !this.fish.getNavigator().noPath()) {
                double d0 = this.posX - this.fish.posX;
                double d1 = this.posY - this.fish.posY;
                double d2 = this.posZ - this.fish.posZ;
                double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d1 = d1 / d3;
                float f = (float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.fish.rotationYaw = this.limitAngle(this.fish.rotationYaw, f, 90.0F);
                this.fish.renderYawOffset = this.fish.rotationYaw;
                float f1 = (float)(this.speed * this.fish.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue());
                this.fish.setAIMoveSpeed(this.fish.getAIMoveSpeed() + (f1 - this.fish.getAIMoveSpeed()) * 0.125F);
                this.fish.motionY += (double)this.fish.getAIMoveSpeed() * d1 * 0.1D;
            } else {
                this.fish.setAIMoveSpeed(0.0F);
            }
        }
    }


}
