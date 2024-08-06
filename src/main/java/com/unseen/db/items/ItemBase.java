package com.unseen.db.items;

import com.unseen.db.Main;
import com.unseen.db.init.ModItems;
import com.unseen.db.util.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {
    public ItemBase(String name, CreativeTabs tab) {
        setUnlocalizedName(name);
        setRegistryName(name);
        if (tab != null) {
            setCreativeTab(tab);
        }

        ModItems.ITEMS.add(this);
    }

    public ItemBase(String name) {
        this(name, CreativeTabs.MISC);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
