package com.github.lunatrius.stackie.config;

import com.github.lunatrius.core.config.Configuration;
import com.github.lunatrius.stackie.lib.Strings;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.init.Items;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class Config extends Configuration {
	public static final int INTERVAL_MIN = 5;
	public static final int INTERVAL_MAX = 20 * 60;
	public static final double DISTANCE_MIN = 0.01;
	public static final double DISTANCE_MAX = 10.0;

	private final Property propertyInterval;
	private final Property propertyDistance;
	private final Property propertyStackItems;
	private final Property propertyStackExperience;
	private final Property propertyStackSizes;

	public int interval = 20;
	public double distance = 0.75;
	public boolean stackItems = true;
	public boolean stackExperience = true;
	public String[] stackSizes = new String[] {
			GameData.itemRegistry.getNameForObject(Items.wooden_door) + Strings.CONFIG_STACKSIZE_DELIMITER + 64,
			GameData.itemRegistry.getNameForObject(Items.minecart) + Strings.CONFIG_STACKSIZE_DELIMITER + 4,
			GameData.itemRegistry.getNameForObject(Items.saddle) + Strings.CONFIG_STACKSIZE_DELIMITER + 8,
			GameData.itemRegistry.getNameForObject(Items.iron_door) + Strings.CONFIG_STACKSIZE_DELIMITER + 64,
			GameData.itemRegistry.getNameForObject(Items.boat) + Strings.CONFIG_STACKSIZE_DELIMITER + 4,
			GameData.itemRegistry.getNameForObject(Items.chest_minecart) + Strings.CONFIG_STACKSIZE_DELIMITER + 4,
			GameData.itemRegistry.getNameForObject(Items.furnace_minecart) + Strings.CONFIG_STACKSIZE_DELIMITER + 4,
			GameData.itemRegistry.getNameForObject(Items.potionitem) + Strings.CONFIG_STACKSIZE_DELIMITER + 4,
			GameData.itemRegistry.getNameForObject(Items.tnt_minecart) + Strings.CONFIG_STACKSIZE_DELIMITER + 4,
			GameData.itemRegistry.getNameForObject(Items.hopper_minecart) + Strings.CONFIG_STACKSIZE_DELIMITER + 4,
			GameData.itemRegistry.getNameForObject(Items.command_block_minecart) + Strings.CONFIG_STACKSIZE_DELIMITER + 4
	};

	public Config(File file) {
		super(file);

		this.propertyInterval = get(Strings.CONFIG_CATEGORY, Strings.CONFIG_INTERVAL, this.interval, INTERVAL_MIN, INTERVAL_MAX, Strings.CONFIG_INTERVAL_DESC);
		this.propertyDistance = get(Strings.CONFIG_CATEGORY, Strings.CONFIG_DISTANCE, this.distance, DISTANCE_MIN, DISTANCE_MAX, Strings.CONFIG_DISTANCE_DESC);
		this.propertyStackItems = get(Strings.CONFIG_CATEGORY, Strings.CONFIG_STACKITEMS, this.stackItems, Strings.CONFIG_STACKITEMS_DESC);
		this.propertyStackExperience = get(Strings.CONFIG_CATEGORY, Strings.CONFIG_STACKEXPERIENCE, this.stackExperience, Strings.CONFIG_STACKEXPERIENCE_DESC);
		this.propertyStackSizes = get(Strings.CONFIG_CATEGORY, Strings.CONFIG_STACKSIZES, this.stackSizes, Strings.CONFIG_STACKSIZES_DESC);

		this.interval = this.propertyInterval.getInt(this.interval);
		this.distance = this.propertyDistance.getDouble(this.distance);
		this.stackItems = this.propertyStackItems.getBoolean(this.stackItems);
		this.stackExperience = this.propertyStackExperience.getBoolean(this.stackExperience);
		this.stackSizes = this.propertyStackSizes.getStringList();
	}

	public void setInterval(int interval) {
		this.propertyInterval.set(interval);
		this.interval = this.propertyInterval.getInt(this.interval);
		this.interval = this.interval < INTERVAL_MIN ? INTERVAL_MIN : (this.interval > INTERVAL_MAX ? INTERVAL_MAX : this.interval);
	}

	public void setDistance(double distance) {
		this.propertyDistance.set(distance);
		this.distance = this.propertyDistance.getDouble(this.distance);
		this.distance = this.distance < DISTANCE_MIN ? DISTANCE_MIN : (this.distance > DISTANCE_MAX ? DISTANCE_MAX : this.distance);
	}

	public void setStackItems(boolean stackItems) {
		this.propertyStackItems.set(stackItems);
		this.stackItems = this.propertyStackItems.getBoolean(this.stackItems);
	}

	public void setStackExperience(boolean stackExperience) {
		this.propertyStackExperience.set(stackExperience);
		this.stackExperience = this.propertyStackExperience.getBoolean(this.stackExperience);
	}
}
