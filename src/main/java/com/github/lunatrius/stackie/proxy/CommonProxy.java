package com.github.lunatrius.stackie.proxy;

import com.github.lunatrius.stackie.command.StackieCommand;
import com.github.lunatrius.stackie.handler.Ticker;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

public abstract class CommonProxy {
	private Ticker ticker = null;

	public abstract void setConfigEntryClasses();

	public void registerEvents() {
		this.ticker = new Ticker();
		FMLCommonHandler.instance().bus().register(this.ticker);
	}

	public void serverStarting(FMLServerStartingEvent event) {
		this.ticker.setServer(event.getServer());
		event.registerServerCommand(new StackieCommand());
	}

	public void serverStopping(FMLServerStoppingEvent event) {
		this.ticker.setServer(null);
	}
}
