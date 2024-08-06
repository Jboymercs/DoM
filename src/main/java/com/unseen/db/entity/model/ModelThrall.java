package com.unseen.db.entity.model;

import com.unseen.db.entity.EntityThrall;
import com.unseen.db.util.Reference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelThrall extends AnimatedGeoModel<EntityThrall> {
    @Override
    public ResourceLocation getModelLocation(EntityThrall entityThrall) {
        return new ResourceLocation(Reference.MOD_ID, "geo/entity/thrall/geo.thrall.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityThrall entityThrall) {
        return new ResourceLocation(Reference.MOD_ID, "textures/entity/thrall.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityThrall entityThrall) {
        return new ResourceLocation(Reference.MOD_ID, "animations/animation.thrall.json");
    }
}
