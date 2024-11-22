package com.unseen.db.entity;

import com.unseen.db.config.ModConfig;
import com.unseen.db.util.IPitch;
import com.unseen.db.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityAbstractClaw extends EntityModBase implements IEntityMultiPart, IPitch {
    private final MultiPartEntityPart[] hitboxParts;
    private final MultiPartEntityPart model = new MultiPartEntityPart(this, "model", 0f, 0f);
    private final MultiPartEntityPart bottom = new MultiPartEntityPart(this, "base", 0.6f, 0.6f);

    private static final DataParameter<Boolean> ATTACK_MODE = EntityDataManager.createKey(EntityAbstractClaw.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> EAT_MODE = EntityDataManager.createKey(EntityAbstractClaw.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Float> LOOK = EntityDataManager.createKey(EntityAbstractClaw.class, DataSerializers.FLOAT);

    public void setAttackMode(boolean value) {this.dataManager.set(ATTACK_MODE, Boolean.valueOf(value));}
    public boolean isAttackMode() {return this.dataManager.get(ATTACK_MODE);}
    public void setEatMode(boolean value) {this.dataManager.set(EAT_MODE, Boolean.valueOf(value));}
    public boolean isEatMode() {return this.dataManager.get(EAT_MODE);}

    public EntityAbstractClaw(World worldIn) {

        super(worldIn);
        this.hitboxParts = new MultiPartEntityPart[]{model, bottom};
    }

    @Override
    public void entityInit() {
        this.dataManager.register(LOOK, 0f);
        this.dataManager.register(ATTACK_MODE, Boolean.valueOf(false));
        this.dataManager.register(EAT_MODE, Boolean.valueOf(false));
        super.entityInit();

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Attack", this.isAttackMode());
        nbt.setBoolean("Eat_Mode", this.isEatMode());
        nbt.setFloat("Look", this.getPitch());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.setAttackMode(nbt.getBoolean("Attack"));
        this.setEatMode(nbt.getBoolean("Eat_Mode"));
        this.dataManager.set(LOOK, nbt.getFloat("Look"));
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(ModConfig.claw_health);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(8D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(ModConfig.claw_attack_damage);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        Vec3d[] avec3d = new Vec3d[this.hitboxParts.length];
        for (int j = 0; j < this.hitboxParts.length; ++j) {
            avec3d[j] = new Vec3d(this.hitboxParts[j].posX, this.hitboxParts[j].posY, this.hitboxParts[j].posZ);
        }

        //ModelLocations for each hitboxes
        this.setHitBoxPos(bottom, new Vec3d(0, -0.2, 0));


        Vec3d knightPos = this.getPositionVector();
        ModUtils.setEntityPosition(model, knightPos);

        for (int l = 0; l < this.hitboxParts.length; ++l) {
            this.hitboxParts[l].prevPosX = avec3d[l].x;
            this.hitboxParts[l].prevPosY = avec3d[l].y;
            this.hitboxParts[l].prevPosZ = avec3d[l].z;
        }
    }


    private void setHitBoxPos(Entity entity, Vec3d offset) {
        Vec3d lookVel = ModUtils.getLookVec(this.getPitch(), this.renderYawOffset);
        Vec3d center = this.getPositionVector().add(ModUtils.yVec(1.2));

        Vec3d position = center.subtract(ModUtils.Y_AXIS.add(ModUtils.getAxisOffset(lookVel, offset)));
        ModUtils.setEntityPosition(entity, position);

    }

    @Override
    public Entity[] getParts() {
        return this.hitboxParts;
    }


    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void setPitch(Vec3d look) {
        float prevLook = this.getPitch();
        float newLook = (float) ModUtils.toPitch(look);
        float deltaLook = 5;
        float clampedLook = MathHelper.clamp(newLook, prevLook - deltaLook, prevLook + deltaLook);
        this.dataManager.set(LOOK, clampedLook);
    }

    @Override
    public boolean isPushedByWater()
    {
        return false;
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public float getPitch() {
        return this.dataManager == null ? 0 : this.dataManager.get(LOOK);
    }


    @Override
    public World getWorld() {
        return world;
    }


    @Override
    public boolean attackEntityFromPart(MultiPartEntityPart multiPartEntityPart, @Nonnull DamageSource damageSource, float damage) {
        return this.attackEntityFrom(damageSource, damage);
    }
}
