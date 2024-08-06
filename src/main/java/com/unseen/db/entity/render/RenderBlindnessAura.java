package com.unseen.db.entity.render;

import com.unseen.db.entity.EntityBlindnessAura;
import com.unseen.db.entity.model.ModelBlindnessAura;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderBlindnessAura extends GeoEntityRenderer<EntityBlindnessAura> {
    public RenderBlindnessAura(RenderManager renderManager) {
        super(renderManager, new ModelBlindnessAura());
    }
}
