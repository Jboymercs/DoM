package com.unseen.db.config;


import com.unseen.db.util.Reference;
import net.minecraftforge.common.config.Config;

@Config(modid = Reference.MOD_ID, name = "Depths of Madness/general_config")
public class ModConfig {
    //This will have near the same amount of customization to End Expansion for Config Options

    @Config.Name("Boss Scaling Health")
    @Config.Comment("Change the scaling added per player of health added to bosses, not counting one player")
    @Config.RequiresMcRestart
    public static double boss_health_scaling = 0.4;

    @Config.Name("Boss Scaling Attack Damage")
    @Config.Comment("Change the scaling added per player of attack damage to bosses, not counting one player")
    @Config.RequiresMcRestart
    public static double boss_attack_damage_scaling = 0.18;

    @Config.Name("Staff of the Forgotten Cooldown")
    @Config.Comment("Change the cooldown of the Staff of the Forgotten, in seconds")
    @Config.RequiresMcRestart
    public static int staff_cooldown = 5;

    @Config.Name("Staff of the Forgotten Durability")
    @Config.Comment("Change the durability of the staff")
    @Config.RequiresMcRestart
    public static int staff_durability = 100;
}
