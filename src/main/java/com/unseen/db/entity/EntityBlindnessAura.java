package com.unseen.db.entity;

import com.unseen.db.config.MobConfig;
import com.unseen.db.config.ModConfig;
import com.unseen.db.util.ModColors;
import com.unseen.db.util.ModUtils;
import com.unseen.db.util.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

/**
 * An aura that casts Blindness and Slowness for a short peroid, used by the Hierophant
 */
public class EntityBlindnessAura extends EntityModBase implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);
    private final String ANIM_SHOOT = "shoot";

    public EntityBlindnessAura(World worldIn) {
        super(worldIn);
        this.setImmovable(true);
        this.setSize(0.6f, 8.0f);
        this.noClip = true;
    }
    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionZ = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;


        if(ticksExisted > 0 && ticksExisted < 60) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }
        if(ticksExisted == 55) {

            List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntityClaw || e instanceof EntityHeirophant || e instanceof EntityBlindnessAura)));

            if(!targets.isEmpty()) {
                for(EntityLivingBase entityLivingBase : targets) {
                    if(!this.world.isRemote) {
                        entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, MobConfig.aura_blindness_timer * 20, 2));
                        entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300, 1));
                    }
                }
            }

        }
        if(ticksExisted == 60) {
            this.setDead();
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "wave_controller", 0, this::predicateIdle));
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(0.5D)), ModColors.DARK_GREY, new Vec3d(0,0.1,0));
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(1.5D)), ModColors.DARK_GREY, new Vec3d(0,0.1,0));
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(3.0D)), ModColors.DARK_GREY, new Vec3d(0,0.1,0));
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(4.5D)), ModColors.DARK_GREY, new Vec3d(0,0.1,0));
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(6.0D)), ModColors.DARK_GREY, new Vec3d(0,0.1,0));
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(7.5D)), ModColors.DARK_GREY, new Vec3d(0,0.1,0));
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHOOT, false));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
