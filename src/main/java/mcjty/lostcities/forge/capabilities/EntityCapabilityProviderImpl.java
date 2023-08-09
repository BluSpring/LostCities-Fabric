package mcjty.lostcities.forge.capabilities;

import net.minecraft.world.entity.Entity;

public interface EntityCapabilityProviderImpl extends ICapabilityProviderImpl<Entity> {
    CapabilityProviderWorkaround<Entity> getWorkaround();
}
