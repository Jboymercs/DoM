package com.unseen.db.entity.render;

import com.unseen.db.entity.EntityHieroSpikeOp;
import com.unseen.db.entity.model.ModelHieroSpikeOp;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderHieroSpikeOp extends GeoEntityRenderer<EntityHieroSpikeOp> {

    public RenderHieroSpikeOp(RenderManager renderManager) {
        super(renderManager, new ModelHieroSpikeOp());
    }

    @Override
    public void doRender(EntityHieroSpikeOp entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
