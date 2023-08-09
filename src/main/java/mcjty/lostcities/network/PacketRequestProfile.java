package mcjty.lostcities.network;

import me.pepperbell.simplenetworking.C2SPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.Level;

public class PacketRequestProfile implements C2SPacket {

    private final ResourceKey<Level> dimension;

    public PacketRequestProfile(FriendlyByteBuf buf) {
        dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, buf.readResourceLocation());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceLocation(dimension.location());
    }

    public PacketRequestProfile(ResourceKey<Level> dimension) {
        this.dimension = dimension;
    }

    /*public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // @todo 1.14
//            ServerPlayerEntity player = ctx.get().getSender();
//            LostCityProfile profile = WorldTypeTools.getProfile(WorldTools.getWorld(dimension));
//            PacketHandler.INSTANCE.sendTo(new PacketReturnProfileToClient(dimension, profile.getName()), player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        });
        ctx.get().setPacketHandled(true);
    }*/

    @Override
    public void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, PacketSender responseSender, SimpleChannel channel) {
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        toBytes(buf);
    }
}
