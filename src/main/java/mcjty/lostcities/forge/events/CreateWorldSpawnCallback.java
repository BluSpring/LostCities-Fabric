package mcjty.lostcities.forge.events;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;

public interface CreateWorldSpawnCallback {
    Event<CreateWorldSpawn> EVENT = EventFactory.createEventResult();

    @FunctionalInterface
    interface CreateWorldSpawn {
        boolean onCreateWorldSpawn(Level level, ServerLevelData storage);
    }
}
