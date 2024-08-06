package com.unseen.db.entity.model;

import com.unseen.db.entity.EntityHeirophant;
import com.unseen.db.util.Reference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelHeirophant extends AnimatedGeoModel<EntityHeirophant> {

    @Override
    public ResourceLocation getModelLocation(EntityHeirophant entityHeirophant) {
        return new ResourceLocation(Reference.MOD_ID, "geo/entity/heirophant/geo.hp.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityHeirophant entityHeirophant) {
        return new ResourceLocation(Reference.MOD_ID, "textures/entity/hp.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityHeirophant entityHeirophant) {
        return new ResourceLocation(Reference.MOD_ID, "animations/animation.hp.json");
    }
    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
    @Override
    public void setLivingAnimations(EntityHeirophant entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));

    }

}
