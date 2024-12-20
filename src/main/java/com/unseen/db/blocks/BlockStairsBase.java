package com.unseen.db.blocks;

import com.unseen.db.Main;
import com.unseen.db.init.ModBlocks;
import com.unseen.db.init.ModItems;
import com.unseen.db.util.IHasModel;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockStairsBase extends BlockStairs implements IHasModel {

    protected BlockStairsBase(IBlockState modelState) {
        super(modelState);
    }

    public BlockStairsBase(String name, IBlockState modelState) {
        super(modelState);
        setUnlocalizedName(name);
        setRegistryName(name);

        // Add both an item as a block and the block itself
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    public BlockStairsBase(String name, IBlockState modelState, float hardness, float resistance, SoundType soundType) {
        this(name, modelState);
        setHardness(hardness);
        setResistance(resistance);
        setSoundType(soundType);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
