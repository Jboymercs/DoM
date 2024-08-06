package com.unseen.db.entity.action;

import com.unseen.db.entity.EntityBlindnessAura;
import com.unseen.db.entity.EntityModBase;
import com.unseen.db.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionSpawnBlindnessAura implements IAction {


    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        EntityBlindnessAura spike1 = new EntityBlindnessAura(actor.world);
        Vec3d pos2 = target.getPositionVector();
        spike1.setPosition(pos2.x, pos2.y - 3, pos2.z);
        actor.world.spawnEntity(spike1);

        ModUtils.circleCallback(1, 8, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(target.getPositionVector());
            EntityBlindnessAura spike = new EntityBlindnessAura(actor.world);
            spike.setPosition(pos.x, pos.y - 3, pos.z);
            actor.world.spawnEntity(spike);
        });
    }
}
