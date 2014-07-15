package com.github.lunatrius.stackie.handler;


import com.github.lunatrius.stackie.lib.Reference;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.init.Items;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.regex.Pattern;

public class ConfigurationHandler {
	public static final String CATEGORY = "general";
	public static final String INTERVAL = "interval";
	public static final String INTERVAL_DESC = "Amount of ticks (20 ticks => 1 second) that will pass between each stacking attempt.";
	public static final String DISTANCE = "distance";
	public static final String DISTANCE_DESC = "Maximum distance between items/experience orbs that can be still stacked (relative to block size).";
	public static final String STACKITEMS = "stackItems";
	public static final String STACKITEMS_DESC = "Should it stack items?";
	public static final String STACKEXPERIENCE = "stackExperience";
	public static final String STACKEXPERIENCE_DESC = "Should it stack experience orbs?";
	public static final String STACKSIZE_DELIMITER = "-";
	public static final String STACKSIZES = "stackSizes";
	public static final String STACKSIZES_DESC = "A list of uniqueName" + STACKSIZE_DELIMITER + "stackSize mappings. These values will override the default stack sizes.";
	public static final String LANG_PREFIX = Reference.MODID.toLowerCase() + ".config";

	public static final int INTERVAL_MIN = 5;
	public static final int INTERVAL_MAX = 20 * 60;
	public static final double DISTANCE_MIN = 0.01;
	public static final double DISTANCE_MAX = 10.0;

	public static Configuration configuration;

	public static int interval = 20;
	public static double distance = 0.75;
	public static boolean stackItems = true;
	public static boolean stackExperience = true;
	public static String[] stackSizes = new String[] {
			GameData.getItemRegistry().getNameForObject(Items.wooden_door) + STACKSIZE_DELIMITER + 64,
			GameData.getItemRegistry().getNameForObject(Items.minecart) + STACKSIZE_DELIMITER + 4,
			GameData.getItemRegistry().getNameForObject(Items.saddle) + STACKSIZE_DELIMITER + 8,
			GameData.getItemRegistry().getNameForObject(Items.iron_door) + STACKSIZE_DELIMITER + 64,
			GameData.getItemRegistry().getNameForObject(Items.boat) + STACKSIZE_DELIMITER + 4,
			GameData.getItemRegistry().getNameForObject(Items.chest_minecart) + STACKSIZE_DELIMITER + 4,
			GameData.getItemRegistry().getNameForObject(Items.furnace_minecart) + STACKSIZE_DELIMITER + 4,
			GameData.getItemRegistry().getNameForObject(Items.tnt_minecart) + STACKSIZE_DELIMITER + 4,
			GameData.getItemRegistry().getNameForObject(Items.hopper_minecart) + STACKSIZE_DELIMITER + 4,
			GameData.getItemRegistry().getNameForObject(Items.command_block_minecart) + STACKSIZE_DELIMITER + 4
	};

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
		propInterval = configuration.get(CATEGORY, INTERVAL, interval, INTERVAL_DESC, INTERVAL_MIN, INTERVAL_MAX);
		propInterval.setLanguageKey(String.format("%s.%s", LANG_PREFIX, INTERVAL));
		interval = propInterval.getInt(interval);

		propDistance = configuration.get(CATEGORY, DISTANCE, distance, DISTANCE_DESC, DISTANCE_MIN, DISTANCE_MAX);
		propDistance.setLanguageKey(String.format("%s.%s", LANG_PREFIX, DISTANCE));
		distance = propDistance.getDouble(distance);

		propStackItems = configuration.get(CATEGORY, STACKITEMS, stackItems, STACKITEMS_DESC);
		propStackItems.setLanguageKey(String.format("%s.%s", LANG_PREFIX, STACKITEMS));
		stackItems = propStackItems.getBoolean(stackItems);

		propStackExperience = configuration.get(CATEGORY, STACKEXPERIENCE, stackExperience, STACKEXPERIENCE_DESC);
		propStackExperience.setLanguageKey(String.format("%s.%s", LANG_PREFIX, STACKEXPERIENCE));
		stackExperience = propStackExperience.getBoolean(stackExperience);

		propStackSizes = configuration.get(CATEGORY, STACKSIZES, stackSizes, STACKSIZES_DESC);
		propStackSizes.setLanguageKey(String.format("%s.%s", LANG_PREFIX, STACKSIZES));
		propStackSizes.setValidationPattern(Pattern.compile("[A-Za-z0-9_:]+-\\d+"));
		stackSizes = propStackSizes.getStringList();

		if (configuration.hasChanged()) {
			configuration.save();
		}
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

	@SubscribeEvent
	public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equalsIgnoreCase(Reference.MODID)) {
			loadConfiguration();
		}
	}
}
