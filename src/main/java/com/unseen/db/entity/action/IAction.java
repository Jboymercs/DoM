package com.unseen.db.entity.action;

import com.unseen.db.entity.EntityModBase;
import net.minecraft.entity.EntityLivingBase;

public interface IAction {
    void performAction(EntityModBase actor, EntityLivingBase target);

    IAction NONE = (actor, target) -> {
    };
}
