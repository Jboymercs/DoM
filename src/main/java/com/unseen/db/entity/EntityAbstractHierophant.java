package com.unseen.db.entity;

import com.unseen.db.config.MobConfig;
import com.unseen.db.config.ModConfig;
import com.unseen.db.util.IPitch;
import com.unseen.db.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
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

public abstract class EntityAbstractHierophant extends EntityModBase implements IEntityMultiPart, IPitch {
    private final MultiPartEntityPart[] hitboxParts;
    private final MultiPartEntityPart model = new MultiPartEntityPart(this, "model", 0f, 0f);

    private final MultiPartEntityPart mainBody = new MultiPartEntityPart(this, "body", 1.2f, 2.4f);

    private final MultiPartEntityPart headFront = new MultiPartEntityPart(this, "head", 1f, 1f);
    private final MultiPartEntityPart tailOne = new MultiPartEntityPart(this, "tail_one", 1f, 0.8f);
    private final MultiPartEntityPart tailTwo = new MultiPartEntityPart(this, "tail_two", 1f, 0.8f);
    private static final DataParameter<Boolean> FIGHT_MODE = EntityDataManager.createKey(EntityAbstractHierophant.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHADOW_CAST = EntityDataManager.createKey(EntityAbstractHierophant.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MELEE_STRIKE = EntityDataManager.createKey(EntityAbstractHierophant.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FULL_BODY_USAGE = EntityDataManager.createKey(EntityAbstractHierophant.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> BELL_RING = EntityDataManager.createKey(EntityAbstractHierophant.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> SUMMON_ORB = EntityDataManager.createKey(EntityAbstractHierophant.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> SUMMON_THRALL = EntityDataManager.createKey(EntityAbstractHierophant.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> SUMMON_JAIL_CELL = EntityDataManager.createKey(EntityAbstractHierophant.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPIKES_AOE = EntityDataManager.createKey(EntityAbstractHierophant.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPIKE_LINE = EntityDataManager.createKey(EntityAbstractHierophant.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DEATH = EntityDataManager.createKey(EntityAbstractHierophant.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Float> LOOK = EntityDataManager.createKey(EntityAbstractHierophant.class, DataSerializers.FLOAT);

    public void setFightMode(boolean value) {this.dataManager.set(FIGHT_MODE, Boolean.valueOf(value));}
    public boolean isFightMode() {return this.dataManager.get(FIGHT_MODE);}
    public void setShadowCast(boolean value) {this.dataManager.set(SHADOW_CAST, Boolean.valueOf(value));}
    public boolean isShadowCasting() {return this.dataManager.get(SHADOW_CAST);}
    public void setMeleeStrike(boolean value) {this.dataManager.set(MELEE_STRIKE, Boolean.valueOf(value));}
    public boolean isMeleeStrike() {return this.dataManager.get(MELEE_STRIKE);}
    public void setFullBodyUsage(boolean value) {this.dataManager.set(FULL_BODY_USAGE, Boolean.valueOf(value));}
    public boolean isFullBodyUsage() {return this.dataManager.get(FULL_BODY_USAGE);}
    public void setBellRing(boolean value0) {this.dataManager.set(BELL_RING, Boolean.valueOf(value0));}
    public void setSummonOrb(boolean value) {this.dataManager.set(SUMMON_ORB, Boolean.valueOf(value));}
    public void setSummonThrall(boolean value) {this.dataManager.set(SUMMON_THRALL, Boolean.valueOf(value));}
    public boolean isBellRinge() {return this.dataManager.get(BELL_RING);}
    public boolean isSummonOrd() {return this.dataManager.get(SUMMON_ORB);}
    public boolean isSummonThrall() {return this.dataManager.get(SUMMON_THRALL);}
    public boolean isSummonJailCell() {return this.dataManager.get(SUMMON_JAIL_CELL);}
    public void setSummonJailCell(boolean value) {this.dataManager.set(SUMMON_JAIL_CELL, Boolean.valueOf(value));}
    public boolean isSpikesAOE() {return this.dataManager.get(SPIKES_AOE);}
    public void setSpikesAoe(boolean value) {this.dataManager.set(SPIKES_AOE, Boolean.valueOf(value));}
    public boolean isSpikeLIne() {return this.dataManager.get(SPIKE_LINE);}
    public void setSpikeLine(boolean value) {this.dataManager.set(SPIKE_LINE, Boolean.valueOf(value));}
    public boolean isDeath() {return this.dataManager.get(DEATH);}
    public void setDeath(boolean value) {this.dataManager.set(DEATH, Boolean.valueOf(value));}

    public EntityAbstractHierophant(World worldIn) {
        super(worldIn);
        this.hitboxParts = new MultiPartEntityPart[]{model, mainBody, headFront, tailOne, tailTwo};
        this.setSize(2f, 3.5f);
        this.isImmuneToExplosions();
        this.isImmuneToFire = true;
        if(MobConfig.scaled_attack_factor_enable_disable) {
            this.healthScaledAttackFactor = MobConfig.hierophant_scaled_factor;
        }
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
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
    public void entityInit() {
        this.dataManager.register(LOOK, 0f);
        this.dataManager.register(FIGHT_MODE, Boolean.valueOf(false));
        this.dataManager.register(SHADOW_CAST, Boolean.valueOf(false));
        this.dataManager.register(MELEE_STRIKE, Boolean.valueOf(false));
        this.dataManager.register(FULL_BODY_USAGE, Boolean.valueOf(false));
        this.dataManager.register(BELL_RING, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_ORB, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_THRALL, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_JAIL_CELL, Boolean.valueOf(false));
        this.dataManager.register(SPIKES_AOE, Boolean.valueOf(false));
        this.dataManager.register(SPIKE_LINE, Boolean.valueOf(false));
        this.dataManager.register(DEATH, Boolean.valueOf(false));
        super.entityInit();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.hierophant_health);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.43590D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(2.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(18.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.hierophant_attack_damage);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        Vec3d[] avec3d = new Vec3d[this.hitboxParts.length];
        for (int j = 0; j < this.hitboxParts.length; ++j) {
            avec3d[j] = new Vec3d(this.hitboxParts[j].posX, this.hitboxParts[j].posY, this.hitboxParts[j].posZ);
        }

        //ModelLocations for each hitboxes
        this.setHitBoxPos(mainBody, new Vec3d(-1, 0.5, 0));
        this.setHitBoxPos(headFront, new Vec3d(-1, 2.9, 0));
        this.setHitBoxPos(tailOne, new Vec3d(0.1, 0.5, 0));
        this.setHitBoxPos(tailTwo, new Vec3d(1.1, 0.5, 0));

        Vec3d knightPos = this.getPositionVector();
        ModUtils.setEntityPosition(model, knightPos);

        for (int l = 0; l < this.hitboxParts.length; ++l) {
            this.hitboxParts[l].prevPosX = avec3d[l].x;
            this.hitboxParts[l].prevPosY = avec3d[l].y;
            this.hitboxParts[l].prevPosZ = avec3d[l].z;
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Fight_Mode", this.isFightMode());
        nbt.setBoolean("Shadow", this.isShadowCasting());
        nbt.setBoolean("Melee", this.isMeleeStrike());
        nbt.setBoolean("Full_Body_Usage", this.isFullBodyUsage());
        nbt.setBoolean("Bell_Ring", this.isBellRinge());
        nbt.setBoolean("Summon_Thrall", this.isSummonThrall());
        nbt.setBoolean("Summon_Orb", this.isSummonOrd());
        nbt.setBoolean("Summon_Jail", this.isSummonJailCell());
        nbt.setBoolean("Spikes_Aoe", this.isSpikesAOE());
        nbt.setBoolean("Spike_Line", this.isSpikeLIne());
        nbt.setBoolean("Death", this.isDeath());
        nbt.setFloat("Look", this.getPitch());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.setFightMode(nbt.getBoolean("Fight_Mode"));
        this.setShadowCast(nbt.getBoolean("Shadow"));
        this.setMeleeStrike(nbt.getBoolean("Melee"));
        this.setFullBodyUsage(nbt.getBoolean("Full_Body_Usage"));
        this.setBellRing(nbt.getBoolean("Bell_Ring"));
        this.setSummonThrall(nbt.getBoolean("Summon_Thrall"));
        this.setSummonOrb(nbt.getBoolean("Summon_Orb"));
        this.setSummonJailCell(nbt.getBoolean("Summon_Jail"));
        this.setSpikesAoe(nbt.getBoolean("Spikes_Aoe"));
        this.setSpikeLine(nbt.getBoolean("Spike_Line"));
        this.setDeath(nbt.getBoolean("Death"));
        this.dataManager.set(LOOK, nbt.getFloat("Look"));
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
