package com.github.lunatrius.stackie.handler;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class StackingHandlerJoin extends StackingHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEntityJoinWorld(final EntityJoinWorldEvent event) {
        if (event.isCanceled()) {
            return;
        }

        final World world = event.getWorld();
        if (world == null || world.isRemote) {
            return;
        }

        final Entity entity = event.getEntity();
        if (entity.isDead) {
            return;
        }

        final Class<? extends Entity> clazz = entity.getClass();

        if (EntityItem.class.equals(clazz)) {
            stackItems(world, (EntityItem) entity);
        } else if (EntityXPOrb.class.equals(clazz)) {
            stackExperience(world, (EntityXPOrb) entity);
        }
    }

    private void stackItems(final World world, final EntityItem entity) {
        final List<EntityItem> entities = getNearbyEntities(world, entity);

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

    private void stackExperience(final World world, final EntityXPOrb entity) {
        final List<EntityXPOrb> entities = getNearbyEntities(world, entity);

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

    private <T extends Entity> List<T> getNearbyEntities(final World world, final T entity) {
        final Class<T> clazz = (Class<T>) entity.getClass();
        final Predicate<T> filter = new Predicate<T>() {
            @Override
            public boolean apply(final T input) {
                return clazz.equals(input.getClass());
            }
        };

        final double distance = ConfigurationHandler.distance;
        final AxisAlignedBB boundingBox = new AxisAlignedBB(entity.posX - distance, entity.posY - distance, entity.posZ - distance, entity.posX + distance, entity.posY + distance, entity.posZ + distance);
        return world.getEntitiesWithinAABB(clazz, boundingBox, filter);
    }
}
