package com.github.lunatrius.stackie.proxy;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

public abstract class CommonProxy {
	public abstract void setConfigEntryClasses();

	public abstract void registerEvents();

	public abstract void serverStarting(FMLServerStartingEvent event);

	public abstract void serverStopping(FMLServerStoppingEvent event);
}
