package com.github.lunatrius.stackie.proxy;

import com.github.lunatrius.stackie.command.StackieCommand;
import com.github.lunatrius.stackie.handler.Ticker;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

public class ServerProxy extends CommonProxy {
	private Ticker ticker = null;

	@Override
	public void setConfigEntryClasses() {
	}

	@Override
	public void registerEvents() {
		this.ticker = new Ticker();
		FMLCommonHandler.instance().bus().register(this.ticker);
	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {
		this.ticker.setServer(event.getServer());
		event.registerServerCommand(new StackieCommand());
	}

	@Override
	public void serverStopping(FMLServerStoppingEvent event) {
		this.ticker.setServer(null);
	}
}
