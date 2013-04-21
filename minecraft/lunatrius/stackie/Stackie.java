package lunatrius.stackie;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import lunatrius.stackie.util.Config;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.Configuration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Mod(modid = "Stackie")
public class Stackie {
	@Instance("Stackie")
	public static Stackie instance;

	// loaded from config
	public int interval = 20;
	public float distance = 0.75f;

	private Field xpValue = null;
	private MinecraftServer server = null;
	private int ticks = -1;

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());

		config.load();
		this.interval = Config.getInt(config, Configuration.CATEGORY_GENERAL, "interval", this.interval, 5, 500, "Amount of ticks (20 ticks => 1 second) that will pass between each stacking attempt.");
		this.distance = (float) Config.getDouble(config, Configuration.CATEGORY_GENERAL, "distance", this.distance, 0.01, 10, "Maximum distance between items that can be still stacked (relative to block size).");
		config.save();

		try {
			this.xpValue = ReflectionHelper.findField(EntityXPOrb.class, "e", "field_70530_e", "xpValue");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Init
	public void init(FMLInitializationEvent event) {
		// register a new ticker with normal server ticks
		TickRegistry.registerTickHandler(new Ticker(EnumSet.of(TickType.SERVER)), Side.SERVER);
	}

	public boolean onTick(TickType tickType, boolean start) {
		// skip starting ticks
		if (start) {
			return true;
		}

		// if the server isn't set try to set it
		if (this.server == null) {
			this.server = MinecraftServer.getServer();
		}

		// decrease the amount of ticks left; if there are less than 0 ticks left and the world is local stack the entities
		if (--this.ticks < 0 && this.server != null && this.server.worldServers != null) {
			// stack entities
			stackEntities(this.server.worldServers);

			// set a new timeout
			this.ticks = this.interval;
		}
		return true;
	}

	@SuppressWarnings("null")
	public void stackEntities(WorldServer[] theWorldServer) {
		// loop through all worlds
		for (WorldServer world : theWorldServer) {
			// create a new array list
			List<Entity> entityList = new ArrayList<Entity>();

			// save all valid entities to the the previous list
			for (int i = 0; i < world.loadedEntityList.size(); i++) {
				if (getType((Entity) world.loadedEntityList.get(i)) != -1) {
					entityList.add((Entity) world.loadedEntityList.get(i));
				}
			}

			int mcType = -1;
			Entity mcEntity = null;
			EntityItem mcEntityItem = null;
			ItemStack mcItemStack = null;
			EntityXPOrb mcEntityXPOrb = null;

			int localType = -1;
			Entity localEntity = null;
			EntityItem localEntityItem = null;
			ItemStack localItemStack = null;
			EntityXPOrb localEntityXPOrb = null;

			try {
				for (int i = 0; i < entityList.size() - 1; i++) {
					// if the entity is dead skip it
					mcEntity = entityList.get(i);
					if (mcEntity.isDead) {
						continue;
					}

					// get entity's type
					mcType = getType(mcEntity);

					switch (mcType) {
					// EntityItem
					case 0:
						mcEntityItem = (EntityItem) mcEntity;
						mcItemStack = mcEntityItem.getEntityItem();

						// if the entity is not stackable, is at the maximum stack limit or if it's at 0 skip it
						if (mcItemStack == null || !mcItemStack.isStackable() || mcItemStack.stackSize >= mcItemStack.getMaxStackSize() || mcItemStack.stackSize <= 0) {
							continue;
						}
						break;

					// EntityXPOrb
					case 1:
						mcEntityXPOrb = (EntityXPOrb) mcEntity;
						break;
					}

					for (int j = i + 1; j < entityList.size(); j++) {
						// if the entity is dead skip it
						localEntity = entityList.get(j);
						if (localEntity.isDead) {
							continue;
						}

						// get entity's type
						localType = getType(localEntity);

						// entity types match
						if (mcType == localType) {
							switch (mcType) {
							// EntityItem
							case 0:
								localEntityItem = (EntityItem) localEntity;
								localItemStack = localEntityItem.getEntityItem();

								// if item ID aren't equal, items have a tag compound, position differs or the damage isn't equal skip it
								if (localItemStack == null) {
									continue;
								} else if (mcItemStack.itemID != localItemStack.itemID) {
									continue;
								} else if (mcItemStack.stackTagCompound != null || localItemStack.stackTagCompound != null) {
									continue;
								} else if (!isEqualPosition(mcEntityItem, localEntityItem)) {
									continue;
								} else if (mcItemStack.getItemDamage() != localItemStack.getItemDamage()) {
									continue;
								}

								// move the items from one stack to the other
								int itemsIn = Math.min(mcItemStack.getMaxStackSize() - mcItemStack.stackSize, localItemStack.stackSize);
								mcItemStack.stackSize += itemsIn;
								localItemStack.stackSize -= itemsIn;

								// set the new item stacks
								mcEntityItem.setEntityItemStack(mcItemStack);
								localEntityItem.setEntityItemStack(localItemStack);

								// the new stack's age is the lowest age of both stacks
								mcEntityItem.age = Math.min(mcEntityItem.age, localEntityItem.age);

								// if the stack size is bellow or equal to 0 the entity is dead
								if (localItemStack.stackSize <= 0) {
									localEntityItem.setDead();

									// set the new position to the average of the merged entities
									mcEntityItem.setPosition((mcEntityItem.posX + localEntityItem.posX) / 2, (mcEntityItem.posY + localEntityItem.posY) / 2, (mcEntityItem.posZ + localEntityItem.posZ) / 2);
								}
								break;

							// EntityXPOrb
							case 1:
								localEntityXPOrb = (EntityXPOrb) localEntity;

								// if positions differ skip it
								if (!isEqualPosition(mcEntityXPOrb, localEntityXPOrb)) {
									continue;
								}

								try {
									// set the new experience values
									this.xpValue.setInt(mcEntityXPOrb, mcEntityXPOrb.getXpValue() + localEntityXPOrb.getXpValue());
									this.xpValue.setInt(localEntityXPOrb, 0);

									// the new orb's age is the lowest age of both orbs
									mcEntityXPOrb.xpOrbAge = Math.min(mcEntityXPOrb.xpOrbAge, localEntityXPOrb.xpOrbAge);

									// set the new position to the average of the merged entities
									mcEntityXPOrb.setPosition((mcEntityXPOrb.posX + localEntityXPOrb.posX) / 2, (mcEntityXPOrb.posY + localEntityXPOrb.posY) / 2, (mcEntityXPOrb.posZ + localEntityXPOrb.posZ) / 2);

									// the entity is dead
									localEntityXPOrb.setDead();
								} catch (Exception ex) {
									ex.printStackTrace();
								}
								break;
							}
						}
					}
				}
			} catch (Exception e) {
			}
		}
	}

	private int getType(Entity entity) {
		if (entity instanceof EntityItem) {
			return 0;
		} else if (entity instanceof EntityXPOrb && this.xpValue != null) {
			return 1;
		}
		return -1;
	}

	private boolean isEqualPosition(Entity a, Entity b) {
		return isEqual(a.posX, b.posX) && isEqual(a.posY, b.posY) && isEqual(a.posZ, b.posZ);
	}

	private boolean isEqual(double a, double b) {
		return isEqual(a, b, this.distance);
	}

	private boolean isEqual(double a, double b, double epsilon) {
		return Math.abs(a - b) < epsilon;
	}
}
