package lunatrius.stackie;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Stackie {
	public static final int MAXIMUM_STACKSIZE = 2048;
	public static final Stackie instance = new Stackie();

	private enum EntityType {
		ITEM, EXPERIENCEORB, OTHER
	}

	public int interval = 20;
	public double distance = 0.75;
	public boolean stackItems = false;
	public boolean stackExperience = true;
	public int stackLimit = 2000;

	private Stackie() {
	}

	@SuppressWarnings("null")
	public void processWorlds(List<World> list) {
		// create a new array list
		List<Entity> entityList = new ArrayList<Entity>();

		// save all valid entities to the the previous list
		for (World world : list) {
			entityList.clear();

			List<Entity> tempList = world.getEntities();
			for (Entity entity : tempList) {
				if (getType(entity) != EntityType.OTHER) {
					entityList.add(entity);
				}
			}

			if (entityList.size() < this.stackLimit) {
				stackEntities(entityList);
			}
		}
	}

	private void stackEntities(List<Entity> entityList) {
		EntityType mcType;
		Entity mcEntity = null;
		Item mcItem = null;
		ItemStack mcItemStack = null;
		ExperienceOrb mcExperienceOrb = null;
		double mcWeight = -1;

		EntityType localType;
		Entity localEntity = null;
		Item localItem = null;
		ItemStack localItemStack = null;
		ExperienceOrb localExperienceOrb = null;
		double localWeight = -1;

		boolean merged = false;
		double totalWeight = -1;

		try {
			for (int i = 0; i < entityList.size() - 1; i++) {
				// if the entity is dead skip it
				mcEntity = entityList.get(i);
				if (mcEntity.isDead()) {
					continue;
				}

				// get entity's type
				mcType = getType(mcEntity);

				switch (mcType) {
				case ITEM:
					mcItem = (Item) mcEntity;
					mcItemStack = mcItem.getItemStack();

					// if the entity is not stackable, is at the maximum stack limit or if it's at 0 skip it
					if (mcItemStack == null || mcItemStack.getMaxStackSize() <= 1 || mcItemStack.getAmount() <= 0) {
						continue;
					}
					break;

				case EXPERIENCEORB:
					mcExperienceOrb = (ExperienceOrb) mcEntity;
					break;
				}

				for (int j = i + 1; j < entityList.size(); j++) {
					// if the entity is dead skip it
					localEntity = entityList.get(j);
					if (localEntity.isDead()) {
						continue;
					}

					// get entity's type
					localType = getType(localEntity);

					// entity types match
					if (mcType == localType) {
						// if positions differ skip it
						if (mcEntity.getLocation().distance(localEntity.getLocation()) > this.distance) {
							continue;
						}

						// reset the merged flag
						merged = false;

						switch (mcType) {
						case ITEM:
							localItem = (Item) localEntity;
							localItemStack = localItem.getItemStack();

							// if item ID aren't equal, items have a tag compound, position differs or the damage isn't equal skip it
							if (localItemStack == null) {
								continue;
							} else if (mcItemStack.getType() != localItemStack.getType()) {
								continue;
							} else if (mcItemStack.hasItemMeta() || localItemStack.hasItemMeta()) {
								continue;
							} else if (mcItemStack.getDurability() != localItemStack.getDurability()) {
								continue;
							}

							// move the items from one stack to the other
							int itemsIn = Math.min(MAXIMUM_STACKSIZE - mcItemStack.getAmount(), localItemStack.getAmount());
							mcItemStack.setAmount(mcItemStack.getAmount() + itemsIn);
							localItemStack.setAmount(localItemStack.getAmount() - itemsIn);

							// the new stack's age is the lowest age of both stacks
							mcItem.setTicksLived(Math.max(1, Math.min(mcItem.getTicksLived(), localItem.getTicksLived())));

							// if the stack size is bellow or equal to 0 the entities have merged
							if (localItemStack.getAmount() <= 0) {
								merged = true;
							}
							break;

						case EXPERIENCEORB:
							localExperienceOrb = (ExperienceOrb) localEntity;

							// set the experience values
							mcExperienceOrb.setExperience(mcExperienceOrb.getExperience() + localExperienceOrb.getExperience());
							localExperienceOrb.setExperience(0);

							// the new orb's age is the lowest age of both orbs
							mcExperienceOrb.setTicksLived(Math.max(1, Math.min(mcExperienceOrb.getTicksLived(), localExperienceOrb.getTicksLived())));

							// the entities have been merged
							merged = true;
							break;
						}

						if (merged) {
							// the entity is dead
							localEntity.remove();

							// sum up the weights
							totalWeight = mcWeight + localWeight;

							// set the new weights
							mcWeight /= totalWeight;
							localWeight /= totalWeight;

							// set the new position to the average of the merged entities
							mcEntity.teleport(mcEntity.getLocation().multiply(mcWeight).add(localEntity.getLocation().multiply(localWeight)));

							// set the new velocity to the average of the merged entities
							mcEntity.setVelocity(mcEntity.getVelocity().multiply(mcWeight).add(localEntity.getVelocity().multiply(localWeight)));
						}
					}
				}
			}
		} catch (Exception e) {
		}
	}

	private EntityType getType(Entity entity) {
		if (this.stackItems && entity instanceof Item) {
			return EntityType.ITEM;
		} else if (this.stackExperience && entity instanceof ExperienceOrb) {
			return EntityType.EXPERIENCEORB;
		}
		return EntityType.OTHER;
	}
}
