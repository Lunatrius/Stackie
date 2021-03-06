package com.github.lunatrius.stackie.reference;

@SuppressWarnings("HardCodedStringLiteral")
public final class Names {
    public static final class Command {
        public static final class Message {
            public static final String USAGE = "commands.stackie.usage";
            public static final String STACK_LIMIT = "commands.stackie.stacklimit";
            public static final String INTERVAL = "commands.stackie.interval";
            public static final String DISTANCE = "commands.stackie.distance";
            public static final String STACK_ITEMS = "commands.stackie.stackitems";
            public static final String STACK_EXPERIENCE = "commands.stackie.stackexperience";
        }

        public static final String NAME = "stackie";
        public static final String STACK_LIMIT = "setstacklimit";
        public static final String INTERVAL = "setinterval";
        public static final String DISTANCE = "setdistance";
        public static final String STACK_ITEMS = "stackitems";
        public static final String STACK_EXPERIENCE = "stackexperience";
        public static final String TRUE = "true";
        public static final String FALSE = "false";
    }

    public static final class Config {
        public static final class Category {
            public static final String GENERAL = "general";
        }

        public static final String STACK_LIMIT_DESC = "If the amount of entities surpasses this number stacking will be disabled (per dimension).";
        public static final String INTERVAL_DESC = "Amount of ticks (20 ticks => 1 second) that will pass between each stacking attempt.";
        public static final String DISTANCE_DESC = "Maximum distance between items/experience orbs that can be still stacked (relative to block size).";
        public static final String STACK_ITEMS_DESC = "Should it stack items?";
        public static final String STACK_EXPERIENCE_DESC = "Should it stack experience orbs?";
        public static final String STACK_SIZE_DELIMITER = "-";
        public static final String STACK_SIZES_DESC = "A list of uniqueName" + STACK_SIZE_DELIMITER + "stackSize mappings. These values will override the default stack sizes.";
        public static final String MAXIMUM_STACK_SIZE_DESC = "Items will be stacked up to this amount.";
        public static final String MAXIMUM_EXPERIENCE_DESC = "Experience will be stacked up to this amount.";
    }
}
