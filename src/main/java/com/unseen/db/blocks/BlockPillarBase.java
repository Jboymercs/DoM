package com.unseen.db.blocks;

import com.unseen.db.Main;
import com.unseen.db.init.ModBlocks;
import com.unseen.db.init.ModItems;
import com.unseen.db.util.IHasModel;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;

public class BlockPillarBase extends BlockRotatedPillar implements IHasModel {
    public BlockPillarBase(String name, Material materialIn) {
        super(materialIn);
        setUnlocalizedName(name);
        setRegistryName(name);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Y));
        // Add both an item as a block and the block itself
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
