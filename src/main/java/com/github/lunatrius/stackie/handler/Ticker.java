package com.github.lunatrius.stackie.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;
import java.util.List;

public class Ticker {
	public static final int MAXIMUM_STACKSIZE = 2048;
	public static final int MAXIMUM_EXPERIENCE = 1024;

	private enum EntityType {
		ITEM, EXPERIENCEORB, OTHER
	}

	private MinecraftServer server = null;
	private int ticks = -1;

	@SubscribeEvent
	public void tick(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			return;
		}

		if (--this.ticks < 0) {
			if (this.server != null && this.server.worldServers != null) {
				processWorlds(this.server.worldServers);
			}

			this.ticks = ConfigurationHandler.interval;
		}
	}

	public void setServer(MinecraftServer server) {
		this.server = server;
	}

	private void processWorlds(WorldServer[] worldServers) {
		for (WorldServer world : worldServers) {
			List<Entity> entityList = new ArrayList<Entity>();

			for (int i = 0; i < world.loadedEntityList.size(); i++) {
				if (getType((Entity) world.loadedEntityList.get(i)) != EntityType.OTHER) {
					entityList.add((Entity) world.loadedEntityList.get(i));
				}
			}

			stackEntities(entityList);
		}
	}

	private void stackEntities(List<Entity> entityList) {
		EntityType mcType = EntityType.OTHER;
		Entity mcEntity = null;
		EntityItem mcEntityItem = null;
		ItemStack mcItemStack = null;
		EntityXPOrb mcEntityXPOrb = null;
		double mcWeight = -1;

		EntityType localType = EntityType.OTHER;
		Entity localEntity = null;
		EntityItem localEntityItem = null;
		ItemStack localItemStack = null;
		EntityXPOrb localEntityXPOrb = null;
		double localWeight = -1;

		boolean merged = false;
		double totalWeight = -1;

		try {
			for (int i = 0; i < entityList.size() - 1; i++) {
				mcEntity = entityList.get(i);
				if (mcEntity.isDead) {
					continue;
				}

				mcType = getType(mcEntity);

				switch (mcType) {
				case ITEM:
					mcEntityItem = (EntityItem) mcEntity;
					mcItemStack = mcEntityItem.getEntityItem();

					if (mcItemStack == null || !mcItemStack.isStackable() || mcItemStack.stackSize <= 0) {
						continue;
					}
					break;

				case EXPERIENCEORB:
					mcEntityXPOrb = (EntityXPOrb) mcEntity;
					break;
				}

				for (int j = i + 1; j < entityList.size(); j++) {
					localEntity = entityList.get(j);
					if (localEntity.isDead) {
						continue;
					}

					localType = getType(localEntity);

					if (mcType == localType) {
						if (!isEqualPosition(mcEntity, localEntity)) {
							continue;
						}

						merged = false;

						switch (mcType) {
						case ITEM:
							localEntityItem = (EntityItem) localEntity;
							localItemStack = localEntityItem.getEntityItem();

							if (localItemStack == null) {
								continue;
							} else if (mcItemStack.getItem() != localItemStack.getItem()) {
								continue;
							} else if (mcItemStack.stackTagCompound != null || localItemStack.stackTagCompound != null) {
								continue;
							} else if (mcItemStack.getItemDamage() != localItemStack.getItemDamage()) {
								continue;
							}

							mcWeight = mcItemStack.stackSize;
							localWeight = localItemStack.stackSize;

							// move the items from one stack to the other
							int itemsIn = Math.min(MAXIMUM_STACKSIZE - mcItemStack.stackSize, localItemStack.stackSize);
							mcItemStack.stackSize += itemsIn;
							localItemStack.stackSize -= itemsIn;

							mcEntityItem.setEntityItemStack(mcItemStack);
							localEntityItem.setEntityItemStack(localItemStack);

							mcEntityItem.age = Math.min(mcEntityItem.age, localEntityItem.age);

							if (localItemStack.stackSize <= 0) {
								merged = true;
							}
							break;

						case EXPERIENCEORB:
							localEntityXPOrb = (EntityXPOrb) localEntity;

							try {
								mcWeight = mcEntityXPOrb.getXpValue();
								localWeight = localEntityXPOrb.getXpValue();

								if (mcWeight + localWeight > MAXIMUM_EXPERIENCE) {
									continue;
								}

								// set the new experience values
								mcEntityXPOrb.xpValue += localEntityXPOrb.xpValue;
								localEntityXPOrb.xpValue = 0;

								// the new orb's age is the lowest age of both orbs
								mcEntityXPOrb.xpOrbAge = Math.min(mcEntityXPOrb.xpOrbAge, localEntityXPOrb.xpOrbAge);

								merged = true;
							} catch (Exception ex) {
							}
							break;
						}

						if (merged) {
							localEntity.setDead();

							totalWeight = mcWeight + localWeight;
							mcWeight /= totalWeight;
							localWeight /= totalWeight;

							mcEntity.setPosition(mcEntity.posX * mcWeight + localEntity.posX * localWeight, mcEntity.posY * mcWeight + localEntity.posY * localWeight, mcEntity.posZ * mcWeight + localEntity.posZ * localWeight);

							mcEntity.motionX = mcEntity.motionX * mcWeight + localEntity.motionX * localWeight;
							mcEntity.motionY = mcEntity.motionY * mcWeight + localEntity.motionY * localWeight;
							mcEntity.motionZ = mcEntity.motionZ * mcWeight + localEntity.motionZ * localWeight;
						}
					}
				}
			}
		} catch (Exception e) {
		}
	}

	private EntityType getType(Entity entity) {
		if (ConfigurationHandler.stackItems && entity instanceof EntityItem) {
			return EntityType.ITEM;
		} else if (ConfigurationHandler.stackExperience && entity instanceof EntityXPOrb) {
			return EntityType.EXPERIENCEORB;
		}
		return EntityType.OTHER;
	}

	private boolean isEqualPosition(Entity a, Entity b) {
		return isEqual(a.posX, b.posX) && isEqual(a.posY, b.posY) && isEqual(a.posZ, b.posZ);
	}

	private boolean isEqual(double a, double b) {
		return isEqual(a, b, ConfigurationHandler.distance);
	}

	private boolean isEqual(double a, double b, double epsilon) {
		return Math.abs(a - b) < epsilon;
	}
}
