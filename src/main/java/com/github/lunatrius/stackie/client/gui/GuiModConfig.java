package com.github.lunatrius.stackie.client.gui;

import com.github.lunatrius.stackie.handler.ConfigurationHandler;
import com.github.lunatrius.stackie.reference.Names;
import com.github.lunatrius.stackie.reference.Reference;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

public class GuiModConfig extends GuiConfig {
	public GuiModConfig(GuiScreen guiScreen) {
		super(guiScreen, new ConfigElement(ConfigurationHandler.configuration.getCategory(Names.Config.Category.GENERAL)).getChildElements(), Reference.MODID, false, false, GuiConfig.getAbridgedConfigPath(ConfigurationHandler.configuration.toString()));
	}
}