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

	public static final String COMMANDS_STACKIE_NAME = "stackie";
	public static final String COMMANDS_STACKIE_USAGE = "commands.stackie.usage";
	public static final String COMMANDS_STACKIE_INTERVAL = "commands.stackie.interval";
	public static final String COMMANDS_STACKIE_DISTANCE = "commands.stackie.distance";
	public static final String COMMANDS_STACKIE_STACKITEMS = "commands.stackie.stackitems";
	public static final String COMMANDS_STACKIE_STACKEXPERIENCE = "commands.stackie.stackexperience";

	public static final String COMMANDS_STACKIE_ARG_INTERVAL = "setinterval";
	public static final String COMMANDS_STACKIE_ARG_DISTANCE = "setdistance";
	public static final String COMMANDS_STACKIE_ARG_STACKITEMS = "stackitems";
	public static final String COMMANDS_STACKIE_ARG_STACKEXPERIENCE = "stackexperience";
	public static final String COMMANDS_STACKIE_ARG_TRUE = "true";
	public static final String COMMANDS_STACKIE_ARG_FALSE = "false";
}
