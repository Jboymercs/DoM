package com.unseen.db.entity.action;

import com.unseen.db.entity.EntityHieroSpike;
import com.unseen.db.entity.Projectile;
import com.unseen.db.init.ModSoundHandler;
import com.unseen.db.util.ModUtils;
import net.minecraft.util.math.Vec3d;

public class ActionPlayerSpikes implements IActionPlayer{

    @Override
    public void performAction(Projectile actor, Vec3d targetLocation) {
        //1
        ModUtils.circleCallback(0, 1, (pos) -> {
            pos = new Vec3d(pos.x, 0, pos.y).add(targetLocation);
            EntityHieroSpike spike = new EntityHieroSpike(actor.world);
            spike.setPosition(pos.x, pos.y - 3, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(1, 6, (pos) -> {
            pos = new Vec3d(pos.x, 0, pos.y).add(targetLocation);
            EntityHieroSpike spike = new EntityHieroSpike(actor.world);
            spike.setPosition(pos.x, pos.y - 3, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(2, 9, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(targetLocation);
            EntityHieroSpike spike = new EntityHieroSpike(actor.world);
            spike.setPosition(pos.x, pos.y - 3, pos.z);
            actor.world.spawnEntity(spike);
        });
        actor.playSound(ModSoundHandler.SPIKE_SUMMON, 0.8f, 1.0f);
    }
}
