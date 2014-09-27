package com.github.lunatrius.stackie.reference;

@SuppressWarnings("HardCodedStringLiteral")
public final class Names {
    public static final class Command {
        public static final class Message {
            public static final String USAGE = "commands.stackie.usage";
            public static final String STACKLIMIT = "commands.stackie.stacklimit";
            public static final String INTERVAL = "commands.stackie.interval";
            public static final String DISTANCE = "commands.stackie.distance";
            public static final String STACKITEMS = "commands.stackie.stackitems";
            public static final String STACKEXPERIENCE = "commands.stackie.stackexperience";
        }

        public static final String NAME = "stackie";
        public static final String STACKLIMIT = "setstacklimit";
        public static final String INTERVAL = "setinterval";
        public static final String DISTANCE = "setdistance";
        public static final String STACKITEMS = "stackitems";
        public static final String STACKEXPERIENCE = "stackexperience";
        public static final String TRUE = "true";
        public static final String FALSE = "false";
    }

    public static final class Config {
        public static final class Category {
            public static final String GENERAL = "general";
        }

        public static final String STACKLIMIT = "stackLimit";
        public static final String STACKLIMIT_DESC = "If the amount of entities surpasses this number stacking will be disabled (per dimension).";
        public static final String INTERVAL = "interval";
        public static final String INTERVAL_DESC = "Amount of ticks (20 ticks => 1 second) that will pass between each stacking attempt.";
        public static final String DISTANCE = "distance";
        public static final String DISTANCE_DESC = "Maximum distance between items/experience orbs that can be still stacked (relative to block size).";
        public static final String STACK_ITEMS = "stackItems";
        public static final String STACK_ITEMS_DESC = "Should it stack items?";
        public static final String STACK_EXPERIENCE = "stackExperience";
        public static final String STACK_EXPERIENCE_DESC = "Should it stack experience orbs?";
        public static final String STACK_SIZE_DELIMITER = "-";
        public static final String STACK_SIZES = "stackSizes";
        public static final String STACK_SIZES_DESC = "A list of uniqueName" + STACK_SIZE_DELIMITER + "stackSize mappings. These values will override the default stack sizes.";

        public static final String LANG_PREFIX = Reference.MODID.toLowerCase() + ".config";
    }
}
