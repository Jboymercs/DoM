package com.unseen.db.world.oceanTemple;

import com.unseen.db.entity.EntityHeirophant;
import com.unseen.db.entity.tileEntity.MobSpawnerLogic;
import com.unseen.db.entity.tileEntity.tileEntityMobSpawner;
import com.unseen.db.init.ModBlocks;
import com.unseen.db.init.ModEntities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenOceanTemple extends WorldGenOceanStructure{

    public WorldGenOceanTemple() {
        super("temple/temple_base", 0);
    }

    @Override
    public void generateStructure(World world, BlockPos pos, Rotation rotation) {
        super.generateStructure(world, pos, Rotation.NONE);
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random random) {
        if(function.startsWith("1")) {
            new WorldGenFourWay1().generateStructure(world, pos, Rotation.NONE);
            world.setBlockToAir(pos);
        }
        if(function.startsWith("2")) {
            new WorldGenStraight1().generateStructure(world, pos, Rotation.NONE);
            world.setBlockToAir(pos);
        }
        if(function.startsWith("r")) {
            new WorldGenRandomRoom1().generateStructure(world, pos, Rotation.NONE);
            world.setBlockToAir(pos);
        }
        if(function.startsWith("3")) {
            new WorldGenFourWay2().generateStructure(world, pos, Rotation.NONE);
            world.setBlockToAir(pos);
        }
        if(function.startsWith("4")) {
            new WorldGenStraight2().generateStructure(world, pos, Rotation.NONE);
            world.setBlockToAir(pos);
        }
        if(function.startsWith("q")) {
            new WorldGenRandomRoom2().generateStructure(world, pos, Rotation.NONE);
            world.setBlockToAir(pos);
        }
        //Issue, blocks don't spawn any mobs
        if(function.startsWith("boss")) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if(tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityHeirophant.class),  1)
                            },
                            new int[]{1},
                            1,
                            40);
                }

        }
        //Can't remember if there is any chest spawns in the center room lmao

    }
}
