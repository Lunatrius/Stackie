package com.github.lunatrius.stackie.proxy;

import com.github.lunatrius.stackie.command.StackieCommand;
import com.github.lunatrius.stackie.handler.ConfigurationHandler;
import com.github.lunatrius.stackie.handler.StackingHandlerTick;
import com.github.lunatrius.stackie.reference.Names;
import com.github.lunatrius.stackie.reference.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.registry.GameData;

public abstract class CommonProxy {
    public void preInit(final FMLPreInitializationEvent event) {
        Reference.logger = event.getModLog();
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());

        FMLInterModComms.sendMessage("LunatriusCore", "checkUpdate", Reference.FORGE);
    }

    public void init(final FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(StackingHandlerTick.INSTANCE);
        // TODO: find a sane way to merge stacks when spawned, remove it otherwise
        // MinecraftForge.EVENT_BUS.register(new StackingHandlerJoin());
    }

    public void postInit(final FMLPostInitializationEvent event) {
        for (final String info : ConfigurationHandler.stackSizes) {
            final String[] parts = info.split(Names.Config.STACK_SIZE_DELIMITER);
            if (parts.length == 2) {
                try {
                    final String uniqueName = parts[0];
                    final int stackSize = MathHelper.clamp(Integer.parseInt(parts[1], 10), 1, 64);

                    final Item item = GameData.getItemRegistry().getObject(new ResourceLocation(uniqueName));
                    if (item != null) {
                        item.setMaxStackSize(stackSize);
                    }
                } catch (final Exception e) {
                    Reference.logger.error("Invalid format?", e);
                }
            }
        }
    }

    public void serverStarting(final FMLServerStartingEvent event) {
        StackingHandlerTick.INSTANCE.setServer(event.getServer());
        event.registerServerCommand(new StackieCommand());
    }

    public void serverStopping(final FMLServerStoppingEvent event) {
        StackingHandlerTick.INSTANCE.setServer(null);
    }
}
