package mcjty.lostcities.mixin;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.mojang.serialization.Codec;
import mcjty.lostcities.setup.CustomRegistries;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RegistryAccess.class)
public interface RegistryAccessMixin {
    @Shadow
    private static <E> void put(ImmutableMap.Builder<ResourceKey<? extends Registry<?>>, RegistryAccess.RegistryData<?>> builder, ResourceKey registryKey, Codec elementCodec) {
    }

    @ModifyReceiver(method = "method_30531", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;"))
    private static ImmutableMap.Builder<ResourceKey<? extends Registry<?>>, RegistryAccess.RegistryData<?>> lc$appendCustomRegistries(ImmutableMap.Builder<ResourceKey<? extends Registry<?>>, RegistryAccess.RegistryData<?>> builder) {
        for (CustomRegistries.DatapackRegistryInfo<?> datapackRegistryInfo : CustomRegistries.getDatapackRegistryInfos()) {
            put(builder, datapackRegistryInfo.registryKey(), datapackRegistryInfo.codec());
        }

        return builder;
    }
}
