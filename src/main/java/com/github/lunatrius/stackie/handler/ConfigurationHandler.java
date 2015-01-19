package com.github.lunatrius.stackie.handler;


import com.github.lunatrius.stackie.reference.Names;
import com.github.lunatrius.stackie.reference.Reference;
import net.minecraft.init.Items;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameData;

import java.io.File;
import java.util.regex.Pattern;

public class ConfigurationHandler {
    public static final ConfigurationHandler INSTANCE = new ConfigurationHandler();

    public static final int STACKLIMIT_MIN = 100;
    public static final int STACKLIMIT_MAX = 10000;
    public static final int INTERVAL_MIN = 5;
    public static final int INTERVAL_MAX = 20 * 60;
    public static final double DISTANCE_MIN = 0.01;
    public static final double DISTANCE_MAX = 10.0;

    public static Configuration configuration;

    public static final int STACKLIMIT_DEFAULT = 2000;
    public static final int INTERVAL_DEFAULT = 20;
    public static final double DISTANCE_DEFAULT = 0.75;
    public static final boolean STACKITEMS_DEFAULT = true;
    public static final boolean STACKEXPERIENCE_DEFAULT = true;
    public static final String[] STACKSIZES_DEFAULT = new String[] {
            GameData.getItemRegistry().getNameForObject(Items.minecart) + Names.Config.STACK_SIZE_DELIMITER + 4,
            GameData.getItemRegistry().getNameForObject(Items.saddle) + Names.Config.STACK_SIZE_DELIMITER + 8,
            GameData.getItemRegistry().getNameForObject(Items.iron_door) + Names.Config.STACK_SIZE_DELIMITER + 64,
            GameData.getItemRegistry().getNameForObject(Items.boat) + Names.Config.STACK_SIZE_DELIMITER + 4,
            GameData.getItemRegistry().getNameForObject(Items.chest_minecart) + Names.Config.STACK_SIZE_DELIMITER + 4,
            GameData.getItemRegistry().getNameForObject(Items.furnace_minecart) + Names.Config.STACK_SIZE_DELIMITER + 4,
            GameData.getItemRegistry().getNameForObject(Items.tnt_minecart) + Names.Config.STACK_SIZE_DELIMITER + 4,
            GameData.getItemRegistry().getNameForObject(Items.hopper_minecart) + Names.Config.STACK_SIZE_DELIMITER + 4,
            GameData.getItemRegistry().getNameForObject(Items.command_block_minecart) + Names.Config.STACK_SIZE_DELIMITER + 4
    };

    public static int stackLimit = STACKLIMIT_DEFAULT;
    public static int interval = INTERVAL_DEFAULT;
    public static double distance = DISTANCE_DEFAULT;
    public static boolean stackItems = STACKITEMS_DEFAULT;
    public static boolean stackExperience = STACKEXPERIENCE_DEFAULT;
    public static String[] stackSizes = STACKSIZES_DEFAULT;

    public static Property propStackLimit = null;
    public static Property propInterval = null;
    public static Property propDistance = null;
    public static Property propStackItems = null;
    public static Property propStackExperience = null;
    public static Property propStackSizes = null;

    public static void init(File configFile) {
        if (configuration == null) {
            configuration = new Configuration(configFile);
            loadConfiguration();
        }
    }

    private static void loadConfiguration() {
        propStackLimit = configuration.get(Names.Config.Category.GENERAL, Names.Config.STACKLIMIT, STACKLIMIT_DEFAULT, Names.Config.STACKLIMIT_DESC, STACKLIMIT_MIN, STACKLIMIT_MAX);
        propStackLimit.setLanguageKey(Names.Config.LANG_PREFIX + "." + Names.Config.STACKLIMIT);
        stackLimit = propStackLimit.getInt(STACKLIMIT_DEFAULT);

        propInterval = configuration.get(Names.Config.Category.GENERAL, Names.Config.INTERVAL, INTERVAL_DEFAULT, Names.Config.INTERVAL_DESC, INTERVAL_MIN, INTERVAL_MAX);
        propInterval.setLanguageKey(Names.Config.LANG_PREFIX + "." + Names.Config.INTERVAL);
        interval = propInterval.getInt(INTERVAL_DEFAULT);

        propDistance = configuration.get(Names.Config.Category.GENERAL, Names.Config.DISTANCE, DISTANCE_DEFAULT, Names.Config.DISTANCE_DESC, DISTANCE_MIN, DISTANCE_MAX);
        propDistance.setLanguageKey(Names.Config.LANG_PREFIX + "." + Names.Config.DISTANCE);
        distance = propDistance.getDouble(DISTANCE_DEFAULT);

        propStackItems = configuration.get(Names.Config.Category.GENERAL, Names.Config.STACK_ITEMS, STACKITEMS_DEFAULT, Names.Config.STACK_ITEMS_DESC);
        propStackItems.setLanguageKey(Names.Config.LANG_PREFIX + "." + Names.Config.STACK_ITEMS);
        stackItems = propStackItems.getBoolean(STACKITEMS_DEFAULT);

        propStackExperience = configuration.get(Names.Config.Category.GENERAL, Names.Config.STACK_EXPERIENCE, STACKEXPERIENCE_DEFAULT, Names.Config.STACK_EXPERIENCE_DESC);
        propStackExperience.setLanguageKey(Names.Config.LANG_PREFIX + "." + Names.Config.STACK_EXPERIENCE);
        stackExperience = propStackExperience.getBoolean(STACKEXPERIENCE_DEFAULT);

        propStackSizes = configuration.get(Names.Config.Category.GENERAL, Names.Config.STACK_SIZES, STACKSIZES_DEFAULT, Names.Config.STACK_SIZES_DESC);
        propStackSizes.setLanguageKey(Names.Config.LANG_PREFIX + "." + Names.Config.STACK_SIZES);
        propStackSizes.setValidationPattern(Pattern.compile("[A-Za-z0-9_:]+-\\d+"));
        stackSizes = propStackSizes.getStringList();

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    public static void setStackLimit(int num) {
        propStackLimit.set(num < STACKLIMIT_MIN ? STACKLIMIT_MIN : (num > STACKLIMIT_MAX ? STACKLIMIT_MAX : num));
        stackLimit = propStackLimit.getInt(num);
    }

    public static void setInterval(int num) {
        propInterval.set(num < INTERVAL_MIN ? INTERVAL_MIN : (num > INTERVAL_MAX ? INTERVAL_MAX : num));
        interval = propInterval.getInt(num);
    }

    public static void setDistance(double num) {
        propDistance.set(num < DISTANCE_MIN ? DISTANCE_MIN : (num > DISTANCE_MAX ? DISTANCE_MAX : num));
        distance = propDistance.getDouble(num);
    }

    public static void setStackItems(boolean stack) {
        propStackItems.set(stack);
        stackItems = propStackItems.getBoolean(stack);
    }

    public static void setStackExperience(boolean stack) {
        propStackExperience.set(stack);
        stackExperience = propStackExperience.getBoolean(stack);
    }

    public static void save() {
        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    private ConfigurationHandler() {}

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(Reference.MODID)) {
            loadConfiguration();
        }
    }
}
