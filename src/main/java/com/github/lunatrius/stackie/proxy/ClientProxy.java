package com.github.lunatrius.stackie.proxy;

import com.github.lunatrius.stackie.handler.ConfigurationHandler;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        ConfigurationHandler.propStackLimit.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
        ConfigurationHandler.propInterval.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
        ConfigurationHandler.propDistance.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
    }
}
