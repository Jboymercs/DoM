package com.unseen.db.entity;

import com.google.common.base.Predicate;
import com.unseen.db.util.ModDamageSource;
import com.unseen.db.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
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

public class EntityClaw extends EntityAbstractClaw implements IAnimatable {

    private AnimationFactory factory = new AnimationFactory(this);
    private final String ANIM_IDLE = "idle";
    private final String ANIM_BITE = "attack";
    private final String ANIM_EAT = "eat";


    public boolean eatRepeatTimerCheck = false;
    public EntityClaw(World worldIn) {
        super(worldIn);
    }
    public boolean grabbed = false;
    public int attackCooldown = 0;

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(this.isInWater()) {
            this.motionY = -0.1;
        }
        if(!this.isAttackMode()) {
            attackCooldown++;
        }
        EntityLivingBase target = this.getAttackTarget();

        //Checks for target and make sure it's close enough to initiate it's attack
        if(target != null) {
            if(!grabbed && !this.isAttackMode() && !this.isEatMode() && attackCooldown > 200) {
                double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
                double distance = Math.sqrt(distSq);
                if( distance < 4) {
                    this.setGrabAttackStart(target);
                }
            }

            this.getLookHelper().setLookPositionWithEntity(target, 30F, 30F);

        }

        if(this.grabbed && !this.isAttackMode() && !this.eatRepeatTimerCheck) {
            System.out.println("Starting Eat Attack");
            this.eatAttack();
            this.eatRepeatTimerCheck = true;
        }

        if(this.grabbed) {

            Predicate<EntityLivingBase> selection = entityLivingBase -> !(entityLivingBase instanceof EntityClaw);
            List<EntityStaticBox> stackBox = this.world.getEntitiesWithinAABB(EntityStaticBox.class, this.getEntityBoundingBox().grow(5D), selection);
            if(!stackBox.isEmpty()) {
                for (EntityStaticBox box : stackBox) {
                    if(!box.isBeingRidden()) {
                        grabbed = false;
                        this.setEatMode(false);
                        this.eatRepeatTimerCheck =false;
                    }
                }
            }
        }
    }




    public void setGrabAttackStart(EntityLivingBase targetSelect) {
        //Sets the current box if any to dead and summons a new one
        this.setAttackMode(true);
        Predicate<EntityLivingBase> selection = entityLivingBase -> !(entityLivingBase instanceof EntityClaw);
        List<EntityLivingBase> stackBox = this.world.getEntitiesWithinAABB(EntityStaticBox.class, this.getEntityBoundingBox().grow(5D), selection);
        if(!stackBox.isEmpty()) {
            for (EntityLivingBase entityLivingBase : stackBox) {
                entityLivingBase.setDead();
            }
        }
        //Spawn the new box
        EntityLivingBase stackBoxGrab = new EntityStaticBox(world);
            Vec3d pos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.6, 0)));
            stackBoxGrab.setPosition(pos.x, pos.y, pos.z);
            world.spawnEntity(stackBoxGrab);


        addEvent(()-> {
            //sets the GrabBox to the tip of it's biting point
            Vec3d yOffset = targetSelect.getPositionVector().subtract(this.getPositionVector());
            Vec3d lookVec = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(4, yOffset.y, 0)));
            ModUtils.setEntityPosition(stackBoxGrab, lookVec);
            stackBoxGrab.updateRidden();
        }, 20);
        addEvent(()-> {

            Vec3d lookVec = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(3, 0, 0)));
            ModUtils.setEntityPosition(stackBoxGrab, lookVec);
            stackBoxGrab.updateRidden();
        }, 25);
        addEvent(()-> {

            Vec3d lookVec = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2, 0, 0)));
            ModUtils.setEntityPosition(stackBoxGrab, lookVec);
            stackBoxGrab.updateRidden();
        }, 30);
        addEvent(()-> {

            Vec3d lookVec = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.6, 0, 0)));
            ModUtils.setEntityPosition(stackBoxGrab, lookVec);
            stackBoxGrab.updateRidden();
            attackCooldown = 0;
        }, 35);

        addEvent(()-> {
        this.setAttackMode(false);
        if(stackBoxGrab.isBeingRidden()) {

            this.grabbed = true;
        }

        }, 40);

    }

    public void eatAttack() {
        this.setEatMode(true);
        System.out.println("starting Eat Attack");
        //Attacks Player
        addEvent(()-> {
            EntityLivingBase target = this.getAttackTarget();
            if(target != null) {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.5, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage = this.getAttack();
                ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0f, 0, false);
            }
        }, 10);
        addEvent(()-> {
            //Check to see if box still has player on it
            Predicate<EntityLivingBase> selection = entityLivingBase -> !(entityLivingBase instanceof EntityClaw);
            List<EntityStaticBox> stackBox = this.world.getEntitiesWithinAABB(EntityStaticBox.class, this.getEntityBoundingBox().grow(3D), selection);
            if(!stackBox.isEmpty()) {
                for(EntityStaticBox box : stackBox) {
                    if(!box.isBeingRidden()) {
                        this.grabbed = false;
                    }
                }
            }

            this.eatRepeatTimerCheck = false;
        }, 15);
    }

    protected PathNavigate createNavigator(World worldIn) {
        return new PathNavigateSwimmer(this, worldIn);
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
}
