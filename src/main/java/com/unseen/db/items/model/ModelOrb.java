package com.unseen.db.items.model;

import com.unseen.db.items.ItemOrb;
import com.unseen.db.util.Reference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelOrb extends AnimatedGeoModel<ItemOrb> {
    @Override
    public ResourceLocation getModelLocation(ItemOrb itemOrb) {
        return new ResourceLocation(Reference.MOD_ID, "geo/item/geo.orb.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemOrb itemOrb) {
        return new ResourceLocation(Reference.MOD_ID, "textures/item/orb.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemOrb itemOrb) {
        return new ResourceLocation(Reference.MOD_ID, "animations/animation.orb.json");
    }
}
