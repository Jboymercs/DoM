package com.unseen.db.entity.render;

import com.unseen.db.entity.EntityHeirophant;
import com.unseen.db.entity.model.ModelHeirophant;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderHeirophant extends GeoEntityRenderer<EntityHeirophant> {


    public RenderHeirophant(RenderManager renderManager) {
        super(renderManager, new ModelHeirophant());
    }

    @Override
    public void doRender(EntityHeirophant entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
