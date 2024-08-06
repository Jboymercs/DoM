package com.unseen.db.entity;

import com.unseen.db.config.ModConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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
 * A single Jail cell bar spawned by the Hierophant in a circle to keep the player trapped
 */
public class EntityJailCell extends EntityModBase implements IAnimatable {

    private static final DataParameter<Boolean> START_MODE = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> END_MODE = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);

    public void setStartMode(boolean value) {this.dataManager.set(START_MODE, Boolean.valueOf(value));}
    public boolean isStartMode() {return this.dataManager.get(START_MODE);}
    public void setEndMode(boolean value) {this.dataManager.set(END_MODE, Boolean.valueOf(value));}
    public boolean isEndMode() {return this.dataManager.get(END_MODE);}
    private AnimationFactory factory = new AnimationFactory(this);
    private final String ANIM_START = "start";
    private final String ANIM_IDLE = "idle";
    private final String ANIM_END = "end";

    public EntityJailCell(World worldIn) {
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

        if (ticksExisted == 0) {
            this.setStartMode(true);
            addEvent(()-> this.setStartMode(false), 20);
        }

        if(ticksExisted > 1 && ticksExisted < (ModConfig.jail_cell_timer * 20)) {
            List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntityClaw || e instanceof EntityHeirophant || e instanceof EntityJailCell || e instanceof EntityThrall)));
            if(!targets.isEmpty()) {
                for (EntityLivingBase entitiesWithin : targets) {
                    this.blockUsingShield(entitiesWithin);
                }
            }
        }
        if(ticksExisted == (ModConfig.jail_cell_timer * 20)) {
            this.setEndMode(true);
            addEvent(()-> {
                this.setDead();
                this.setEndMode(false);
                }, 10);
        }

    }

    @Override
    public void entityInit() {
        this.dataManager.register(START_MODE, Boolean.valueOf(false));
        this.dataManager.register(END_MODE, Boolean.valueOf(false));
        super.entityInit();
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "wave_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "start_end", 0, this::predicateSE));
    }
    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState predicateSE(AnimationEvent<E> event) {
        if(this.isStartMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_START, false));
            return PlayState.CONTINUE;
        }
        if(this.isEndMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_END, false));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
