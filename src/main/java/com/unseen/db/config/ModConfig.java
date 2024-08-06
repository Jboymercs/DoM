package com.unseen.db.config;


import com.unseen.db.util.Reference;
import net.minecraftforge.common.config.Config;

@Config(modid = Reference.MOD_ID, name = Reference.NAME)
public class ModConfig {
    //This will have near the same amount of customization to End Expansion for Config Options
    @Config.Name("Hierophant Orb Projectile Damage")
    @Config.Comment("Change the damage of the orbs thrown by the Hierophant")
    @Config.RequiresMcRestart
    public static float orb_damage= 12.0f;

    @Config.Name("Hierophant Health")
    @Config.Comment("Change the health of the Hierophant")
    @Config.RequiresMcRestart
    public static double hierophant_health = 220d;

    @Config.Name("Hierophant Attack Damage")
    @Config.Comment("Determine the base damage of the Hierophant")
    @Config.RequiresMcRestart
    public static double hierophant_attack_damage = 14d;

    @Config.Name("Claw Trap Health")
    @Config.Comment("Change the health of the Claw Trap")
    @Config.RequiresMcRestart
    public static double claw_health = 10d;

    @Config.Name("Claw Trap Attack Damage")
    @Config.Comment("Change the attack damage of the claw trap upon being trapped")
    @Config.RequiresMcRestart
    public static double claw_attack_damage = 9d;

    @Config.Name("Hierophant Blindness attack")
    @Config.Comment("Change how many seconds of blindness is given when the aura hits the player from the Hierophant, in seconds.")
    @Config.RequiresMcRestart
    public static int aura_blindness_timer = 15;

    @Config.Name("Hierophant Jail Cell Attack Timer")
    @Config.Comment("Changes the timer for how long the jail cell is around the player, in seconds. WARNING, making this higher will allow the Hierophant to summon multiple if set too long")
    @Config.RequiresMcRestart
    public static int jail_cell_timer = 10;

    @Config.Name("Hierpphant's Temple spawnrate")
    @Config.Comment("Change the frequency in which the structure spawns, Higher value is more frequent, lower is less frequent")
    @Config.RequiresMcRestart
    public static int temple_structure_spawnrate = 20;

    @Config.Name("Hierophant's Temple Enabled/ Disabled")
    @Config.Comment("Change if the structure is enabled to spawn or not")
    @Config.RequiresMcRestart
    public static boolean temple_enabled_disabled = true;

    @Config.Name("Hierophant's Health Scaled Attack Factor")
    @Config.Comment("This variable increases the attack damage as the Hierophant gets lower on health, but has decreased attack damage as it has closer to full health")
    @Config.RequiresMcRestart
    public static double hierophant_scaled_factor = 0.4;

    @Config.Name("Hierophant's Health Scaled Attack Factor Enabled/Disabled")
    @Config.Comment("Enable/Disable the scaled attack of the Hierophant for consistent damage")
    @Config.RequiresMcRestart
    public static boolean scaled_attack_factor_enable_disable = true;

    @Config.Name("Temple Mob spawn chance")
    @Config.Comment("Determine the value in which mob spawns can occur in the temple, lower means higher mob spawns, higher means less")
    @Config.RangeInt(min = 0, max = 10)
    public static int mob_spawn_chance = 6;

    @Config.Name("Temple Chest spawn chance")
    @Config.Comment("Determine the value of how often chests spawn in the temple, lower means higher chest spawns, higher means less")
    @Config.RangeInt(min = 0, max = 5)
    public static int chest_spawn_chance = 3;

    @Config.Name("Projectile Damage Multiplier")
    @Config.Comment("This multiplies the base Hierophant attack damage by this modifier")
    @Config.RequiresMcRestart
    public static double projectile_modifier = 1.2d;

    @Config.Name("Thrall Health")
    @Config.Comment("Change the Health of the Thrall")
    @Config.RequiresMcRestart
    public static double thrall_health = 30d;

    @Config.Name("Thrall Attack Damage")
    @Config.Comment("Change the Attack Damage of the Thrall")
    @Config.RequiresMcRestart
    public static double thrall_attack_damage = 9d;

    @Config.Name("Hierophant's Spike Damage")
    @Config.Comment("Change the damage of the Spike")
    @Config.RequiresMcRestart
    public static float spike_attack_damage = 10f;

    @Config.Name("Hierophant Attack Speed")
    @Config.Comment("Change the cooldown in between each attack in ticks (1 seconds = 20 ticks) WARNING, going below 30 may cause game to crash!")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 30, max = 200)
    public static int hierophant_base_attack_speed = 60;
}
