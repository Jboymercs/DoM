package com.unseen.db.proxy;

import com.unseen.db.Main;
import com.unseen.db.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

    public void registerItemRenderer(Item item, int meta, String id) {
    }
    public void setCustomState(Block block, IStateMapper mapper) {
    }
    public void init() {
        Main.network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.CHANNEL_NAME);


    }
}
