package mcjty.lostcities.setup;

import net.fabricmc.api.ClientModInitializer;

public class ClientSetup implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        new ClientEventHandlers();
    }
}
