package com.github.lunatrius.stackie.command;

import com.github.lunatrius.stackie.handler.ConfigurationHandler;
import com.github.lunatrius.stackie.reference.Names;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentTranslation;

import java.util.List;

public class StackieCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return Names.Command.NAME;
    }

    @Override
    public String getCommandUsage(ICommandSender commandSender) {
        return Names.Command.Message.USAGE;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender commandSender, String[] args) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, Names.Command.STACK_LIMIT, Names.Command.INTERVAL, Names.Command.DISTANCE, Names.Command.STACK_ITEMS, Names.Command.STACK_EXPERIENCE);
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase(Names.Command.STACK_ITEMS) || args[0].equalsIgnoreCase(Names.Command.STACK_EXPERIENCE)) {
                return getListOfStringsMatchingLastWord(args, Names.Command.TRUE, Names.Command.FALSE);
            }
        }

        return null;
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase(Names.Command.STACK_LIMIT)) {
                ConfigurationHandler.setStackLimit(Integer.parseInt(args[1]));
                commandSender.addChatMessage(new ChatComponentTranslation(Names.Command.Message.STACK_LIMIT, ConfigurationHandler.stackLimit));
                ConfigurationHandler.save();
                return;
            } else if (args[0].equalsIgnoreCase(Names.Command.INTERVAL)) {
                ConfigurationHandler.setInterval(Integer.parseInt(args[1]));
                commandSender.addChatMessage(new ChatComponentTranslation(Names.Command.Message.INTERVAL, ConfigurationHandler.interval, ConfigurationHandler.interval / 20.0));
                ConfigurationHandler.save();
                return;
            } else if (args[0].equalsIgnoreCase(Names.Command.DISTANCE)) {
                ConfigurationHandler.setDistance(Double.parseDouble(args[1]));
                commandSender.addChatMessage(new ChatComponentTranslation(Names.Command.Message.DISTANCE, ConfigurationHandler.distance));
                ConfigurationHandler.save();
                return;
            } else if (args[0].equalsIgnoreCase(Names.Command.STACK_ITEMS)) {
                ConfigurationHandler.setStackItems(Boolean.parseBoolean(args[1].toLowerCase()));
                commandSender.addChatMessage(new ChatComponentTranslation(Names.Command.Message.STACK_ITEMS, ConfigurationHandler.stackItems));
                ConfigurationHandler.save();
                return;
            } else if (args[0].equalsIgnoreCase(Names.Command.STACK_EXPERIENCE)) {
                ConfigurationHandler.setStackExperience(Boolean.parseBoolean(args[1].toLowerCase()));
                commandSender.addChatMessage(new ChatComponentTranslation(Names.Command.Message.STACK_EXPERIENCE, ConfigurationHandler.stackExperience));
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

