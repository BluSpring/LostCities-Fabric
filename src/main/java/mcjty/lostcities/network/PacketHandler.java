package mcjty.lostcities.network;


import mcjty.lostcities.LostCities;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

public class PacketHandler {
    private static int ID = 12;
    private static int packetId = 0;

    public static SimpleChannel INSTANCE = null;

    public static int nextPacketID() {
        return packetId++;
    }

    public PacketHandler() {
    }

    public static int nextID() {
        return ID++;
    }

    public static void registerMessages(String channelName) {
        INSTANCE = new SimpleChannel(new ResourceLocation(LostCities.MODID, channelName));
        registerMessages();
    }

    public static void registerMessages() {
        INSTANCE.registerC2SPacket(PacketRequestProfile.class, nextID(),
                PacketRequestProfile::new);
        INSTANCE.registerS2CPacket(PacketReturnProfileToClient.class, nextID(),
                PacketReturnProfileToClient::new);

        INSTANCE.initServerListener();
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            INSTANCE.initClientListener();
    }
}
