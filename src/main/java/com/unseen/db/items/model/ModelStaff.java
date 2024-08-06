package com.unseen.db.items.model;

import com.unseen.db.items.ItemStaff;
import com.unseen.db.util.Reference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelStaff extends AnimatedGeoModel<ItemStaff> {
    @Override
    public ResourceLocation getModelLocation(ItemStaff itemStaff) {
        return new ResourceLocation(Reference.MOD_ID, "geo/item/geo.staff.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemStaff itemStaff) {
        return new ResourceLocation(Reference.MOD_ID, "textures/item/staff.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemStaff itemStaff) {
        return new ResourceLocation(Reference.MOD_ID, "animations/animation.staff.json");
    }
}
