package mcjty.lostcities.playerdata;

import mcjty.lostcities.forge.capabilities.Capability;
import mcjty.lostcities.forge.capabilities.CapabilityManager;
import mcjty.lostcities.forge.capabilities.CapabilityToken;

public class PlayerProperties {

    public static Capability<PlayerSpawnSet> PLAYER_SPAWN_SET
            = CapabilityManager.get(new CapabilityToken<>(){});
}
