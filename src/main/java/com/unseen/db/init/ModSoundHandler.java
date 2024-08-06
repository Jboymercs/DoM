package com.unseen.db.init;

import com.unseen.db.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModSoundHandler {
    //Boss Ambience
    public static SoundEvent HIEROPHANT_IDLE_ONE;
    public static SoundEvent HIEROPHANT_IDLE_TWO;
    public static SoundEvent HIEROPHANT_IDLE_THREE;
    public static SoundEvent HIEROPHANT_HURT_ONE;
    public static SoundEvent HIEROPHANT_HURT_TWO;
    public static SoundEvent HIEROPHANT_DEATH_SOUND;
    public static SoundEvent HIEROPHANT_SCREAM;
    //Boss Actions
    public static SoundEvent HIEROPHANT_MELEE_STRIKE;
    public static SoundEvent HIEROPHANT_BELL_RING;
    public static SoundEvent HIEROPHANT_SPIKE_RING;
    public static SoundEvent HIEROPHANT_SPIKE_LINE;
    public static SoundEvent HIEROPHANT_SUMMON_THRALL;
    public static SoundEvent HIEROPHANT_SUMMON_ORB;

    //Spike Actions
    public static SoundEvent SPIKE_SUMMON;

    //Ambience
    public static SoundEvent TEMPLE_NOISE;

    //Thrall
    public static SoundEvent THRALL_IDLE;
    public static SoundEvent THRALL_HURT;
    public static SoundEvent THRALL_ATTACK;
    public static SoundEvent THRALL_DEATH;

    // Claw
    public static SoundEvent CLAW_PULL;
    public static SoundEvent CLAW_CATCH;

    //Wand
    public static SoundEvent STAFF_CAST_WATER;
    public static SoundEvent STAFF_CAST_LAND;



    public static void registerSounds() {
    HIEROPHANT_IDLE_ONE = registerSound("boss.idle1", "entity");
    HIEROPHANT_IDLE_TWO = registerSound("boss.idle2", "entity");
    HIEROPHANT_IDLE_THREE = registerSound("boss.idle3", "entity");
    HIEROPHANT_HURT_ONE = registerSound("boss.hurt1", "entity");
    HIEROPHANT_HURT_TWO = registerSound("boss.hurt2", "entity");
    HIEROPHANT_DEATH_SOUND = registerSound("boss.death", "entity");
    HIEROPHANT_SCREAM = registerSound("boss.scream", "entity");

    HIEROPHANT_MELEE_STRIKE = registerSound("boss.strike", "entity");
    HIEROPHANT_BELL_RING = registerSound("boss.bell", "entity");
    HIEROPHANT_SPIKE_RING = registerSound("boss.spike_ring", "entity");
    HIEROPHANT_SPIKE_LINE = registerSound("boss.spike_line", "entity");
    HIEROPHANT_SUMMON_ORB = registerSound("boss.orb", "entity");
    HIEROPHANT_SUMMON_THRALL = registerSound("boss.thrall", "entity");

    SPIKE_SUMMON = registerSound("object.spike", "entity");

    TEMPLE_NOISE = registerSound("temple.noise", "ambience");

    THRALL_IDLE = registerSound("thrall.idle1", "entity");
    THRALL_HURT = registerSound("thrall.hurt1", "entity");
    THRALL_ATTACK = registerSound("thrall.attack", "entity");
    THRALL_DEATH = registerSound("thrall.death", "entity");

    CLAW_CATCH = registerSound("claw.catch", "entity");
    CLAW_PULL = registerSound("claw.pull1", "entity");

    STAFF_CAST_LAND = registerSound("object.staffland", "entity");
    STAFF_CAST_WATER = registerSound("object.staffwater", "entity");
    }

    private static SoundEvent registerSound(String name, String category) {
        String fullName = category + "." + name;
        ResourceLocation location = new ResourceLocation(Reference.MOD_ID, fullName);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(fullName);
        ForgeRegistries.SOUND_EVENTS.register(event);

        return event;
    }
}
