package lunatrius.stackie;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class Stackie {
	public static Stackie instance = new Stackie();

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

			int localType = -1;
			Entity localEntity = null;
			Item localItem = null;
			ItemStack localItemStack = null;
			ExperienceOrb localExperienceOrb = null;

			try {
				for (int i = 0; i < entityList.size(); i++) {
					// cache the i-th entity
					mcEntity = entityList.get(i);

					// if the entity is dead skip it
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
						if (mcItemStack == null || mcItemStack.getAmount() >= mcItemStack.getMaxStackSize() || mcItemStack.getAmount() <= 0) {
							continue;
						}
						break;

					// ExperienceOrb
					case 1:
						mcExperienceOrb = (ExperienceOrb) mcEntity;
						break;
					}

					for (int j = i + 1; j < entityList.size(); j++) {
						// cache the j-th entity
						localEntity = entityList.get(j);

						// if the entity is dead skip it
						if (localEntity.isDead()) {
							continue;
						}

						// get entity's type
						localType = getType(localEntity);

						// entity types match
						if (mcType == localType) {
							switch (mcType) {
							// Item
							case 0:
								localItem = (Item) localEntity;
								localItemStack = localItem.getItemStack();

								// if item ID aren't equal, items have a tag compound, position differs or the damage isn't equal skip it
								if (localItemStack == null) {
									continue;
								} else if (mcItemStack.getTypeId() != localItemStack.getTypeId()) {
									continue;
								} else if (mcItemStack.hasItemMeta() || localItemStack.hasItemMeta()) {
									continue;
								} else if (mcItem.getLocation().distance(localItem.getLocation()) > 0.75) {
									continue;
								} else if (mcItemStack.getDurability() != localItemStack.getDurability()) {
									continue;
								}

								// move the items from one stack to the other
								int itemsIn = Math.min(mcItemStack.getMaxStackSize() - mcItemStack.getAmount(), localItemStack.getAmount());
								mcItemStack.setAmount(mcItemStack.getAmount() + itemsIn);
								localItemStack.setAmount(localItemStack.getAmount() - itemsIn);

								// the new stack's age is the lowest age of both stacks
								mcItem.setTicksLived(Math.max(1, Math.min(mcItem.getTicksLived(), localItem.getTicksLived())));

								// if the stack size is bellow or equal to 0 the second entity is dead
								if (localItemStack.getAmount() <= 0) {
									localItem.remove();
								}
								break;

							// ExperienceOrb
							case 1:
								localExperienceOrb = (ExperienceOrb) localEntity;

								// if positions differ skip it
								if (mcExperienceOrb.getLocation().distance(localExperienceOrb.getLocation()) > 0.75) {
									continue;
								}

								// set the experience values
								mcExperienceOrb.setExperience(mcExperienceOrb.getExperience() + localExperienceOrb.getExperience());
								localExperienceOrb.setExperience(0);

								// the new orb's age is the lowest age of both orbs
								mcExperienceOrb.setTicksLived(Math.max(1, Math.min(mcExperienceOrb.getTicksLived(), localExperienceOrb.getTicksLived())));

								// the second entity is dead
								localExperienceOrb.remove();
								break;
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
		if (entity instanceof Item) {
			return 0;
		} else if (entity instanceof ExperienceOrb) {
			return 1;
		}
		return -1;
	}
}
