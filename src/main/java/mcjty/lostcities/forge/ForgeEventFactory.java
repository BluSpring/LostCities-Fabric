package mcjty.lostcities.forge;

import mcjty.lostcities.forge.capabilities.AttachCapabilitiesEvent;
import mcjty.lostcities.forge.capabilities.CapabilityDispatcher;
import mcjty.lostcities.forge.capabilities.ICapabilityProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedList;

public class ForgeEventFactory {
    @Nullable
    public static <T extends ICapabilityProvider> CapabilityDispatcher gatherCapabilities(Class<? extends T> type, T provider)
    {
        return gatherCapabilities(type, provider, null);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T extends ICapabilityProvider> CapabilityDispatcher gatherCapabilities(Class<? extends T> type, T provider, @Nullable ICapabilityProvider parent)
    {
        return gatherCapabilities(provider, parent);
    }

    @Nullable
    private static CapabilityDispatcher gatherCapabilities(ICapabilityProvider provider, @Nullable ICapabilityProvider parent)
    {
        var capabilities = new HashMap<ResourceLocation, ICapabilityProvider>();
        var listeners = new LinkedList<Runnable>();

        AttachCapabilitiesEvent.EVENT.invoker().onAttachCapability(provider, capabilities, listeners);

        return capabilities.size() > 0 || parent != null ? new CapabilityDispatcher(capabilities, listeners, parent) : null;
    }
}
