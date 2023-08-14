package mcjty.lostcities.playerdata;

import xyz.bluspring.forgecapabilities.capabilities.Capability;
import xyz.bluspring.forgecapabilities.capabilities.CapabilityManager;
import xyz.bluspring.forgecapabilities.capabilities.CapabilityToken;

public class PlayerProperties {

    public static Capability<PlayerSpawnSet> PLAYER_SPAWN_SET
            = CapabilityManager.get(new CapabilityToken<>(){});
}
