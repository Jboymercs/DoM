package com.unseen.db.entity.action;

import com.unseen.db.entity.EntityJailCell;
import com.unseen.db.entity.EntityModBase;
import com.unseen.db.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionJailCell implements IAction{
    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        ModUtils.circleCallback(4, 24, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(target.getPositionVector());
            EntityJailCell spike = new EntityJailCell(actor.world);
            spike.setPosition(pos.x, pos.y - 3, pos.z);
            actor.world.spawnEntity(spike);
        });
    }
}
