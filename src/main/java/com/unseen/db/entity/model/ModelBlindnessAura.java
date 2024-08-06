package com.unseen.db.entity.model;

import com.unseen.db.entity.EntityBlindnessAura;
import com.unseen.db.util.Reference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelBlindnessAura extends AnimatedGeoModel<EntityBlindnessAura> {
    @Override
    public ResourceLocation getModelLocation(EntityBlindnessAura entityBlindnessAura) {
        return new ResourceLocation(Reference.MOD_ID, "geo/entity/heirophant/geo.aura.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBlindnessAura entityBlindnessAura) {
        if(entityBlindnessAura.ticksExisted > 1 && entityBlindnessAura.ticksExisted < 10) {
            return new ResourceLocation(Reference.MOD_ID, "textures/entity/wave/wave1.png");
        }
        if(entityBlindnessAura.ticksExisted > 10 && entityBlindnessAura.ticksExisted < 20) {
            return new ResourceLocation(Reference.MOD_ID, "textures/entity/wave/wave2.png");
        }
        if(entityBlindnessAura.ticksExisted > 20 && entityBlindnessAura.ticksExisted < 30) {
            return new ResourceLocation(Reference.MOD_ID, "textures/entity/wave/wave3.png");
        }

        if(entityBlindnessAura.ticksExisted > 40 && entityBlindnessAura.ticksExisted < 45) {
            return new ResourceLocation(Reference.MOD_ID, "textures/entity/wave/wave4.png");
        }

        if(entityBlindnessAura.ticksExisted > 45 && entityBlindnessAura.ticksExisted < 52) {
            return new ResourceLocation(Reference.MOD_ID, "textures/entity/wave/wave5.png");
        }
        if(entityBlindnessAura.ticksExisted > 52 && entityBlindnessAura.ticksExisted < 55) {
            return new ResourceLocation(Reference.MOD_ID, "textures/entity/wave/wave2.png");
        }
        return new ResourceLocation(Reference.MOD_ID, "textures/entity/wave/wave.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityBlindnessAura entityBlindnessAura) {
        return new ResourceLocation(Reference.MOD_ID, "animations/animation.aura.json");
    }
}
