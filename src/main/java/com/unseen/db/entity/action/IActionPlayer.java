package com.unseen.db.entity.action;

import com.unseen.db.entity.Projectile;
import net.minecraft.util.math.Vec3d;

public interface IActionPlayer {

    void performAction(Projectile actor, Vec3d targetLocation);

    IAction NONE = (actor, targetLocation) -> {
    };
}
