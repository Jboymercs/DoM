package com.unseen.db.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;

import javax.annotation.Nullable;

public abstract class StructureWorldGen extends MapGenStructure {

    private int spacing;
    private int offset;
    private int odds;

    public StructureWorldGen(int spacing, int offset, int odds) {
        this.odds = odds;
        this.spacing = spacing;
        this.offset = offset;
    }

    @Override
    public String getStructureName() {
        return null;
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos(World worldIn, BlockPos blockPos, boolean findUnexplored) {
        this.world = worldIn;

        return findNearestStructurePosBySpacing(worldIn, this, blockPos, 20, 11, 10387313, true, 100, findUnexplored);

    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        return Math.abs(chunkX % spacing) == offset && Math.abs(chunkZ % spacing) == offset && rand.nextInt(odds) == 0;
    }
}
