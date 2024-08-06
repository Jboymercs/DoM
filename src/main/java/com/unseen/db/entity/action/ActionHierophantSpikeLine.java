package com.unseen.db.entity.action;

import com.unseen.db.entity.EntityHieroSpike;
import com.unseen.db.entity.EntityModBase;
import com.unseen.db.init.ModSoundHandler;
import com.unseen.db.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.atomic.AtomicReference;

public class ActionHierophantSpikeLine implements IAction{
    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {

            Vec3d targetPosition = target.getPositionVector();
            Vec3d throwerPosition = actor.getPositionVector();
            Vec3d dir = targetPosition.subtract(throwerPosition).normalize();
            AtomicReference<Vec3d> spawnPos = new AtomicReference<>(throwerPosition);

            for (int t = 3; t < 16; t += 1) {
                int additive = t;
                actor.addEvent(() -> {
                    actor.playSound(ModSoundHandler.SPIKE_SUMMON, 0.8f, 1.0f);
                    ModUtils.lineCallback(throwerPosition.add(dir), throwerPosition.add(dir.scale(additive)), additive * 2, (pos, r) -> {
                        spawnPos.set(pos);
                    });
                    Vec3d initPos = spawnPos.get();
                    EntityHieroSpike crystal = new EntityHieroSpike(actor.world);
                    BlockPos blockPos = new BlockPos(initPos.x, initPos.y, initPos.z);
                    crystal.setPosition(blockPos.getX(), blockPos.getY() -3, blockPos.getZ());
                    actor.world.spawnEntity(crystal);

                }, t);
            }

    }
}
