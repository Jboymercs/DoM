package com.unseen.db.items.render;

import com.unseen.db.items.ItemOrb;
import com.unseen.db.items.model.ModelOrb;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderOrb extends GeoItemRenderer<ItemOrb> {
    public RenderOrb() {
        super(new ModelOrb());
    }
}
