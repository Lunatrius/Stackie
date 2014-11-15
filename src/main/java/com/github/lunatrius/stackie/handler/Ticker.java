package com.github.lunatrius.stackie.handler;

import com.github.lunatrius.stackie.reference.Reference;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Ticker extends StackingHandler {
    private MinecraftServer server = null;
    private int ticks = -1;

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
}
