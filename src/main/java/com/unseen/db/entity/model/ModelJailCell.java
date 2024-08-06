package com.unseen.db.entity.model;

import com.unseen.db.entity.EntityJailCell;
import com.unseen.db.util.Reference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelJailCell extends AnimatedGeoModel<EntityJailCell> {
    @Override
    public ResourceLocation getModelLocation(EntityJailCell entityJailCell) {
        return new ResourceLocation(Reference.MOD_ID, "geo/entity/heirophant/geo.jail.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityJailCell entityJailCell) {
        return new ResourceLocation(Reference.MOD_ID, "textures/entity/jail.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityJailCell entityJailCell) {
        return new ResourceLocation(Reference.MOD_ID, "animations/animation.jail.json");
    }
}
