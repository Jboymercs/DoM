package com.unseen.db.entity.render;

import com.unseen.db.entity.EntityClawTwo;
import com.unseen.db.entity.model.ModelClawTwo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderClawTwo extends GeoEntityRenderer<EntityClawTwo> {
    public RenderClawTwo(RenderManager renderManager) {
        super(renderManager, new ModelClawTwo());
    }

    @Override
    public void doRender(EntityClawTwo entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
