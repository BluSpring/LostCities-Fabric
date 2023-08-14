package mcjty.lostcities.playerdata;

import io.github.fabricators_of_create.porting_lib.extensions.INBTSerializable;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import xyz.bluspring.forgecapabilities.capabilities.Capability;
import xyz.bluspring.forgecapabilities.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PropertiesDispatcher implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    private PlayerSpawnSet playerSpawnSet = null;
    private LazyOptional<PlayerSpawnSet> opt = LazyOptional.of(this::createPlayerSpawnSet);

    @Nonnull
    private PlayerSpawnSet createPlayerSpawnSet() {
        if (playerSpawnSet == null) {
            playerSpawnSet = new PlayerSpawnSet();
        }
        return playerSpawnSet;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == PlayerProperties.PLAYER_SPAWN_SET) {
            return opt.cast();
        }
        return LazyOptional.empty();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerSpawnSet().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerSpawnSet().loadNBTData(nbt);
    }
}
