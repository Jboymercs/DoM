package com.unseen.db.entity.action;

import com.unseen.db.entity.EntityAbstractHierophant;
import com.unseen.db.entity.Projectile;
import com.unseen.db.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.function.Function;
import java.util.function.Supplier;

public class ActionSummonOrbs implements IActionHiero{
    Supplier<Projectile> projectileSupplier;
    float velocity;
    public ActionSummonOrbs(Supplier<Projectile> projectileSupplier, float velocity) {
        this.projectileSupplier = projectileSupplier;
        this.velocity = velocity;
    }

    @Override
    public void performAction(EntityAbstractHierophant actor, EntityLivingBase target) {
        Function<Vec3d, Runnable> missile = (offset) -> () -> {
            Projectile projectile = projectileSupplier.get();
            projectile.setTravelRange(40);

            actor.addEvent(()-> actor.world.spawnEntity(projectile), 5);

            //Hold the Orbs
            for (int i = 0; i < 40; i++) {
                actor.addEvent(() -> {

                    Vec3d orbPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(3, 3, 0)));
                    ModUtils.setEntityPosition(projectile, orbPos);
                }, i);
            }
            //COME BACK TO THIS, CHANGES TO BE MADE
            // Throw the Orbs
            actor.addEvent(() -> {
                Vec3d vel = target.getPositionVector().subtract(ModUtils.yVec(1)).subtract(projectile.getPositionVector());
                if(actor.isInWater()) {
                    projectile.shoot(vel.x, vel.y, vel.z, 5.0f, 0.3f);
                } else {
                    projectile.shoot(vel.x, vel.y, vel.z, 0.8f, 0.3f);
                }

                ModUtils.addEntityVelocity(actor, vel.normalize().scale(-0.1));
            }, 40);

        };
        actor.addEvent(missile.apply(ModUtils.getRelativeOffset(actor, new Vec3d(3, 2, 0))),5);

    }


}
