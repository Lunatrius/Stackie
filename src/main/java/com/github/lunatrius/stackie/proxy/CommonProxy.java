package com.github.lunatrius.stackie.proxy;

import com.github.lunatrius.core.version.VersionChecker;
import com.github.lunatrius.stackie.command.StackieCommand;
import com.github.lunatrius.stackie.handler.ConfigurationHandler;
import com.github.lunatrius.stackie.handler.SpawnHandler;
import com.github.lunatrius.stackie.handler.Ticker;
import com.github.lunatrius.stackie.reference.Names;
import com.github.lunatrius.stackie.reference.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.registry.GameData;

public abstract class CommonProxy {
    private Ticker ticker = null;

    public void preInit(FMLPreInitializationEvent event) {
        Reference.logger = event.getModLog();
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());

        VersionChecker.registerMod(event.getModMetadata(), Reference.FORGE);
    }

    public void init(FMLInitializationEvent event) {
        this.ticker = new Ticker();
        FMLCommonHandler.instance().bus().register(this.ticker);
        MinecraftForge.EVENT_BUS.register(new SpawnHandler());
    }

    public void postInit(FMLPostInitializationEvent event) {
        for (String info : ConfigurationHandler.stackSizes) {
            String[] parts = info.split(Names.Config.STACK_SIZE_DELIMITER);
            if (parts.length == 2) {
                try {
                    String uniqueName = parts[0];
                    int stackSize = MathHelper.clamp_int(Integer.parseInt(parts[1], 10), 1, 64);

                    Item item = GameData.getItemRegistry().getObject(uniqueName);
                    if (item != null) {
                        item.setMaxStackSize(stackSize);
                    }
                } catch (Exception e) {
                    Reference.logger.error("Invalid format?", e);
                }
            }
        }
    }

    public void serverStarting(FMLServerStartingEvent event) {
        this.ticker.setServer(event.getServer());
        event.registerServerCommand(new StackieCommand());
    }

    public void serverStopping(FMLServerStoppingEvent event) {
        this.ticker.setServer(null);
    }
}
