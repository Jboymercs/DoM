package com.unseen.db.entity;

import com.google.common.base.Predicate;
import com.unseen.db.entity.ai.IHostileMount;
import com.unseen.db.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class EntityStaticBox extends EntityModBase implements IAnimatable, IHostileMount {
    private AnimationFactory factory = new AnimationFactory(this);
    public EntityStaticBox(World worldIn) {
        super(worldIn);
        this.setSize(0.1f, 0.1f);
        this.setNoGravity(true);
        this.noClip = true;
    }

    @Override
    public void onUpdate() {
        //Tests for to see if a Claw is nearby, if not the box will remove itself
        Predicate<EntityLivingBase> selection = entityLivingBase -> !(entityLivingBase instanceof EntityStaticBox);
        List<EntityLivingBase> stack = this.world.getEntitiesWithinAABB(EntityClaw.class, this.getEntityBoundingBox().grow(5D), selection);

        if(stack.isEmpty()) {
            this.setDead();
            this.removePassengers();
        }


        List<EntityLivingBase> stackPlayer = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(2D), selection);
        if(!stackPlayer.isEmpty() && !this.isBeingRidden()) {
         for(EntityLivingBase entityLivingBase: stackPlayer) {
             if(entityLivingBase instanceof EntityPlayer) {
                 this.world.setEntityState(this, (byte) 5);
                 this.startRidingTargeted(entityLivingBase);
             }
         }


        }

        if(!this.getPassengers().isEmpty()) {
            if(this.getPassengers().get(0).isSneaking()) {
                this.getPassengers().get(0).setSneaking(false);
            }
            for(EntityLivingBase player: stackPlayer) {
                if(player instanceof EntityPlayer) {
                    if(player.isDead) {
                        this.setDead();
                    }
                }
            }
        }

        if (!this.getPassengers().isEmpty()) {
            this.getLookHelper().setLookPositionWithEntity(getPassengers().get(0), 100F, 100F);

            // push out of user in wall
            Vec3d riderPos = this.getRiderPosition();
        }
        super.onUpdate();
    }



    @Override
    public boolean canRiderInteract() {
        return true;
    }

    @Override
    public void updatePassenger(Entity passenger) {
        if (!this.getPassengers().isEmpty()) {
            Vec3d riderPos = this.getRiderPosition();

            this.getPassengers().get(0).setPosition(riderPos.x, riderPos.y, riderPos.z);
        }

    }

    @Override
    public double getMountedYOffset() {
        return 0.25D;
    }

    private Vec3d getRiderPosition() {
        if (!this.getPassengers().isEmpty()) {
            float distance = 0.2F;

            double dx = Math.cos((this.rotationYaw + 90) * Math.PI / 180.0D) * distance;
            double dz = Math.sin((this.rotationYaw + 90) * Math.PI / 180.0D) * distance;

            return new Vec3d(this.posX + dx, this.posY + this.getMountedYOffset() + this.getPassengers().get(0).getYOffset(), this.posZ + dz);
        } else {
            return new Vec3d(this.posX, this.posY, this.posZ);
        }
    }

    @Override
    public boolean isPushedByWater()
    {
        return false;
    }

    public boolean canBreatheUnderwater() {
        return true;
    }
    protected PathNavigate createNavigator(World worldIn) {
        return new PathNavigateSwimmer(this, worldIn);
    }
    public void startRidingTargeted(EntityLivingBase targets) {
        if(this.getPassengers().isEmpty() && !targets.isRiding()) {
            targets.startRiding(this);
        }
    }


    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(500D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(6D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
    }

    @Override
    protected void initEntityAI() {

        ModUtils.removeTaskOfType(this.tasks, EntityAILookIdle.class);
        ModUtils.removeTaskOfType(this.tasks, EntityAISwimming.class);
    }

    @Override
    public void registerControllers(AnimationData animationData) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
