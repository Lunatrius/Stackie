package com.github.lunatrius.stackie.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public abstract class StackingHandler {
    public static final int MAXIMUM_STACKSIZE = 127; // vanilla stores the stack size in a byte...
    public static final int MAXIMUM_EXPERIENCE = 1024;

    private static final Set<Class<?>> clazzBlacklist = new HashSet<Class<?>>();

    protected enum EntityType {
        ITEM(EntityItem.class),
        EXPERIENCEORB(EntityXPOrb.class),
        OTHER(null);

        public final Class<? extends Entity> clazz;

        private EntityType(Class<? extends Entity> clazz) {
            this.clazz = clazz;
        }
    }

    private double weightL = -1;
    private double weightR = -1;

    protected boolean stackEntities(Entity entityL, Entity entityR) {
        final EntityType typeL = getType(entityL);
        final EntityType typeR = getType(entityR);

        if (typeL == typeR && isEqualPosition(entityL, entityR)) {
            boolean merged;

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

        if (itemStackL.getTagCompound() == null && itemStackR.getTagCompound() == null) {
            return true;
        }

        return itemStackL.getTagCompound() != null && itemStackL.getTagCompound().equals(itemStackR.getTagCompound());
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

    protected EntityType getType(Entity entity) {
        if (ConfigurationHandler.stackItems && entity instanceof EntityItem) {
            if (clazzBlacklist.contains(entity.getClass())) {
                return EntityType.OTHER;
            }

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

    private static Class<?> getClassFor(final String className) {
        try {
            return Class.forName(className);
        } catch (final ClassNotFoundException ignored) {
        }

        return null;
    }

    private static void blacklistClass(final String className) {
        final Class<?> clazz = getClassFor(className);
        if (clazz != null) {
            clazzBlacklist.add(clazz);
        }
    }

    static {
        blacklistClass("thaumcraft.common.entities.EntityFollowingItem");
    }
}
