package mcjty.lostcities.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MinecraftServer.class)
public interface MinecraftServerAccessor {
    @Invoker
    void callDoRunTask(TickTask task);
}
