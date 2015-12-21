package com.github.lunatrius.stackie.proxy;

import com.github.lunatrius.stackie.handler.ConfigurationHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(final FMLPreInitializationEvent event) {
        super.preInit(event);

        ConfigurationHandler.propStackLimit.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
        ConfigurationHandler.propInterval.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
        ConfigurationHandler.propDistance.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
        ConfigurationHandler.propMaximumStackSize.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
        ConfigurationHandler.propMaximumExperience.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
    }

    @Override
    public void init(final FMLInitializationEvent event) {
        super.init(event);

        MinecraftForge.EVENT_BUS.register(ConfigurationHandler.INSTANCE);
    }
}
