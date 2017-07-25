package com.github.lunatrius.stackie.handler;

import com.github.lunatrius.stackie.reference.Names;
import com.github.lunatrius.stackie.reference.Reference;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigurationHandler {
    public static final ConfigurationHandler INSTANCE = new ConfigurationHandler();

    public static final int STACK_LIMIT_MIN = 100;
    public static final int STACK_LIMIT_MAX = 10000;
    public static final int INTERVAL_MIN = 5;
    public static final int INTERVAL_MAX = 20 * 60;
    public static final double DISTANCE_MIN = 0.01;
    public static final double DISTANCE_MAX = 10.0;
    public static final int MAXIMUM_STACK_SIZE_MIN = 64;
    public static final int MAXIMUM_STACK_SIZE_MAX = 127; // vanilla stores the stack size in a byte...
    public static final int MAXIMUM_EXPERIENCE_MIN = 32;
    public static final int MAXIMUM_EXPERIENCE_MAX = 16384;

    public static final int STACK_LIMIT_DEFAULT = 2000;
    public static final int INTERVAL_DEFAULT = 20;
    public static final double DISTANCE_DEFAULT = 0.75;
    public static final boolean STACK_ITEMS_DEFAULT = false;
    public static final boolean STACK_EXPERIENCE_DEFAULT = true;
    public static final String[] STACK_SIZES_DEFAULT = new String[] {
            Item.REGISTRY.getNameForObject(Items.MINECART) + Names.Config.STACK_SIZE_DELIMITER + 4,
            Item.REGISTRY.getNameForObject(Items.SADDLE) + Names.Config.STACK_SIZE_DELIMITER + 8,
            Item.REGISTRY.getNameForObject(Items.IRON_DOOR) + Names.Config.STACK_SIZE_DELIMITER + 64,
            Item.REGISTRY.getNameForObject(Items.BOAT) + Names.Config.STACK_SIZE_DELIMITER + 4,
            Item.REGISTRY.getNameForObject(Items.CHEST_MINECART) + Names.Config.STACK_SIZE_DELIMITER + 4,
            Item.REGISTRY.getNameForObject(Items.FURNACE_MINECART) + Names.Config.STACK_SIZE_DELIMITER + 4,
            Item.REGISTRY.getNameForObject(Items.TNT_MINECART) + Names.Config.STACK_SIZE_DELIMITER + 4,
            Item.REGISTRY.getNameForObject(Items.HOPPER_MINECART) + Names.Config.STACK_SIZE_DELIMITER + 4,
            Item.REGISTRY.getNameForObject(Items.COMMAND_BLOCK_MINECART) + Names.Config.STACK_SIZE_DELIMITER + 4
    };
    public static final int MAXIMUM_STACK_SIZE_DEFAULT = 64;
    public static final int MAXIMUM_EXPERIENCE_DEFAULT = 1024;

    @Config(modid = Reference.MODID, category = Names.Config.Category.GENERAL)
    public static class General {
        @RangeInt(min = STACK_LIMIT_MIN, max = STACK_LIMIT_MAX)
        @Comment(Names.Config.STACK_LIMIT_DESC)
        public static int stackLimit = STACK_LIMIT_DEFAULT;

        @RangeInt(min = INTERVAL_MIN, max = INTERVAL_MAX)
        @Comment(Names.Config.INTERVAL_DESC)
        public static int interval = INTERVAL_DEFAULT;

        @RangeDouble(min = DISTANCE_MIN, max = DISTANCE_MAX)
        @Comment(Names.Config.DISTANCE_DESC)
        public static double distance = DISTANCE_DEFAULT;

        @Comment(Names.Config.STACK_ITEMS_DESC)
        public static boolean stackItems = STACK_ITEMS_DEFAULT;

        @Comment(Names.Config.STACK_EXPERIENCE_DESC)
        public static boolean stackExperience = STACK_EXPERIENCE_DEFAULT;

        // TODO: pattern "[A-Za-z0-9_:]+-\\d+"
        @Comment(Names.Config.STACK_SIZES_DESC)
        public static String[] stackSizes = STACK_SIZES_DEFAULT;

        @RangeInt(min = MAXIMUM_STACK_SIZE_MIN, max = MAXIMUM_STACK_SIZE_MAX)
        @Comment(Names.Config.MAXIMUM_STACK_SIZE_DESC)
        public static int maximumStackSize = MAXIMUM_STACK_SIZE_DEFAULT;

        @RangeInt(min = MAXIMUM_EXPERIENCE_MIN, max = MAXIMUM_EXPERIENCE_MAX)
        @Comment(Names.Config.MAXIMUM_EXPERIENCE_DESC)
        public static int maximumExperience = MAXIMUM_EXPERIENCE_DEFAULT;
    }

    public static void setStackLimit(final int num) {
        General.stackLimit = num < STACK_LIMIT_MIN ? STACK_LIMIT_MIN : (num > STACK_LIMIT_MAX ? STACK_LIMIT_MAX : num);
    }

    public static void setInterval(final int num) {
        General.interval = num < INTERVAL_MIN ? INTERVAL_MIN : (num > INTERVAL_MAX ? INTERVAL_MAX : num);
    }

    public static void setDistance(final double num) {
        General.distance = num < DISTANCE_MIN ? DISTANCE_MIN : (num > DISTANCE_MAX ? DISTANCE_MAX : num);
    }

    public static void save() {
        ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(final ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equalsIgnoreCase(Reference.MODID)) {
            ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
        }
    }
}
