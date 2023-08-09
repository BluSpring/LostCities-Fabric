package mcjty.lostcities.network;

import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class PacketReturnProfileToClient implements S2CPacket {

    private final ResourceKey<Level> dimension;
    private final String profile;

    public PacketReturnProfileToClient(FriendlyByteBuf buf) {
        dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, buf.readResourceLocation());
        profile = buf.readUtf(32767);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceLocation(dimension.location());
        buf.writeUtf(profile);
    }

    public PacketReturnProfileToClient(ResourceKey<Level> dimension, String profileName) {
        this.dimension = dimension;
        this.profile = profileName;
    }

    /*public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // @todo 1.14
//            WorldTypeTools.setProfileFromServer(dimension, profile);
        });
        ctx.get().setPacketHandled(true);
    }*/

    @Override
    public void handle(Minecraft client, ClientPacketListener listener, PacketSender responseSender, SimpleChannel channel) {
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        toBytes(buf);
    }
}
