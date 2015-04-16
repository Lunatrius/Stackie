package com.github.lunatrius.stackie.handler;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.util.List;

public class StackingHandlerJoin extends StackingHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.isCanceled()) {
            return;
        }

        if (event.world == null || event.world.isRemote) {
            return;
        }

        final Entity entity = event.entity;
        if (entity.isDead) {
            return;
        }

        final Class<? extends Entity> clazz = entity.getClass();

        if (EntityItem.class.equals(clazz)) {
            stackItems(event, (EntityItem) entity);
        } else if (EntityXPOrb.class.equals(clazz)) {
            stackExperience(event, (EntityXPOrb) entity);
        }
    }

    private void stackItems(final EntityJoinWorldEvent event, final EntityItem entity) {
        final double distance = ConfigurationHandler.distance;
        final AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(entity.posX - distance, entity.posY - distance, entity.posZ - distance, entity.posX + distance, entity.posY + distance, entity.posZ + distance);
        final List<EntityItem> entities = event.world.getEntitiesWithinAABB(EntityItem.class, boundingBox);

        for (final EntityItem entityR : entities) {
            if (entityR.isDead) {
                continue;
            }

            if (!EntityItem.class.equals(entityR.getClass())) {
                continue;
            }

            final boolean merged = stackItems(entity, entityR);
            if (merged) {
                entityR.setDead();
            }
        }
    }

    private void stackExperience(final EntityJoinWorldEvent event, final EntityXPOrb entity) {
        final double distance = ConfigurationHandler.distance;
        final AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(entity.posX - distance, entity.posY - distance, entity.posZ - distance, entity.posX + distance, entity.posY + distance, entity.posZ + distance);
        final List<EntityXPOrb> entities = event.world.getEntitiesWithinAABB(EntityXPOrb.class, boundingBox);

        for (final EntityXPOrb entityR : entities) {
            if (entityR.isDead) {
                continue;
            }

            if (!EntityXPOrb.class.equals(entityR.getClass())) {
                continue;
            }

            final boolean merged = stackExperience(entity, entityR);
            if (merged) {
                entityR.setDead();
            }
        }
    }
}
