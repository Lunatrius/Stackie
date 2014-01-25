package com.github.lunatrius.stackie.lib;

public class Strings {
	public static final String CONFIG_CATEGORY = "general";

	public static final String CONFIG_INTERVAL = "interval";
	public static final String CONFIG_INTERVAL_DESC = "Amount of ticks (20 ticks => 1 second) that will pass between each stacking attempt.";

	public static final String CONFIG_DISTANCE = "distance";
	public static final String CONFIG_DISTANCE_DESC = "Maximum distance between items/experience orbs that can be still stacked (relative to block size).";

	public static final String CONFIG_STACKITEMS = "stackItems";
	public static final String CONFIG_STACKITEMS_DESC = "Should it stack items?";

	public static final String CONFIG_STACKEXPERIENCE = "stackExperience";
	public static final String CONFIG_STACKEXPERIENCE_DESC = "Should it stack experience orbs?";

	public static final String CONFIG_STACKSIZE_DELIMITER = "-";

	public static final String CONFIG_STACKSIZES = "stackSizes";
	public static final String CONFIG_STACKSIZES_DESC = "A list of uniqueName" + CONFIG_STACKSIZE_DELIMITER + "stackSize mappings. These values will override the default stack sizes.";
}
