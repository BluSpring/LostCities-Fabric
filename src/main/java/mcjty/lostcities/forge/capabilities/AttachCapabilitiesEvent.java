package mcjty.lostcities.forge.capabilities;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

public interface AttachCapabilitiesEvent {
    Event<AttachCapabilities> EVENT = EventFactory.createEventResult();

    interface AttachCapabilities {
        void onAttachCapability(ICapabilityProvider obj, Map<ResourceLocation, ICapabilityProvider> capabilities, List<Runnable> listeners);
    }
}
