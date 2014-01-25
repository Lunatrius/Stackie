package com.github.lunatrius.stackie.config;

import com.github.lunatrius.core.config.Configuration;
import com.github.lunatrius.stackie.lib.Strings;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.init.Items;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class Config extends Configuration {
	public final Property propertyInterval;
	public final Property propertyDistance;
	public final Property propertyStackItems;
	public final Property propertyStackExperience;
	public final Property propertyStackSizes;

	public int interval = 20;
	public double distance = 0.75;
	public boolean stackItems = true;
	public boolean stackExperience = true;
	public String[] stackSizes = new String[] {
			GameData.itemRegistry.func_148750_c(Items.wooden_door) + Strings.CONFIG_STACKSIZE_DELIMITER + 64,
			GameData.itemRegistry.func_148750_c(Items.minecart) + Strings.CONFIG_STACKSIZE_DELIMITER + 4,
			GameData.itemRegistry.func_148750_c(Items.saddle) + Strings.CONFIG_STACKSIZE_DELIMITER + 8,
			GameData.itemRegistry.func_148750_c(Items.iron_door) + Strings.CONFIG_STACKSIZE_DELIMITER + 64,
			GameData.itemRegistry.func_148750_c(Items.boat) + Strings.CONFIG_STACKSIZE_DELIMITER + 4,
			GameData.itemRegistry.func_148750_c(Items.chest_minecart) + Strings.CONFIG_STACKSIZE_DELIMITER + 4,
			GameData.itemRegistry.func_148750_c(Items.furnace_minecart) + Strings.CONFIG_STACKSIZE_DELIMITER + 4,
			GameData.itemRegistry.func_148750_c(Items.potionitem) + Strings.CONFIG_STACKSIZE_DELIMITER + 4,
			GameData.itemRegistry.func_148750_c(Items.tnt_minecart) + Strings.CONFIG_STACKSIZE_DELIMITER + 4,
			GameData.itemRegistry.func_148750_c(Items.hopper_minecart) + Strings.CONFIG_STACKSIZE_DELIMITER + 4,
			GameData.itemRegistry.func_148750_c(Items.command_block_minecart) + Strings.CONFIG_STACKSIZE_DELIMITER + 4
	};

	public Config(File file) {
		super(file);

		this.propertyInterval = get(Strings.CONFIG_CATEGORY, Strings.CONFIG_INTERVAL, this.interval, 5, 20 * 60, Strings.CONFIG_INTERVAL_DESC);
		this.propertyDistance = get(Strings.CONFIG_CATEGORY, Strings.CONFIG_DISTANCE, this.distance, 0.01, 10, Strings.CONFIG_DISTANCE_DESC);
		this.propertyStackItems = get(Strings.CONFIG_CATEGORY, Strings.CONFIG_STACKITEMS, this.stackItems, Strings.CONFIG_STACKITEMS_DESC);
		this.propertyStackExperience = get(Strings.CONFIG_CATEGORY, Strings.CONFIG_STACKEXPERIENCE, this.stackExperience, Strings.CONFIG_STACKEXPERIENCE_DESC);
		this.propertyStackSizes = get(Strings.CONFIG_CATEGORY, Strings.CONFIG_STACKSIZES, this.stackSizes, Strings.CONFIG_STACKSIZES_DESC);

		this.interval = this.propertyInterval.getInt(this.interval);
		this.distance = this.propertyDistance.getDouble(this.distance);
		this.stackItems = this.propertyStackItems.getBoolean(this.stackItems);
		this.stackExperience = this.propertyStackExperience.getBoolean(this.stackExperience);
		this.stackSizes = this.propertyStackSizes.getStringList();
	}
}
