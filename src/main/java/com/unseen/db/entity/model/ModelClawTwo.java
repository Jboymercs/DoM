package com.unseen.db.entity.model;

import com.unseen.db.entity.EntityClawTwo;
import com.unseen.db.util.Reference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelClawTwo extends AnimatedGeoModel<EntityClawTwo> {

    @Override
    public ResourceLocation getModelLocation(EntityClawTwo entityClaw) {
        return new ResourceLocation(Reference.MOD_ID, "geo/entity/claw/geo.claw.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityClawTwo entityClaw) {
        return new ResourceLocation(Reference.MOD_ID, "textures/entity/claw.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityClawTwo entityClaw) {
        return new ResourceLocation(Reference.MOD_ID, "animations/animation.claw.json");
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
    @Override
    public void setLivingAnimations(EntityClawTwo entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("HeadStart");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationZ(extraData.netHeadYaw * ((float) Math.PI / 180F));

    }
}
