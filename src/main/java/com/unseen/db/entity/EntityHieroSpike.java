package com.unseen.db.entity;

import com.unseen.db.config.MobConfig;
import com.unseen.db.config.ModConfig;
import com.unseen.db.util.ModDamageSource;
import com.unseen.db.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
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
 * Hierophant's spike summon class, a basic entity that deals damage and disappears
 */

public class EntityHieroSpike extends EntityModBase implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);

    private final String ANIM_SHOOT = "shoot";


    public EntityHieroSpike(World worldIn) {
        super(worldIn);
        this.setSize(0.8f, 8.0f);
        this.setImmovable(true);
        this.noClip = true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
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
        if(ticksExisted > 10 && ticksExisted < 20) {
            List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntityClaw || e instanceof EntityHeirophant || e instanceof EntityThrall || e instanceof EntityHieroSpike || e instanceof EntityJailCell)));
            if(!targets.isEmpty()) {
                for (EntityLivingBase entityLivingBase : targets) {
                    Vec3d damageDealtPos = entityLivingBase.getPositionVector();
                    DamageSource builder = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                    float damage = MobConfig.spike_attack_damage;
                    ModUtils.handleAreaImpact(1.0f, (e)-> damage, this, damageDealtPos, builder, 0.5f, 0, false);
                }
            }
        }


        if(ticksExisted == 25) {
            this.setDead();
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "wave_spike_controller", 0, this::predicateIdle));
    }

    private <E extends IAnimatable>PlayState predicateIdle (AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHOOT, false));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
