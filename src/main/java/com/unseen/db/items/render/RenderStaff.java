package com.unseen.db.items.render;

import com.unseen.db.items.ItemStaff;
import com.unseen.db.items.model.ModelStaff;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderStaff extends GeoItemRenderer<ItemStaff> {
    public RenderStaff() {
        super(new ModelStaff());
    }
}
