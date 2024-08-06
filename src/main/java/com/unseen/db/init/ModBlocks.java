package com.unseen.db.init;

import com.unseen.db.blocks.*;
import com.unseen.db.blocks.slab.BlockDoubleSlabBase;
import com.unseen.db.blocks.slab.BlockHalfSlabBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {
    public static final List<Block> BLOCKS = new ArrayList<Block>();
    public static final float STONE_HARDNESS = 1.7f;
    public static final float STONE_RESISTANCE = 10f;
    public static final float BRICK_HARDNESS = 2.0f;
    public static final float WOOD_HARDNESS = 1.5f;
    public static final float WOOD_RESISTANCE = 5.0f;
    public static final float PLANTS_HARDNESS = 0.2f;
    public static final float PLANTS_RESISTANCE = 2.0f;
    public static final float ORE_HARDNESS = 3.0F;
    public static final float OBSIDIAN_HARDNESS = 50;
    public static final float OBSIDIAN_RESISTANCE = 2000;

    public static final Block DISAPPEARING_SPAWNER = new BlockDisappearingSpawner("disappearing_spawner", Material.ROCK);
    public static final Block MEGA_STRUCTURE_BLOCK = new BlockMegaStructure("mega_structure_block");
    public static final Block LIGHTING_UPDATER = new BlockLightingUpdater("lighting_updater", Material.AIR).setLightLevel(0.1f);
    public static final Block TEMPLE_BRICK = new BlockBase("temple_brick", Material.ROCK, OBSIDIAN_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Block TEMPLE_POLISH_BRICK = new BlockBase("temple_polish", Material.ROCK, OBSIDIAN_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Block TEMPLE_COBBLESTONE = new BlockBase("temple_cobble", Material.ROCK, OBSIDIAN_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Block TEMPLE_CHISLE_BRICK = new BlockBase("temple_chisle_brick", Material.ROCK, OBSIDIAN_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Block GOLD_CHISLE_BRICK = new BlockBase("gold_chisle_brick", Material.ROCK, OBSIDIAN_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.METAL).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Block GOLD_TEMPLE_BRICK = new BlockBase("temple_gold_brick", Material.ROCK, OBSIDIAN_HARDNESS,OBSIDIAN_RESISTANCE, SoundType.METAL).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

    //Light Emitter
    public static final Block TEMPLE_LAMP = new BlockLamp("temple_lamp", Material.ROCK, OBSIDIAN_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setLightLevel(1.0f);


    public static final Block TEMPLE_BRICK_STAIRS = new BlockStairsBase("temple_brick_stairs", TEMPLE_BRICK.getDefaultState(), OBSIDIAN_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final BlockSlab TEMPLE_BRICK_SLAB_DOUBLE = new BlockDoubleSlabBase("temple_brick_slab_double", Material.ROCK, CreativeTabs.BUILDING_BLOCKS, ModBlocks.TEMPLE_BRICK_SLAB_HALF, OBSIDIAN_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.STONE);
    public static final BlockSlab TEMPLE_BRICK_SLAB_HALF = new BlockHalfSlabBase("temple_brick_slab_half", Material.ROCK, CreativeTabs.BUILDING_BLOCKS, ModBlocks.TEMPLE_BRICK_SLAB_HALF, ModBlocks.TEMPLE_BRICK_SLAB_DOUBLE, OBSIDIAN_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.STONE);
    public static final Block TEMPLE_PILLAR_BLOCK = new BlockPillarBase("temple_pillar", Material.ROCK).setHardness(OBSIDIAN_HARDNESS).setResistance(OBSIDIAN_RESISTANCE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final BlockSlab TEMPLE_COBBLE_SLAB_DOUBLE = new BlockDoubleSlabBase("temple_cobble_slab_double", Material.ROCK, CreativeTabs.BUILDING_BLOCKS, ModBlocks.TEMPLE_COBBLE_SLAB_HALF, OBSIDIAN_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.STONE);
    public static final BlockSlab TEMPLE_COBBLE_SLAB_HALF = new BlockHalfSlabBase("temple_cobble_slab_half", Material.ROCK, CreativeTabs.BUILDING_BLOCKS, ModBlocks.TEMPLE_COBBLE_SLAB_HALF, ModBlocks.TEMPLE_COBBLE_SLAB_DOUBLE, OBSIDIAN_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.STONE);



}
