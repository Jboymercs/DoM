package com.unseen.db.config;

import com.unseen.db.util.Reference;
import net.minecraftforge.common.config.Config;

@Config(modid = Reference.MOD_ID, name = "Depths of Madness/world_config")
public class WorldConfig {
    @Config.Name("Hierpphant's Temple spawnrate")
    @Config.Comment("Change the frequency in which the structure spawns, Higher value is more frequent, lower is less frequent")
    @Config.RequiresMcRestart
    public static int temple_structure_spawnrate = 20;

    @Config.Name("Hierophant's Temple Enabled/ Disabled")
    @Config.Comment("Change if the structure is enabled to spawn or not")
    @Config.RequiresMcRestart
    public static boolean temple_enabled_disabled = true;

    @Config.Name("Temple Mob spawn chance")
    @Config.Comment("Determine the value in which mob spawns can occur in the temple, lower means higher mob spawns, higher means less")
    @Config.RangeInt(min = 0, max = 10)
    public static int mob_spawn_chance = 6;

    @Config.Name("Temple Chest spawn chance")
    @Config.Comment("Determine the value of how often chests spawn in the temple, lower means higher chest spawns, higher means less")
    @Config.RangeInt(min = 0, max = 5)
    public static int chest_spawn_chance = 3;

    @Config.Name("Hierophant Temple Dimensions allowed in!")
    @Config.Comment("Select what dimensions are applicable for this structure to spawn in")
    @Config.RequiresMcRestart
    public static int[] list_of_dimensions = {0};

    @Config.Name("Hierophant Temple Allowed Biomes to spawn in")
    @Config.Comment("Allowed Biomes for the Hierophant Temple to spawn in")
    @Config.RequiresMcRestart
    public static String[] biome_allowed = {
            "minecraft:ocean",
            "minecraft:deep_ocean"
    };

    @Config.Name("Hierophant Temple Spawn Height")
    @Config.Comment("Change the spawn height of the Hierophant Temple")
    @Config.RequiresMcRestart
    public static double temple_spawn_height = 25;
}
