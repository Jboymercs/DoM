package com.unseen.db.entity;

import com.google.common.base.Predicate;
import com.unseen.db.init.ModSoundHandler;
import com.unseen.db.util.ModColors;
import com.unseen.db.util.ModDamageSource;
import com.unseen.db.util.ModUtils;
import com.unseen.db.util.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
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

public class EntityClawTwo extends EntityAbstractClaw implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);
    private final String ANIM_IDLE = "idle";
    private final String ANIM_BITE = "attack";
    private final String ANIM_EAT = "eat";

    public boolean isEatAttack;
    public boolean isPullAttack;
    public int damageCooldown = 0;
    public int soundCoolDown = 0;

    public int pullAttackCoolDown = 0;

    public EntityClawTwo(World worldIn) {
        super(worldIn);
        this.setImmovable(true);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(this.isInWater()) {
            this.motionY = -0.1;
        }
        Predicate<EntityLivingBase> selection = entityLivingBase -> !(entityLivingBase instanceof EntityClaw);
        List<EntityPlayer> playersnearby = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(5.0D),selection );
        pullAttackCoolDown++;
        if(pullAttackCoolDown > 80) {


            if (!playersnearby.isEmpty()) {

                for (EntityPlayer entity : playersnearby) {
                    if (!this.isPullAttack && !entity.isCreative()) {
                        this.isPullAttack = true;
                    }
                    if(pullAttackCoolDown > 260 && !this.isEatAttack) {
                        pullAttackCoolDown = 0;
                    }
                    if (this.isPullAttack) {
                        this.onPullAttack(entity);
                        if (soundCoolDown > 40) {
                            this.playSound(ModSoundHandler.CLAW_PULL, 1.6f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
                            soundCoolDown = 0;
                        } else {
                            soundCoolDown++;
                        }
                    }
                }
            } else {
                pullAttackCoolDown = 0;
                this.isPullAttack = false;
            }
        }

        EntityLivingBase target = this.getAttackTarget();

        if(target != null && !world.isRemote) {
            this.getLookHelper().setLookPositionWithEntity(target, 30F, 30F);
            double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            double distance = Math.sqrt(distSq);
            if(distance < 2) {
                this.isEatAttack = true;
                this.setEatMode(true);
                if(damageCooldown > 60) {
                    this.eatAttack(target);
                    damageCooldown = 0;
                } else {
                    damageCooldown++;
                }
            }
            else {
                this.isEatAttack = false;
                this.setEatMode(false);
            }
        }
    }
    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        if(this.isPullAttack) {
            if (rand.nextInt(8) == 0) {
                world.setEntityState(this, ModUtils.PARTICLE_BYTE);
            }
        }
    }

    public void onPullAttack(EntityLivingBase target) {
        if (!target.isBeingRidden() && this.canEntityBeSeen(target)) {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 0, 0)));
            double d0 = (offset.x - target.posX) * 0.04;
            double d2 = (offset.y - target.posY) * 0.04;
            double d1 = (this.posZ - target.posZ) * 0.04;
            target.addVelocity(d0, d2, d1);

        }

    }
    @Override
    public void handleStatusUpdate(byte id) {
    if(id == ModUtils.PARTICLE_BYTE) {
        EntityLivingBase target = this.getAttackTarget();
        if(target != null) {
            Vec3d motionParticles = target.getPositionVector().subtract(this.getPositionVector()).scale(0.6D);
            ParticleManager.spawnColoredSmoke(world, target.getPositionVector(), ModColors.RED, motionParticles);
        }

    }
        super.handleStatusUpdate(id);
    }

    public void eatAttack(EntityLivingBase targetToo) {
        if(targetToo != null) {
            this.playSound(ModSoundHandler.CLAW_CATCH, 1.4f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.5, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0f, 0, false);
        }
    }


    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttack));
        animationData.addAnimationController(new AnimationController(this, "eat_controller", 0, this::predicateEat));
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isEatMode() && !this.isAttackMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateEat(AnimationEvent<E> event) {
        if(!this.isAttackMode()) {
            if(this.isEatMode()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_EAT, true));
                return PlayState.CONTINUE;
            }
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        if(this.isAttackMode() && !this.isEatMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BITE, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    protected PathNavigate createNavigator(World worldIn) {
        return new PathNavigateSwimmer(this, worldIn);
    }
}
