package com.unseen.db.entity.model;

import com.unseen.db.entity.EntityStaticBox;
import com.unseen.db.util.Reference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelStaticBox  extends AnimatedGeoModel<EntityStaticBox> {
    @Override
    public ResourceLocation getModelLocation(EntityStaticBox entityStaticBox) {
        return new ResourceLocation(Reference.MOD_ID, "geo/entity/claw/geo.box.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityStaticBox entityStaticBox) {
        return new ResourceLocation(Reference.MOD_ID, "textures/entity/box.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityStaticBox entityStaticBox) {
        return new ResourceLocation(Reference.MOD_ID, "animations/animation.box.json");
    }
}
