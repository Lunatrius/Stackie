package com.github.lunatrius.stackie.proxy;

import com.github.lunatrius.stackie.handler.ConfigurationHandler;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {
    @Override
    public void setConfigEntryClasses() {
        ConfigurationHandler.propStackLimit.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
        ConfigurationHandler.propInterval.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
        ConfigurationHandler.propDistance.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
    }

    @Override
    public void registerEvents() {
        super.registerEvents();
        FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
    }
}
