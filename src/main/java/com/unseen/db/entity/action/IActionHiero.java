package com.unseen.db.entity.action;

import com.unseen.db.entity.EntityAbstractHierophant;
import net.minecraft.entity.EntityLivingBase;

public interface IActionHiero {
    void performAction(EntityAbstractHierophant actor, EntityLivingBase target);

    IAction NONE = (actor, target) -> {
    };
}
