package mcjty.lostcities.mixin;

import mcjty.lostcities.forge.events.CreateWorldSpawnCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "setInitialSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getChunkSource()Lnet/minecraft/server/level/ServerChunkCache;", shift = At.Shift.AFTER), cancellable = true)
    private static void lc$invokeCreateSpawn(ServerLevel level, ServerLevelData levelData, boolean generateBonusChest, boolean debug, CallbackInfo ci) {
        if (CreateWorldSpawnCallback.EVENT.invoker().onCreateWorldSpawn(level, levelData)) {
            ci.cancel();
        }
    }
}
