package com.github.lunatrius.stackie.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;

public class StackingHandler {
    protected boolean stackItems(final EntityItem entityItemL, final EntityItem entityItemR) {
        if (!areEntityItemsEqual(entityItemL, entityItemR)) {
            return false;
        }

        final ItemStack itemStackL = entityItemL.getEntityItem();
        final ItemStack itemStackR = entityItemR.getEntityItem();

        if (itemStackL.getCount() == ConfigurationHandler.maximumStackSize || itemStackR.getCount() == ConfigurationHandler.maximumStackSize) {
            return false;
        }

        final int itemsIn = Math.min(ConfigurationHandler.maximumStackSize - itemStackL.getCount(), itemStackR.getCount());
        itemStackL.setCount(itemStackL.getCount() + itemsIn);
        itemStackR.setCount(itemStackR.getCount() - itemsIn);

        entityItemL.setEntityItemStack(itemStackL);
        entityItemR.setEntityItemStack(itemStackR);

        entityItemL.age = Math.min(entityItemL.age, entityItemR.age);

        return itemStackR.getCount() <= 0;
    }

    /**
     * @see net.minecraft.entity.item.EntityItem#combineItems
     */
    private boolean areEntityItemsEqual(final EntityItem entityItemL, final EntityItem entityItemR) {
        if (entityItemL == entityItemR) {
            return false;
        }

        if (entityItemL.delayBeforeCanPickup == 32767 || entityItemR.delayBeforeCanPickup == 32767) {
            return false;
        }

        if (entityItemL.age == -32768 || entityItemR.age == -32768) {
            return false;
        }

        final ItemStack itemStackL = entityItemL.getEntityItem();
        final ItemStack itemStackR = entityItemR.getEntityItem();

        if (itemStackL == null || itemStackR == null) {
            return false;
        }

        if (!itemStackL.isStackable()) {
            return false;
        }

        if (itemStackL.getCount() <= 0 || itemStackR.getCount() <= 0) {
            return false;
        }

        if (itemStackL.getItem() == null) {
            return false;
        }

        if (itemStackL.getItem() != itemStackR.getItem()) {
            return false;
        }

        if (itemStackL.getItem().getHasSubtypes() && itemStackL.getMetadata() != itemStackR.getMetadata()) {
            return false;
        }

        if (itemStackL.hasTagCompound() != itemStackR.hasTagCompound()) {
            return false;
        }

        if (itemStackL.hasTagCompound() && !itemStackL.getTagCompound().equals(itemStackR.getTagCompound())) {
            return false;
        }

        return true;
    }

    protected boolean stackExperience(final EntityXPOrb entityExpOrbL, final EntityXPOrb entityExpOrbR) {
        final int experienceIn = Math.min(ConfigurationHandler.maximumExperience - entityExpOrbL.xpValue, entityExpOrbR.xpValue);
        entityExpOrbL.xpValue += experienceIn;
        entityExpOrbR.xpValue -= experienceIn;

        entityExpOrbL.xpOrbAge = Math.min(entityExpOrbL.xpOrbAge, entityExpOrbR.xpOrbAge);

        return entityExpOrbR.xpValue <= 0;
    }

    protected boolean areEntitiesClose(final Entity a, final Entity b) {
        return isEqual(a.posX, b.posX) && isEqual(a.posY, b.posY) && isEqual(a.posZ, b.posZ);
    }

    private boolean isEqual(final double a, final double b) {
        return Math.abs(a - b) < ConfigurationHandler.distance;
    }
}
