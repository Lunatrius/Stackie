package com.github.lunatrius.stackie.handler;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.util.List;

public class SpawnHandler extends StackingHandler {
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

        final EntityType type = getType(entity);
        if (type.clazz == null) {
            return;
        }

        final double distance = ConfigurationHandler.distance;
        final AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(entity.posX - distance, entity.posY - distance, entity.posZ - distance, entity.posX + distance, entity.posY + distance, entity.posZ + distance);
        final List<Entity> entities = event.world.getEntitiesWithinAABB(type.clazz, boundingBox);

        for (Entity ent : entities) {
            if (ent.isDead) {
                continue;
            }

            event.setCanceled(stackEntities(ent, entity));
        }
    }
}
