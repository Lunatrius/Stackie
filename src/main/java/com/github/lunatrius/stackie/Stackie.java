package com.github.lunatrius.stackie;

import com.github.lunatrius.core.version.VersionChecker;
import com.github.lunatrius.stackie.command.StackieCommand;
import com.github.lunatrius.stackie.config.Config;
import com.github.lunatrius.stackie.lib.Reference;
import com.github.lunatrius.stackie.lib.Strings;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MODID, name = Reference.NAME)
public class Stackie {
	public static Logger logger = null;
	private Ticker ticker = null;
	private Config config = null;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		VersionChecker.registerMod(event.getModMetadata());

		logger = event.getModLog();

		this.config = new Config(event.getSuggestedConfigurationFile());
		this.config.save();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		this.ticker = new Ticker();
		this.ticker.setConfig(this.config);

		FMLCommonHandler.instance().bus().register(this.ticker);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		for (String info : this.config.stackSizes) {
			String[] parts = info.split(Strings.CONFIG_STACKSIZE_DELIMITER);
			if (parts.length == 2) {
				try {
					String uniqueName = parts[0];
					int stackSize = MathHelper.clamp_int(Integer.parseInt(parts[1], 10), 1, 64);

					Item item = GameData.getItemRegistry().getObject(uniqueName);
					if (item != null) {
						item.setMaxStackSize(stackSize);
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Invalid format?", e);
				}
			}
		}
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		this.ticker.setServer(event.getServer());
		event.registerServerCommand(new StackieCommand(this.config));
	}

	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		this.ticker.setServer(null);
	}
}
