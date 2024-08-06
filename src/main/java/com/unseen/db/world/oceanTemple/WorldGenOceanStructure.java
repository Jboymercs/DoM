package com.unseen.db.world.oceanTemple;

import com.unseen.db.config.ModConfig;
import com.unseen.db.util.ModRandom;
import com.unseen.db.util.ModUtils;
import com.unseen.db.world.WorldGenStructure;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenOceanStructure extends WorldGenStructure {
    /**
     * Small class for the base
     */

    int y_offset;

    public WorldGenOceanStructure(String structureName, int y_offset) {
        super(structureName);
        this.y_offset = y_offset;
    }

    @Override
    public boolean generate(World worldIn, Random random, BlockPos blockPos) {
        return super.generate(worldIn, random, blockPos.add(new BlockPos(0,y_offset,0)));
    }

    @Override
    public int getYGenHeight(World world, int x, int z) {
        return ModUtils.calculateGenerationHeight(world, x, z);
    }

    //A Random configuration that will base if a creature will spawn with the given tag upon spawning the structure
    public boolean generateMobSpawn() {
        int randomNumberGenerator = ModRandom.range(0, 10);
        if (randomNumberGenerator >= ModConfig.mob_spawn_chance) {
            return false;
        }
        return true;
    }

    public boolean generateChestSpawn() {
        int randomNumberChestGenerator = ModRandom.range(0, 5);
        if(randomNumberChestGenerator >= ModConfig.chest_spawn_chance) {
            return false;
        }
        return true;
    }
}
