package com.github.lunatrius.stackie.proxy;

import com.github.lunatrius.stackie.handler.ConfigurationHandler;
import cpw.mods.fml.client.config.GuiConfigEntries;

public class ClientProxy extends CommonProxy {
	@Override
	public void setConfigEntryClasses() {
		ConfigurationHandler.propInterval.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
		ConfigurationHandler.propDistance.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
	}
}
