package com.unseen.db.entity.action;

import com.unseen.db.entity.EntityHieroSpike;
import com.unseen.db.entity.EntityModBase;
import com.unseen.db.init.ModSoundHandler;
import com.unseen.db.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionHierophantSpikeAOE implements IAction{
    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        /**
         * Hierophant's Spike AOE attack
         */
        //1
        ModUtils.circleCallback(2, 12, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityHieroSpike spike = new EntityHieroSpike(actor.world);
            spike.setPosition(pos.x, pos.y -3, pos.z);
            actor.world.spawnEntity(spike);
        });
        actor.playSound(ModSoundHandler.SPIKE_SUMMON, 0.8f, 1.0f);
        //2
        actor.addEvent(()-> ModUtils.circleCallback(3, 16, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityHieroSpike spike = new EntityHieroSpike(actor.world);
            spike.setPosition(pos.x, pos.y -3, pos.z);
            actor.world.spawnEntity(spike);
        }), 5);
        actor.addEvent(()-> actor.playSound(ModSoundHandler.SPIKE_SUMMON, 0.8f, 1.0f), 5);
        //3
        actor.addEvent(()-> ModUtils.circleCallback(4, 20, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityHieroSpike spike = new EntityHieroSpike(actor.world);
            spike.setPosition(pos.x, pos.y -3, pos.z);
            actor.world.spawnEntity(spike);
        }), 10);
        actor.addEvent(()-> actor.playSound(ModSoundHandler.SPIKE_SUMMON, 0.8f, 1.0f), 10);
        //4
        actor.addEvent(()-> ModUtils.circleCallback(5, 24, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityHieroSpike spike = new EntityHieroSpike(actor.world);
            spike.setPosition(pos.x, pos.y -3, pos.z);
            actor.world.spawnEntity(spike);
        }), 15);
        actor.addEvent(()-> actor.playSound(ModSoundHandler.SPIKE_SUMMON, 0.8f, 1.0f), 15);
        //5
        actor.addEvent(()-> ModUtils.circleCallback(6, 28, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            EntityHieroSpike spike = new EntityHieroSpike(actor.world);
            spike.setPosition(pos.x, pos.y -3, pos.z);
            actor.world.spawnEntity(spike);
        }), 20);
        actor.addEvent(()-> actor.playSound(ModSoundHandler.SPIKE_SUMMON, 0.8f, 1.0f), 20);

    }
}
