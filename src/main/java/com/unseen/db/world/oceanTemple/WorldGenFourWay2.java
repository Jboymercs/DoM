package com.unseen.db.world.oceanTemple;

import com.unseen.db.entity.EntityClawTwo;
import com.unseen.db.entity.EntityThrall;
import com.unseen.db.entity.tileEntity.MobSpawnerLogic;
import com.unseen.db.entity.tileEntity.tileEntityMobSpawner;
import com.unseen.db.init.ModBlocks;
import com.unseen.db.init.ModEntities;
import com.unseen.db.util.ModRandom;
import com.unseen.db.util.Reference;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenFourWay2 extends GenerateRandomRoom{
    public String V1 = "temple/four_way_2_1";
    public String V2 = "temple/four_way_2_2";

    private static final ResourceLocation LOOT = new ResourceLocation(Reference.MOD_ID, "ocean_temple_level_two");
    public String V3 = "temple/four_way_2_3";
    public String[] Options = {V1, V2, V3};
    public WorldGenFourWay2() {
        super("", -1);
        String result = ModRandom.choice(Options);
        this.structureName = result;
    }
    @Override
    public void generateStructure(World world, BlockPos pos, Rotation rotation) {
        super.generateStructure(world, pos, Rotation.NONE);
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random random) {
        //Mob Spawns
        if(function.startsWith("mob")) {
            if(generateMobSpawn()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if(tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityThrall.class),  1),
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityClawTwo.class), 1)
                            },
                            new int[]{1, 1},
                            1,
                            16);
                }
            } else {
                world.setBlockToAir(pos);
            }
        }
        //Chest Spawns
        if(function.startsWith("chest")) {
            if(generateChestSpawn()) {
                TileEntity tileEntity = world.getTileEntity(pos.down());
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                if (tileEntity instanceof TileEntityChest) {
                    TileEntityChest chest = (TileEntityChest) tileEntity;
                    chest.setLootTable(LOOT, random.nextLong());

                }
            } else {
                world.setBlockToAir(pos);
                world.setBlockToAir(pos.down());
            }
        }
    }
}
