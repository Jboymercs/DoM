package com.unseen.db.items;

import com.unseen.db.Main;
import com.unseen.db.util.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class ItemOrb extends ItemBase implements IAnimatable, IHasModel {
    private final String ANIM_IDLE = "idle";
    private final String ANIM_SUMMON = "spawn";
    public AnimationFactory factory = new AnimationFactory(this);

    public ItemOrb(String name, CreativeTabs tabs) {
        super(name, tabs);
        this.setCreativeTab(CreativeTabs.SEARCH);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
    }
    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {

        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void registerModels() {
        {
            Main.proxy.registerItemRenderer(this, 0, "inventory");
        }}
}
