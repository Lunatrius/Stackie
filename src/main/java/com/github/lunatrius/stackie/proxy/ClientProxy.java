package com.github.lunatrius.stackie.proxy;

import com.github.lunatrius.stackie.handler.ConfigurationHandler;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

public class ClientProxy extends CommonProxy {
	@Override
	public void setConfigEntryClasses() {
		ConfigurationHandler.propStackLimit.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
		ConfigurationHandler.propInterval.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
		ConfigurationHandler.propDistance.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
	}

	@Override
	public void registerEvents() {
		FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {
	}

	@Override
	public void serverStopping(FMLServerStoppingEvent event) {
	}
}
