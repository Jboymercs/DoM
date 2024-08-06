package com.unseen.db.entity.model;

import com.unseen.db.entity.EntityHieroSpikeOp;
import com.unseen.db.util.Reference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelHieroSpikeOp extends AnimatedGeoModel<EntityHieroSpikeOp> {
    @Override
    public ResourceLocation getModelLocation(EntityHieroSpikeOp entityHieroSpike) {
        return new ResourceLocation(Reference.MOD_ID, "geo/entity/heirophant/geo.spike.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityHieroSpikeOp entityHieroSpike) {
        return new ResourceLocation(Reference.MOD_ID, "textures/entity/spike2.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityHieroSpikeOp entityHieroSpike) {
        return new ResourceLocation(Reference.MOD_ID, "animations/animation.upspike.json");
    }
}
