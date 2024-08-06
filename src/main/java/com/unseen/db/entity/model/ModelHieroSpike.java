package com.unseen.db.entity.model;

import com.unseen.db.entity.EntityHieroSpike;
import com.unseen.db.util.Reference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelHieroSpike extends AnimatedGeoModel<EntityHieroSpike> {
    @Override
    public ResourceLocation getModelLocation(EntityHieroSpike entityHieroSpike) {
        return new ResourceLocation(Reference.MOD_ID, "geo/entity/heirophant/geo.spike.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityHieroSpike entityHieroSpike) {
        return new ResourceLocation(Reference.MOD_ID, "textures/entity/spike.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityHieroSpike entityHieroSpike) {
         return new ResourceLocation(Reference.MOD_ID, "animations/animation.spike.json");
    }
}
