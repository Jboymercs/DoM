package com.unseen.db.entity.tileEntity;

import com.unseen.db.init.ModBlocks;
import net.minecraft.util.ITickable;

public class TileEntityDisappearingSpawner extends tileEntityMobSpawner implements ITickable {
    @Override
    protected MobSpawnerLogic getSpawnerLogic() {
        return new DisappearingSpawnerLogic(() -> world, () -> pos, ModBlocks.DISAPPEARING_SPAWNER);
    }
}
