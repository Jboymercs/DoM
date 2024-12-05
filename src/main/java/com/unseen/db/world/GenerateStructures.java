package com.unseen.db.world;

import com.google.common.collect.Lists;
import com.unseen.db.config.ModConfig;
import com.unseen.db.config.WorldConfig;
import com.unseen.db.util.logger.DoMLogger;
import com.unseen.db.world.oceanTemple.WorldGenOceanTemple;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GenerateStructures implements IWorldGenerator {

    private static List<Biome> spawnBiomes;

    //Imported from End Expansion: The Lamented Islands
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int x = chunkX * 16;
        int z = chunkZ * 16;
        BlockPos pos = world.getHeight(new BlockPos(x, 0, z));
        if(isAllowedDimensionTooSpawnIn(world.provider.getDimension()) && WorldConfig.temple_enabled_disabled) {

            if(world.getBiomeForCoordsBody(pos) == getSpawnBiomes().iterator().next()) {
                if (canStructureSpawn(chunkX, chunkZ, world, WorldConfig.temple_structure_spawnrate)) {

                    new WorldGenOceanTemple().generateStructure(world, new BlockPos(x, WorldConfig.temple_spawn_height, z), Rotation.NONE);
                    //Generate Structure
                }
            }
        }
    }

    public static boolean canStructureSpawn(int chunkX, int chunkZ, World world, int frequency){
        if (frequency <= 0) return false;
        int realFreq= 48 - frequency;
        int maxDistanceBetween = realFreq + 8;

        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= maxDistanceBetween - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= maxDistanceBetween - 1;
        }

        int k = chunkX / maxDistanceBetween;
        int l = chunkZ / maxDistanceBetween;
        Random random = world.setRandomSeed(k, l, 14357617);
        k = k * maxDistanceBetween;
        l = l * maxDistanceBetween;
        k = k + random.nextInt(maxDistanceBetween - 8);
        l = l + random.nextInt(maxDistanceBetween - 8);

        return i == k && j == l;
    }

    public static boolean isAllowedDimensionTooSpawnIn(int dimensionIn) {
        for(int i : WorldConfig.list_of_dimensions) {
            if(i == dimensionIn)
                return true;
        }

        return false;
    }

    public static List<Biome> getSpawnBiomes() {
        if (spawnBiomes == null) {
            spawnBiomes = Lists.newArrayList();
            for (String str : WorldConfig.biome_allowed) {
                try {
                    Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(str));
                    if (biome != null) spawnBiomes.add(biome);
                    else DoMLogger.logError("Biome " + str + " is not registered", new NullPointerException());
                } catch (Exception e) {
                    DoMLogger.logError(str + " is not a valid registry name", e);
                }
            }
        }
        return spawnBiomes;
    }

}
