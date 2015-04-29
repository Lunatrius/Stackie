package com.github.lunatrius.stackie.handler;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
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
        final List<EntityItem> entities = getNearbyEntities(event.world, entity);

        for (final EntityItem entityR : entities) {
            if (entityR.isDead) {
                continue;
            }

            // FIX: vanilla portal duping...
            if (entityR.age < 20) {
                continue;
            }

            final boolean merged = stackItems(entity, entityR);
            if (merged) {
                entityR.setDead();
            }
        }
    }

    private void stackExperience(final EntityJoinWorldEvent event, final EntityXPOrb entity) {
        final List<EntityXPOrb> entities = getNearbyEntities(event.world, entity);

        for (final EntityXPOrb entityR : entities) {
            if (entityR.isDead) {
                continue;
            }

            // FIX: vanilla portal duping...
            if (entityR.xpOrbAge < 20) {
                continue;
            }

            final boolean merged = stackExperience(entity, entityR);
            if (merged) {
                entityR.setDead();
            }
        }
    }

    private <T extends Entity> List<T> getNearbyEntities(final World world, T entity) {
        final Class<?> clazz = entity.getClass();
        final IEntitySelector filter = new IEntitySelector() {
            @Override
            public boolean isEntityApplicable(Entity input) {
                return clazz.equals(input.getClass());
            }
        };

        final double distance = ConfigurationHandler.distance;
        final AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(entity.posX - distance, entity.posY - distance, entity.posZ - distance, entity.posX + distance, entity.posY + distance, entity.posZ + distance);
        return world.selectEntitiesWithinAABB(clazz, boundingBox, filter);
    }
}
