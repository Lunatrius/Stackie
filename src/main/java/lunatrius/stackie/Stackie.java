package lunatrius.stackie;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Stackie {
	public static Stackie instance = new Stackie();

	public int interval = 20;
	public double distance = 0.75;
	public boolean stackItems = false;
	public boolean stackExperience = true;

	private Stackie() {
	}

	@SuppressWarnings("null")
	public void stackEntities(List<World> list) {
		// create a new array list
		List<Entity> entityList = new ArrayList<Entity>();

		// save all valid entities to the the previous list
		for (World world : list) {
			entityList.clear();

			List<Entity> tempList = world.getEntities();
			for (int i = 0; i < tempList.size(); i++) {
				if (getType(tempList.get(i)) != -1) {
					entityList.add(tempList.get(i));
				}
			}

			int mcType = -1;
			Entity mcEntity = null;
			Item mcItem = null;
			ItemStack mcItemStack = null;
			ExperienceOrb mcExperienceOrb = null;
			double mcWeight = -1;

			int localType = -1;
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
					// Item
					case 0:
						mcItem = (Item) mcEntity;
						mcItemStack = mcItem.getItemStack();

						// if the entity is not stackable, is at the maximum stack limit or if it's at 0 skip it
						if (mcItemStack == null || mcItemStack.getMaxStackSize() <= 1 || mcItemStack.getAmount() <= 0) {
							continue;
						}
						break;

					// ExperienceOrb
					case 1:
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
							// Item
							case 0:
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
								int itemsIn = Math.min(2048 - mcItemStack.getAmount(), localItemStack.getAmount());
								mcItemStack.setAmount(mcItemStack.getAmount() + itemsIn);
								localItemStack.setAmount(localItemStack.getAmount() - itemsIn);

								// the new stack's age is the lowest age of both stacks
								mcItem.setTicksLived(Math.max(1, Math.min(mcItem.getTicksLived(), localItem.getTicksLived())));

								// if the stack size is bellow or equal to 0 the entities have merged
								if (localItemStack.getAmount() <= 0) {
									merged = true;
								}
								break;

							// ExperienceOrb
							case 1:
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
				e.printStackTrace();
			}
		}
	}

	private int getType(Entity entity) {
		if (this.stackItems && entity instanceof Item) {
			return 0;
		} else if (this.stackExperience && entity instanceof ExperienceOrb) {
			return 1;
		}
		return -1;
	}
}
