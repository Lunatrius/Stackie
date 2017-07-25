package com.github.lunatrius.stackie.command;

import com.github.lunatrius.stackie.handler.ConfigurationHandler;
import com.github.lunatrius.stackie.reference.Names;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class StackieCommand extends CommandBase {
    @Override
    public String getName() {
        return Names.Command.NAME;
    }

    @Override
    public String getUsage(final ICommandSender sender) {
        return Names.Command.Message.USAGE;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, final @Nullable BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, Names.Command.STACK_LIMIT, Names.Command.INTERVAL, Names.Command.DISTANCE, Names.Command.STACK_ITEMS, Names.Command.STACK_EXPERIENCE);
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase(Names.Command.STACK_ITEMS) || args[0].equalsIgnoreCase(Names.Command.STACK_EXPERIENCE)) {
                return getListOfStringsMatchingLastWord(args, Names.Command.TRUE, Names.Command.FALSE);
            }
        }

        return Collections.emptyList();
    }

    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase(Names.Command.STACK_LIMIT)) {
                ConfigurationHandler.setStackLimit(Integer.parseInt(args[1]));
                sender.sendMessage(new TextComponentTranslation(Names.Command.Message.STACK_LIMIT, ConfigurationHandler.General.stackLimit));
                ConfigurationHandler.save();
                return;
            } else if (args[0].equalsIgnoreCase(Names.Command.INTERVAL)) {
                ConfigurationHandler.setInterval(Integer.parseInt(args[1]));
                sender.sendMessage(new TextComponentTranslation(Names.Command.Message.INTERVAL, ConfigurationHandler.General.interval, ConfigurationHandler.General.interval / 20.0));
                ConfigurationHandler.save();
                return;
            } else if (args[0].equalsIgnoreCase(Names.Command.DISTANCE)) {
                ConfigurationHandler.setDistance(Double.parseDouble(args[1]));
                sender.sendMessage(new TextComponentTranslation(Names.Command.Message.DISTANCE, ConfigurationHandler.General.distance));
                ConfigurationHandler.save();
                return;
            } else if (args[0].equalsIgnoreCase(Names.Command.STACK_ITEMS)) {
                ConfigurationHandler.General.stackItems = Boolean.parseBoolean(args[1].toLowerCase(Locale.ENGLISH));
                sender.sendMessage(new TextComponentTranslation(Names.Command.Message.STACK_ITEMS, ConfigurationHandler.General.stackItems));
                ConfigurationHandler.save();
                return;
            } else if (args[0].equalsIgnoreCase(Names.Command.STACK_EXPERIENCE)) {
                ConfigurationHandler.General.stackExperience = Boolean.parseBoolean(args[1].toLowerCase(Locale.ENGLISH));
                sender.sendMessage(new TextComponentTranslation(Names.Command.Message.STACK_EXPERIENCE, ConfigurationHandler.General.stackExperience));
                ConfigurationHandler.save();
                return;
            }
        }

        throw new WrongUsageException(getUsage(sender));
    }
}

