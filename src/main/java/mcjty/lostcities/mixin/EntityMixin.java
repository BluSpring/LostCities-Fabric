package mcjty.lostcities.mixin;

import io.github.fabricators_of_create.porting_lib.extensions.EntityExtensions;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import mcjty.lostcities.forge.capabilities.*;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Supplier;

@Mixin(Entity.class)
public abstract class EntityMixin implements CapabilityProviderInjection, EntityCapabilityProviderImpl, EntityExtensions {
    @Shadow public Level level;

    private final CapabilityProviderWorkaround<Entity> workaround = new CapabilityProviderWorkaround<>(Entity.class, (Entity) (Object) this);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return workaround.getCapability(cap, side);
    }

    @Override
    public CapabilityProviderWorkaround<Entity> getWorkaround() {
        return workaround;
    }

    private boolean canUpdate = true;

    private boolean isAddedToWorld;

    @Override
    public boolean areCapsCompatible(CapabilityProvider<Entity> other) {
        return workaround.areCapsCompatible(other);
    }

    @Override
    public boolean areCapsCompatible(@Nullable CapabilityDispatcher other) {
        return workaround.areCapsCompatible(other);
    }

    @Override
    public void invalidateCaps() {
        workaround.invalidateCaps();
    }

    @Override
    public void reviveCaps() {
        workaround.reviveCaps();
    }

    @Override
    public void gatherCapabilities(@Nullable Supplier<ICapabilityProvider> parent) {
        workaround.invokeGatherCapabilities(parent);
    }
}
