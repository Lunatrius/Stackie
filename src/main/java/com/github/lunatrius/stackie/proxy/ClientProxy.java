package com.github.lunatrius.stackie.proxy;

import com.github.lunatrius.stackie.handler.ConfigurationHandler;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {
    @Override
    public void setConfigEntryClasses() {
        ConfigurationHandler.propStackLimit.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
        ConfigurationHandler.propInterval.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
        ConfigurationHandler.propDistance.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
        ConfigurationHandler.propMaximumStackSize.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
        ConfigurationHandler.propMaximumExperience.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
    }

    @Override
    public void registerEvents() {
        super.registerEvents();
        FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
    }
}
