package com.unseen.db.util.handlers;

import com.unseen.db.entity.*;
import com.unseen.db.entity.render.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.function.Function;

public class RenderHandler {
    private static <T extends Entity, U extends ModelBase, V extends RenderModEntity> void registerModEntityRenderer(Class<T> entityClass, U model, String... textures) {
        registerModEntityRenderer(entityClass, (manager) -> new RenderModEntity(manager, model, textures));
    }

    private static <T extends Entity, U extends ModelBase, V extends RenderModEntity> void registerModEntityRenderer(Class<T> entityClass, Function<RenderManager, Render<? super T>> renderClass) {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, new IRenderFactory<T>() {
            @Override
            public Render<? super T> createRenderFor(RenderManager manager) {
                return renderClass.apply(manager);
            }
        });
    }

    private static <T extends Entity> void registerProjectileRenderer(Class<T> projectileClass) {
        registerProjectileRenderer(projectileClass, null);
    }

    /**
     * Makes a projectile render with the given item
     *
     * @param projectileClass
     */
    private static <T extends Entity> void registerProjectileRenderer(Class<T> projectileClass, Item item) {
        RenderingRegistry.registerEntityRenderingHandler(projectileClass, new IRenderFactory<T>() {
            @Override
            public Render<? super T> createRenderFor(RenderManager manager) {
                return new RenderProjectile<T>(manager, Minecraft.getMinecraft().getRenderItem(), item);
            }
        });
    }

    public static void registerGeoRenderers() {
    //Register Geckolib models

        //Temple OCean Boss
    RenderingRegistry.registerEntityRenderingHandler(EntityHeirophant.class, RenderHeirophant::new);
        //Trap entity
        RenderingRegistry.registerEntityRenderingHandler(EntityClaw.class, RenderClaw::new);
        //Utility - Static PassengerBox
        RenderingRegistry.registerEntityRenderingHandler(EntityStaticBox.class, RenderStaticBox::new);
        //Utility - Blindness Aura - Temple Boss
        RenderingRegistry.registerEntityRenderingHandler(EntityBlindnessAura.class, RenderBlindnessAura::new);
        //Utility - Jail Cell - Temple Boss
        RenderingRegistry.registerEntityRenderingHandler(EntityJailCell.class, RenderJailCell::new);
        //Utility - Projectile Orb - Temple Boss
        registerProjectileRenderer(ProjectileOrb.class);
        //Utility - Projectile Player Orb
        registerProjectileRenderer(ProjectileActionOrb.class);
        //Thrall Minion
        RenderingRegistry.registerEntityRenderingHandler(EntityThrall.class, RenderThrall::new);
        //Utility - Hierophant's Spike - Temple Boss
        RenderingRegistry.registerEntityRenderingHandler(EntityHieroSpike.class, RenderHieroSpike::new);
        //Claw V2
        RenderingRegistry.registerEntityRenderingHandler(EntityClawTwo.class, RenderClawTwo::new);
        //Utility - Hierophant's Spike Upside Down - Temple Boss
        RenderingRegistry.registerEntityRenderingHandler(EntityHieroSpikeOp.class, RenderHieroSpikeOp::new);
    }
}
