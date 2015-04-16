package com.github.lunatrius.stackie.proxy;

import com.github.lunatrius.stackie.command.StackieCommand;
import com.github.lunatrius.stackie.handler.StackingHandlerJoin;
import com.github.lunatrius.stackie.handler.StackingHandlerTick;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.common.MinecraftForge;

public abstract class CommonProxy {
    public abstract void setConfigEntryClasses();

    public void registerEvents() {
        FMLCommonHandler.instance().bus().register(StackingHandlerTick.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new StackingHandlerJoin());
    }

    public void serverStarting(FMLServerStartingEvent event) {
        StackingHandlerTick.INSTANCE.setServer(event.getServer());
        event.registerServerCommand(new StackieCommand());
    }

    public void serverStopping(FMLServerStoppingEvent event) {
        StackingHandlerTick.INSTANCE.setServer(null);
    }
}
