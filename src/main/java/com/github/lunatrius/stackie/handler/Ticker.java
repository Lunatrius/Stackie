package com.github.lunatrius.stackie.handler;

import com.github.lunatrius.stackie.reference.Reference;
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
import java.util.ListIterator;

public class Ticker {
    public static final int MAXIMUM_STACKSIZE = 2048;
    public static final int MAXIMUM_EXPERIENCE = 1024;

    private enum EntityType {
        ITEM, EXPERIENCEORB, OTHER
    }

    private MinecraftServer server = null;
    private int ticks = -1;

    private double weightL = -1;
    private double weightR = -1;

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

            if (entityList.size() >= 2 && entityList.size() <= ConfigurationHandler.stackLimit) {
                try {
                    stackEntities(entityList);
                } catch (Exception e) {
                    Reference.logger.error("Could not stack entities!", e);
                }
            }
        }
    }

    private void stackEntities(List<Entity> entityList) {
        final ListIterator<Entity> iteratorL = entityList.listIterator();
        while (iteratorL.hasNext()) {
            final Entity entityL = iteratorL.next();

            if (entityL.isDead) {
                continue;
            }

            final ListIterator<Entity> iteratorR = entityList.listIterator(iteratorL.nextIndex());
            while (iteratorR.hasNext()) {
                final Entity entityR = iteratorR.next();

                if (entityR.isDead) {
                    continue;
                }

                stackEntities(entityL, entityR);
            }
        }
    }

    private boolean stackEntities(Entity entityL, Entity entityR) {
        final EntityType typeL = getType(entityL);
        final EntityType typeR = getType(entityR);

        if (typeL == typeR && isEqualPosition(entityL, entityR)) {
            boolean merged = false;

            switch (typeL) {
            case ITEM:
                merged = stackItems((EntityItem) entityL, (EntityItem) entityR);
                break;

            case EXPERIENCEORB:
                merged = stackExperience((EntityXPOrb) entityL, (EntityXPOrb) entityR);
                break;

            default:
                return false;
            }

            if (merged) {
                entityR.setDead();

                final double totalWeight = this.weightL + this.weightR;
                this.weightL /= totalWeight;
                this.weightR /= totalWeight;

                final double x = entityL.posX * this.weightL + entityR.posX * this.weightR;
                final double y = entityL.posY * this.weightL + entityR.posY * this.weightR;
                final double z = entityL.posZ * this.weightL + entityR.posZ * this.weightR;
                entityL.setPosition(x, y, z);

                entityL.motionX = entityL.motionX * this.weightL + entityR.motionX * this.weightR;
                entityL.motionY = entityL.motionY * this.weightL + entityR.motionY * this.weightR;
                entityL.motionZ = entityL.motionZ * this.weightL + entityR.motionZ * this.weightR;
            }

            return merged;
        }

        return false;
    }

    private boolean stackItems(EntityItem entityItemL, EntityItem entityItemR) {
        final ItemStack itemStackL = entityItemL.getEntityItem();
        final ItemStack itemStackR = entityItemR.getEntityItem();

        if (!areItemStacksValid(itemStackL, itemStackR)) {
            return false;
        }

        this.weightL = itemStackL.stackSize;
        this.weightR = itemStackR.stackSize;

        final int itemsIn = Math.min(MAXIMUM_STACKSIZE - itemStackL.stackSize, itemStackR.stackSize);
        itemStackL.stackSize += itemsIn;
        itemStackR.stackSize -= itemsIn;

        entityItemL.setEntityItemStack(itemStackL);
        entityItemR.setEntityItemStack(itemStackR);

        entityItemL.age = Math.min(entityItemL.age, entityItemR.age);

        return itemStackR.stackSize <= 0;
    }

    private boolean areItemStacksValid(ItemStack itemStackL, ItemStack itemStackR) {
        if (itemStackL == null || itemStackR == null) {
            return false;
        }

        if (!itemStackL.isStackable()) {
            return false;
        }

        if (itemStackL.stackSize <= 0 || itemStackR.stackSize <= 0) {
            return false;
        }

        if (itemStackL.getItem() != itemStackR.getItem()) {
            return false;
        }

        if (itemStackL.getItemDamage() != itemStackR.getItemDamage()) {
            return false;
        }

        if (itemStackL.stackTagCompound == null && itemStackR.stackTagCompound == null) {
            return true;
        }

        return itemStackL.stackTagCompound != null && itemStackL.stackTagCompound.equals(itemStackR.stackTagCompound);
    }

    private boolean stackExperience(EntityXPOrb entityExpOrbL, EntityXPOrb entityExpOrbR) {
        this.weightL = entityExpOrbL.getXpValue();
        this.weightR = entityExpOrbR.getXpValue();

        final int experienceIn = Math.min(MAXIMUM_EXPERIENCE - entityExpOrbL.xpValue, entityExpOrbR.xpValue);
        entityExpOrbL.xpValue += experienceIn;
        entityExpOrbR.xpValue -= experienceIn;

        entityExpOrbL.xpOrbAge = Math.min(entityExpOrbL.xpOrbAge, entityExpOrbR.xpOrbAge);

        return entityExpOrbR.xpValue <= 0;
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
