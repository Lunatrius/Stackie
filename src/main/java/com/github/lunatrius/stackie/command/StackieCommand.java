package com.github.lunatrius.stackie.command;

import com.github.lunatrius.stackie.config.Config;
import com.github.lunatrius.stackie.lib.Strings;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentTranslation;

import java.util.List;

public class StackieCommand extends CommandBase {
	private final Config config;

	public StackieCommand(Config config) {
		this.config = config;
	}

	@Override
	public String getCommandName() {
		return Strings.COMMANDS_STACKIE_NAME;
	}

	@Override
	public String getCommandUsage(ICommandSender commandSender) {
		return Strings.COMMANDS_STACKIE_USAGE;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender commandSender, String[] args) {
		if (args.length == 1) {
			return getListOfStringsMatchingLastWord(args, Strings.COMMANDS_STACKIE_ARG_INTERVAL, Strings.COMMANDS_STACKIE_ARG_DISTANCE, Strings.COMMANDS_STACKIE_ARG_STACKITEMS, Strings.COMMANDS_STACKIE_ARG_STACKEXPERIENCE);
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase(Strings.COMMANDS_STACKIE_ARG_STACKITEMS) || args[0].equalsIgnoreCase(Strings.COMMANDS_STACKIE_ARG_STACKEXPERIENCE)) {
				return getListOfStringsMatchingLastWord(args, Strings.COMMANDS_STACKIE_ARG_TRUE, Strings.COMMANDS_STACKIE_ARG_FALSE);
			}
		}

		return null;
	}

	@Override
	public void processCommand(ICommandSender commandSender, String[] args) {
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase(Strings.COMMANDS_STACKIE_ARG_INTERVAL)) {
				this.config.setInterval(Integer.parseInt(args[1]));
				commandSender.addChatMessage(new ChatComponentTranslation(Strings.COMMANDS_STACKIE_INTERVAL, this.config.interval, this.config.interval / 20.0));
				this.config.save();
				return;
			} else if (args[0].equalsIgnoreCase(Strings.COMMANDS_STACKIE_ARG_DISTANCE)) {
				this.config.setDistance(Double.parseDouble(args[1]));
				commandSender.addChatMessage(new ChatComponentTranslation(Strings.COMMANDS_STACKIE_DISTANCE, this.config.distance));
				this.config.save();
				return;
			} else if (args[0].equalsIgnoreCase(Strings.COMMANDS_STACKIE_ARG_STACKITEMS)) {
				this.config.setStackItems(Boolean.parseBoolean(args[1].toLowerCase()));
				commandSender.addChatMessage(new ChatComponentTranslation(Strings.COMMANDS_STACKIE_STACKITEMS, this.config.stackItems));
				this.config.save();
				return;
			} else if (args[0].equalsIgnoreCase(Strings.COMMANDS_STACKIE_ARG_STACKEXPERIENCE)) {
				this.config.setStackExperience(Boolean.parseBoolean(args[1].toLowerCase()));
				commandSender.addChatMessage(new ChatComponentTranslation(Strings.COMMANDS_STACKIE_STACKEXPERIENCE, this.config.stackExperience));
				this.config.save();
				return;
			}
		}

		throw new WrongUsageException(getCommandUsage(commandSender));
	}

	@Override
	public int compareTo(Object obj) {
		return 0;
	}
}

