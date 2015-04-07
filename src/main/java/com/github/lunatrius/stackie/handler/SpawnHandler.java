package com.github.lunatrius.stackie.handler;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class SpawnHandler extends StackingHandler {
    public static final SpawnHandler INSTANCE = new SpawnHandler();

    private SpawnHandler() {}

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
        final AxisAlignedBB boundingBox = new AxisAlignedBB(entity.posX - distance, entity.posY - distance, entity.posZ - distance, entity.posX + distance, entity.posY + distance, entity.posZ + distance);
        final List<Entity> entities = event.world.getEntitiesWithinAABB(type.clazz, boundingBox);

        for (Entity ent : entities) {
            if (ent.isDead) {
                continue;
            }

            event.setCanceled(stackEntities(ent, entity));
        }
    }
}
