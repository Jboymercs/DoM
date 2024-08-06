package com.unseen.db.proxy;

import com.unseen.db.util.handlers.RenderHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy{


    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }

    @Override
    public void setCustomState(Block block, IStateMapper mapper) {
        ModelLoader.setCustomStateMapper(block, mapper);
    }

    @Override
    public void init() {
        if (!Minecraft.getMinecraft().getFramebuffer().isStencilEnabled()) {
            Minecraft.getMinecraft().getFramebuffer().enableStencil();
        }

        //Handles the Geckolib Entities Renderers
        RenderHandler.registerGeoRenderers();
        super.init();

    }


}
