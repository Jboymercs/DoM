package com.unseen.db.entity.render;

import com.unseen.db.entity.EntityStaticBox;
import com.unseen.db.entity.model.ModelStaticBox;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderStaticBox extends GeoEntityRenderer<EntityStaticBox> {
    public RenderStaticBox(RenderManager renderManager) {
        super(renderManager, new ModelStaticBox());
    }
}
