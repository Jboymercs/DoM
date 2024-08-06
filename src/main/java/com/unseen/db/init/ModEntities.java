package com.unseen.db.init;

import com.unseen.db.Main;
import com.unseen.db.entity.*;
import com.unseen.db.entity.tileEntity.TileEntityDisappearingSpawner;
import com.unseen.db.entity.tileEntity.TileEntityMegaStructure;
import com.unseen.db.util.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.Map;

public class ModEntities {
    private static final Map<Class<? extends Entity>, String> ID_MAP = new HashMap<>();

    private static int ENTITY_START_ID = 123;
    private static int PROJECTILE_START_ID = 230;
    private static int PARTICLE_START_ID = 500;

    public static Vec3i deepBelow_mobs_spawn_egg_color = new Vec3i(0x0c4a6b, 0x000000, 0);

    //used for registering the entities
    public static void registerEntities() {
        //Temple Boss
        registerEntityWithID("hierophant", EntityHeirophant.class, ENTITY_START_ID++, 50, deepBelow_mobs_spawn_egg_color);
        //Claw Trap - Old - will redo to be the new one
        registerEntity("claw", EntityClaw.class, ENTITY_START_ID++, 50);
        //Utility - Claw Trap
        registerEntity("static_box", EntityStaticBox.class, ENTITY_START_ID++, 50);
        //Utility - Temple Boss
        registerEntity("entity_aura", EntityBlindnessAura.class, ENTITY_START_ID++, 50);
        //Utility - Temple Boss
        registerEntity("entity_jail", EntityJailCell.class, ENTITY_START_ID++, 50);
        //Utility - Orb Projectile - Temple Boss
        registerEntity("entity_orb", ProjectileOrb.class, ENTITY_START_ID++, 50);
        //Thrall Minion
        registerEntityWithID("thrall", EntityThrall.class, ENTITY_START_ID++, 50, deepBelow_mobs_spawn_egg_color);
        //Utility - Temple Boss
        registerEntity("hiero_spike", EntityHieroSpike.class, ENTITY_START_ID++, 50);
        //Utility - Temple Boss
        registerEntity("hiero_spike_up", EntityHieroSpikeOp.class, ENTITY_START_ID++, 50);
        //Utility - Projectile Orb Player Version
        registerEntity("orb_player", ProjectileActionOrb.class, ENTITY_START_ID++, 50);
        //Claw V2
        registerEntityWithID("claw_two", EntityClawTwo.class, ENTITY_START_ID++, 50, deepBelow_mobs_spawn_egg_color);



        registerTileEntity(TileEntityDisappearingSpawner.class, "disappearing_spawner_entity");
        registerTileEntity(TileEntityMegaStructure.class, "mega_structure_block");
    }
    public static String getID(Class<? extends Entity> entity) {
        if (ID_MAP.containsKey(entity)) {
            return Reference.MOD_ID + ":" + ID_MAP.get(entity);
        }
        throw new IllegalArgumentException("Mapping of an entity has not be registered for the deeper below spawner system.");
    }

    private static void registerEntityWithID(String name, Class<? extends Entity> entity, int id, int range, Vec3i eggColor) {
        EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, true, eggColor.getX(), eggColor.getY());
        ID_MAP.put(entity, name);
    }

    private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, Vec3i eggColor) {
        EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, true, eggColor.getX(), eggColor.getY());
    }

    private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range) {
        EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, true);
    }

    private static void registerFastProjectile(String name, Class<? extends Entity> entity, int id, int range) {
        EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, false);
    }

    private static void registerTileEntity(Class<? extends TileEntity> entity, String name) {
        GameRegistry.registerTileEntity(entity, new ResourceLocation(Reference.MOD_ID + ":" + name));
    }
}
