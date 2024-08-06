package com.unseen.db.blocks;

import com.unseen.db.Main;
import com.unseen.db.entity.tileEntity.TileEntityMegaStructure;
import com.unseen.db.init.ModBlocks;
import com.unseen.db.init.ModItems;
import com.unseen.db.util.IHasModel;
import net.minecraft.block.BlockStructure;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMegaStructure extends BlockStructure implements IHasModel {
    public BlockMegaStructure(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);

        // Add both an item as a block and the block itself
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityMegaStructure();
    }
}
