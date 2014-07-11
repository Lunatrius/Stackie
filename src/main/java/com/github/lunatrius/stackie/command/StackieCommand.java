package com.github.lunatrius.stackie.command;

import com.github.lunatrius.stackie.handler.ConfigurationHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentTranslation;

import java.util.List;

public class StackieCommand extends CommandBase {
	public static final String NAME = "stackie";
	public static final String USAGE = "commands.stackie.usage";
	public static final String INTERVAL = "commands.stackie.interval";
	public static final String DISTANCE = "commands.stackie.distance";
	public static final String STACKITEMS = "commands.stackie.stackitems";
	public static final String STACKEXPERIENCE = "commands.stackie.stackexperience";
	public static final String ARG_INTERVAL = "setinterval";
	public static final String ARG_DISTANCE = "setdistance";
	public static final String ARG_STACKITEMS = "stackitems";
	public static final String ARG_STACKEXPERIENCE = "stackexperience";
	public static final String ARG_TRUE = "true";
	public static final String ARG_FALSE = "false";

	@Override
	public String getCommandName() {
		return NAME;
	}

	@Override
	public String getCommandUsage(ICommandSender commandSender) {
		return USAGE;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender commandSender, String[] args) {
		if (args.length == 1) {
			return getListOfStringsMatchingLastWord(args, ARG_INTERVAL, ARG_DISTANCE, ARG_STACKITEMS, ARG_STACKEXPERIENCE);
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase(ARG_STACKITEMS) || args[0].equalsIgnoreCase(ARG_STACKEXPERIENCE)) {
				return getListOfStringsMatchingLastWord(args, ARG_TRUE, ARG_FALSE);
			}
		}

		return null;
	}

	@Override
	public void processCommand(ICommandSender commandSender, String[] args) {
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase(ARG_INTERVAL)) {
				ConfigurationHandler.setInterval(Integer.parseInt(args[1]));
				commandSender.addChatMessage(new ChatComponentTranslation(INTERVAL, ConfigurationHandler.interval, ConfigurationHandler.interval / 20.0));
				ConfigurationHandler.save();
				return;
			} else if (args[0].equalsIgnoreCase(ARG_DISTANCE)) {
				ConfigurationHandler.setDistance(Double.parseDouble(args[1]));
				commandSender.addChatMessage(new ChatComponentTranslation(DISTANCE, ConfigurationHandler.distance));
				ConfigurationHandler.save();
				return;
			} else if (args[0].equalsIgnoreCase(ARG_STACKITEMS)) {
				ConfigurationHandler.setStackItems(Boolean.parseBoolean(args[1].toLowerCase()));
				commandSender.addChatMessage(new ChatComponentTranslation(STACKITEMS, ConfigurationHandler.stackItems));
				ConfigurationHandler.save();
				return;
			} else if (args[0].equalsIgnoreCase(ARG_STACKEXPERIENCE)) {
				ConfigurationHandler.setStackExperience(Boolean.parseBoolean(args[1].toLowerCase()));
				commandSender.addChatMessage(new ChatComponentTranslation(STACKEXPERIENCE, ConfigurationHandler.stackExperience));
				ConfigurationHandler.save();
				return;
			}
		}

		throw new WrongUsageException(getCommandUsage(commandSender));
	}

	@Override
	public int compareTo(Object obj) {
		return super.compareTo(obj);
	}
}

