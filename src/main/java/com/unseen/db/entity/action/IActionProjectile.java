package com.unseen.db.entity.action;

import com.unseen.db.entity.Projectile;
import net.minecraft.entity.EntityLivingBase;

public interface IActionProjectile {
    void performAction(Projectile actor, EntityLivingBase target);

    IAction NONE = (actor, target) -> {
    };
}
