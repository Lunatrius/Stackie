package com.github.lunatrius.stackie;

import com.github.lunatrius.core.version.VersionChecker;
import com.github.lunatrius.stackie.handler.ConfigurationHandler;
import com.github.lunatrius.stackie.proxy.CommonProxy;
import com.github.lunatrius.stackie.reference.Names;
import com.github.lunatrius.stackie.reference.Reference;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;

import java.util.Map;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY)
public class Stackie {
    @SidedProxy(serverSide = Reference.PROXY_SERVER, clientSide = Reference.PROXY_CLIENT)
    public static CommonProxy proxy;

    @NetworkCheckHandler
    public boolean checkModList(Map<String, String> versions, Side side) {
        return true;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Reference.logger = event.getModLog();
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        proxy.setConfigEntryClasses();

        VersionChecker.registerMod(event.getModMetadata(), Reference.FORGE);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.registerEvents();
    }

    @EventHandler
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

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        proxy.serverStopping(event);
    }
}
