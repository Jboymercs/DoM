package com.unseen.db.entity.render;

import com.unseen.db.entity.EntityJailCell;
import com.unseen.db.entity.model.ModelJailCell;
import com.unseen.db.entity.render.geo.LayerGenericGlow;
import com.unseen.db.entity.render.geo.RenderAbstractGeoEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderJailCell extends RenderAbstractGeoEntity<EntityJailCell> {
    public RenderJailCell(RenderManager renderManager) {
        super(renderManager, new ModelJailCell());
        this.addLayer(new LayerGenericGlow<EntityJailCell>(this, this.TEXTURE_LOCATION, this.MODEL_LOCATION));
    }

    @Override
    public void doRender(EntityJailCell entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
