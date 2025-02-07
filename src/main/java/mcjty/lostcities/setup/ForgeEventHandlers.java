package mcjty.lostcities.setup;

import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.event.events.common.PlayerEvent;
import io.github.fabricators_of_create.porting_lib.event.common.EntityEvents;
import mcjty.lostcities.LostCities;
import mcjty.lostcities.commands.ModCommands;
import mcjty.lostcities.config.LostCityProfile;
import mcjty.lostcities.forge.events.CreateWorldSpawnCallback;
import mcjty.lostcities.playerdata.PlayerProperties;
import mcjty.lostcities.playerdata.PropertiesDispatcher;
import mcjty.lostcities.varia.ComponentFactory;
import mcjty.lostcities.varia.CustomTeleporter;
import mcjty.lostcities.varia.WorldTools;
import mcjty.lostcities.worldgen.GlobalTodo;
import mcjty.lostcities.worldgen.IDimensionInfo;
import mcjty.lostcities.worldgen.lost.*;
import mcjty.lostcities.worldgen.lost.cityassets.AssetRegistries;
import mcjty.lostcities.worldgen.lost.cityassets.PredefinedCity;
import mcjty.lostcities.worldgen.lost.cityassets.PredefinedSphere;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ServerLevelData;
import xyz.bluspring.forgecapabilities.capabilities.AttachCapabilitiesCallback;
import xyz.bluspring.forgecapabilities.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

import static mcjty.lostcities.setup.Registration.LOSTCITY;

public class ForgeEventHandlers {

    private final Map<ResourceKey<Level>, BlockPos> spawnPositions = new HashMap<>();

    public ForgeEventHandlers() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            commandRegister(dispatcher);
        });

        PlayerEvent.PLAYER_CLONE.register((oldPlayer, newPlayer, wonGame) -> {
            onPlayerCloned(oldPlayer, newPlayer, !wonGame);
        });

        AttachCapabilitiesCallback.EVENT.register((object, capabilities, listeners) -> {
            onEntityConstructing(object, capabilities);
        });

        EntityEvents.ON_JOIN_WORLD.register((entity, world, diskLoaded) -> {
            if (!(entity instanceof Player))
                return true;

            onPlayerLoggedIn(entity, world);

            return true;
        });

        ServerTickEvents.END_WORLD_TICK.register((level) -> {
            onWorldTick(level);
        });

        ServerLifecycleEvents.SERVER_STARTING.register((server) -> {
            onServerStarting();
        });

        CreateWorldSpawnCallback.EVENT.register(this::onCreateSpawnPoint);

        EntitySleepEvents.ALLOW_SLEEPING.register((player, pos) -> {
            return onPlayerSleepInBedEvent(player, pos);
        });
    }

    public void commandRegister(CommandDispatcher<CommandSourceStack> dispatcher) {
        ModCommands.register(dispatcher);
    }

    public void onEntityConstructing(ICapabilityProvider object, Map<ResourceLocation, ICapabilityProvider> capabilities){
        if (object instanceof Player) {
            if (!object.getCapability(PlayerProperties.PLAYER_SPAWN_SET).isPresent()) {
                capabilities.put(new ResourceLocation(LostCities.MODID, "spawnset"), new PropertiesDispatcher());
            }
        }
    }

    public void onPlayerCloned(ServerPlayer original, ServerPlayer entity, boolean wasDeath) {
        if (wasDeath) {
            // We need to copyFrom the capabilities
            ((ICapabilityProvider) original).getCapability(PlayerProperties.PLAYER_SPAWN_SET).ifPresent(oldStore -> {
                ((ICapabilityProvider) entity).getCapability(PlayerProperties.PLAYER_SPAWN_SET).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }

    public void onPlayerLoggedIn(Entity entity, Level level) {
        ((ICapabilityProvider) entity).getCapability(PlayerProperties.PLAYER_SPAWN_SET).ifPresent(note -> {
            if (!note.isPlayerSpawnSet()) {
                note.setPlayerSpawnSet(true);
                for (Map.Entry<ResourceKey<Level>, BlockPos> entry : spawnPositions.entrySet()) {
                    if (entity instanceof ServerPlayer serverPlayer) {
                        serverPlayer.setRespawnPosition(entry.getKey(), entry.getValue(), 0.0f, true, true);
                        serverPlayer.teleportTo(entry.getValue().getX(), entry.getValue().getY(), entry.getValue().getZ());
                    }
                }
            }
        });
    }

    public void onWorldTick(ServerLevel serverLevel) {
        GlobalTodo.getData(serverLevel).executeAndClearTodo(serverLevel);
    }

    public void onServerStarting() {
        BuildingInfo.cleanCache();
        Highway.cleanCache();
        Railway.cleanCache();
        BiomeInfo.cleanCache();
        City.cleanCache();
        CitySphere.cleanCache();
    }

    public boolean onCreateSpawnPoint(Level world, ServerLevelData settings) {
        if (world instanceof ServerLevel serverLevel) {
            IDimensionInfo dimensionInfo = Registration.LOSTCITY_FEATURE.get().getDimensionInfo(serverLevel);
            if (dimensionInfo == null) {
                return false;
            }
            LostCityProfile profile = dimensionInfo.getProfile();

            Predicate<BlockPos> isSuitable = pos -> true;
            boolean needsCheck = false;

            if (!profile.SPAWN_BIOME.isEmpty()) {
                final Biome spawnBiome = BuiltinRegistries.BIOME.get(new ResourceLocation(profile.SPAWN_BIOME));
                if (spawnBiome == null) {
                    ModSetup.getLogger().error("Cannot find biome '{}' for the player to spawn in !", profile.SPAWN_BIOME);
                } else {
                    isSuitable = blockPos -> world.getBiome(blockPos).value() == spawnBiome;
                    needsCheck = true;
                }
            } else if (!profile.SPAWN_CITY.isEmpty()) {
                final PredefinedCity city = AssetRegistries.PREDEFINED_CITIES.get(world, profile.SPAWN_CITY);
                if (city == null) {
                    ModSetup.getLogger().error("Cannot find city '{}' for the player to spawn in !", profile.SPAWN_CITY);
                } else {
                    float sqradius = getSqRadius(city.getRadius(), 0.8f);
                    isSuitable = blockPos -> city.getDimension() == serverLevel.dimension() &&
                            CitySphere.squaredDistance(city.getChunkX()*16+8, city.getChunkZ()*16+8, blockPos.getX(), blockPos.getZ()) < sqradius;
                    needsCheck = true;
                }
            } else if (!profile.SPAWN_SPHERE.isEmpty()) {
                if ("<in>".equals(profile.SPAWN_SPHERE)) {
                    isSuitable = blockPos -> {
                        CitySphere sphere = CitySphere.getCitySphere(blockPos.getX() >> 4, blockPos.getZ() >> 4, dimensionInfo);
                        if (!sphere.isEnabled()) {
                            return false;
                        }
                        float sqradius = getSqRadius((int) sphere.getRadius(), 0.8f);
                        return sphere.getCenterPos().distSqr(blockPos.atY(sphere.getCenterPos().getY())) < sqradius;
                    };
                    needsCheck = true;
                } else if ("<out>".equals(profile.SPAWN_SPHERE)) {
                    isSuitable = blockPos -> {
                        CitySphere sphere = CitySphere.getCitySphere(blockPos.getX() >> 4, blockPos.getZ() >> 4, dimensionInfo);
                        if (!sphere.isEnabled()) {
                            return true;
                        }
                        float sqradius = sphere.getRadius() * sphere.getRadius();
                        return sphere.getCenterPos().distSqr(blockPos.atY(sphere.getCenterPos().getY())) > sqradius;
                    };
                    needsCheck = true;
                } else {
                    final PredefinedSphere sphere = AssetRegistries.PREDEFINED_SPHERES.get(world, profile.SPAWN_SPHERE);
                    if (sphere == null) {
                        LostCities.setup.getLogger().error("Cannot find sphere '" + profile.SPAWN_SPHERE + "' for the player to spawn in !");
                    } else {
                        float sqradius = getSqRadius(sphere.getRadius(), 0.8f);
                        isSuitable = blockPos -> sphere.getDimension() == serverLevel.dimension() &&
                                CitySphere.squaredDistance(sphere.getChunkX() * 16 + 8, sphere.getChunkZ() * 16 + 8, blockPos.getX(), blockPos.getZ()) < sqradius;
                        needsCheck = true;
                    }
                }
            }

            if (profile.SPAWN_NOT_IN_BUILDING) {
                isSuitable = isSuitable.and(blockPos -> isOutsideBuilding(dimensionInfo, blockPos));
                needsCheck = true;
            } else if (profile.FORCE_SPAWN_IN_BUILDING) {
                isSuitable = isSuitable.and(blockPos -> !isOutsideBuilding(dimensionInfo, blockPos));
                needsCheck = true;
            }

            // Potentially set the spawn point
            switch (profile.LANDSCAPE_TYPE) {
                case DEFAULT:
                case SPHERES:
                case CAVERN:
                    if (needsCheck) {
                        BlockPos pos = findSafeSpawnPoint(serverLevel, dimensionInfo, isSuitable, settings);
                        settings.setSpawn(pos, 0.0f);
                        spawnPositions.put(serverLevel.dimension(), pos);
                        return true;
                    }
                    break;
                case FLOATING:
                case SPACE:
                    BlockPos pos = findSafeSpawnPoint(serverLevel, dimensionInfo, isSuitable, settings);
                    settings.setSpawn(pos, 0.0f);
                    spawnPositions.put(serverLevel.dimension(), pos);
                    return true;
            }
        }

        return false;
    }

    private boolean isOutsideBuilding(IDimensionInfo provider, BlockPos pos) {
        BuildingInfo info = BuildingInfo.getBuildingInfo(pos.getX() >> 4, pos.getZ() >> 4, provider);
        return !(info.isCity() && info.hasBuilding);
    }

    private int getSqRadius(int radius, float pct) {
        return (int) ((radius * pct) * (radius * pct));
    }

    private BlockPos findSafeSpawnPoint(Level world, IDimensionInfo provider, @Nonnull Predicate<BlockPos> isSuitable,
                                    @Nonnull ServerLevelData serverLevelData) {
        Random rand = new Random(provider.getSeed());
        rand.nextFloat();
        rand.nextFloat();
        int radius = 200;
        int attempts = 0;
//        int bottom = world.getWorldType().getMinimumSpawnHeight(world);
        while (true) {
            for (int i = 0 ; i < 200 ; i++) {
                int x = rand.nextInt(radius * 2) - radius;
                int z = rand.nextInt(radius * 2) - radius;
                attempts++;

                if (!isSuitable.test(new BlockPos(x, 128, z))) {
                    continue;
                }

                LostCityProfile profile = BuildingInfo.getProfile(x >> 4, z >> 4, provider);

                for (int y = profile.GROUNDLEVEL-5 ; y < 125 ; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (isValidStandingPosition(world, pos)) {
//                        serverLevelData.setSpawn(pos.above(), 0.0f);
                        return pos.above();
                    }
                }
            }
            radius += 100;
            if (attempts > 20000) {
                LostCities.setup.getLogger().error("Can't find a valid spawn position!");
                throw new RuntimeException("Can't find a valid spawn position!");
            }
        }
    }

    private boolean isValidStandingPosition(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
//        return state.getBlock().isTopSolid(state) && state.getBlock().isFullCube(state) && state.getBlock().isOpaqueCube(state) && world.isAirBlock(pos.up()) && world.isAirBlock(pos.up(2));
        // @todo 1.14
        return state.canOcclude();
    }

    private boolean isValidSpawnBed(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof BedBlock)) {
            return false;
        }
        Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);
        Block b1 = world.getBlockState(pos.below()).getBlock();
        Block b2 = world.getBlockState(pos.relative(direction.getOpposite()).below()).getBlock();
        Block b = Registry.BLOCK.get(new ResourceLocation(Config.SPECIAL_BED_BLOCK.get()));
        if (b1 != b || b2 != b) {
            return false;
        }
        // Check if the bed is surrounded by 6 skulls
        if (!(world.getBlockState(pos.relative(direction)).getBlock() instanceof AbstractSkullBlock)) {   // @todo 1.14 other skulls!
            return false;
        }
        if (!(world.getBlockState(pos.relative(direction.getClockWise())).getBlock() instanceof AbstractSkullBlock)) {
            return false;
        }
        if (!(world.getBlockState(pos.relative(direction.getCounterClockWise())).getBlock() instanceof AbstractSkullBlock)) {
            return false;
        }
        if (!(world.getBlockState(pos.relative(direction.getOpposite(), 2)).getBlock() instanceof AbstractSkullBlock)) {
            return false;
        }
        if (!(world.getBlockState(pos.relative(direction.getOpposite()).relative(direction.getOpposite().getClockWise())).getBlock() instanceof AbstractSkullBlock)) {
            return false;
        }
        if (!(world.getBlockState(pos.relative(direction.getOpposite()).relative(direction.getOpposite().getCounterClockWise())).getBlock() instanceof AbstractSkullBlock)) {
            return false;
        }
        return true;
    }

    private BlockPos findValidTeleportLocation(Level world, BlockPos start) {
        int chunkX = start.getX()>>4;
        int chunkZ = start.getZ()>>4;
        int y = start.getY();
        BlockPos pos = findValidTeleportLocation(world, chunkX, chunkZ, y);
        if (pos != null) {
            return pos;
        }
        for (int r = 1 ; r < 50 ; r++) {
            for (int i = -r ; i < r ; i++) {
                pos = findValidTeleportLocation(world, chunkX + i, chunkZ - r, y);
                if (pos != null) {
                    return pos;
                }
                pos = findValidTeleportLocation(world, chunkX + r, chunkZ + i, y);
                if (pos != null) {
                    return pos;
                }
                pos = findValidTeleportLocation(world, chunkX + r - i, chunkZ + r, y);
                if (pos != null) {
                    return pos;
                }
                pos = findValidTeleportLocation(world, chunkX - r, chunkZ + r - i, y);
                if (pos != null) {
                    return pos;
                }
            }
        }
        return null;
    }

    private BlockPos findValidTeleportLocation(Level world, int chunkX, int chunkZ, int y) {
        BlockPos bestSpot = null;
        for (int dy = 0 ; dy < 255 ; dy++) {
            for (int x = 0 ; x < 16 ; x++) {
                for (int z = 0 ; z < 16 ; z++) {
                    if ((y + dy) < 250) {
                        BlockPos p = new BlockPos(chunkX * 16 + x, y + dy, chunkZ * 16 + z);
                        if (isValidSpawnBed(world, p)) {
                            return p.above();
                        }
                        if (bestSpot == null && isValidStandingPosition(world, p)) {
                            bestSpot = p.above();
                        }
                    }
                    if ((y - dy) > 1) {
                        BlockPos p = new BlockPos(chunkX * 16 + x, y - dy, chunkZ * 16 + z);
                        if (isValidSpawnBed(world, p)) {
                            return p.above();
                        }
                        if (bestSpot == null && isValidStandingPosition(world, p)) {
                            bestSpot = p.above();
                        }
                    }
                }
            }
        }
        return bestSpot;
    }

    public Player.BedSleepingProblem onPlayerSleepInBedEvent(Player entity, BlockPos bedLocation) {
//        if (LostCityConfiguration.DIMENSION_ID == null) {
//            return;
//        }

        Level world = entity.getCommandSenderWorld();
        if (world.isClientSide) {
            return null;
        }

        if (bedLocation == null || !isValidSpawnBed(world, bedLocation)) {
            return null;
        }

        if (world.dimension() == Registration.DIMENSION) {
            ServerLevel destWorld = WorldTools.getOverworld(world);
            BlockPos location = findLocation(bedLocation, destWorld);
            CustomTeleporter.teleportToDimension(entity, destWorld, location);

            return Player.BedSleepingProblem.OTHER_PROBLEM;
        } else {
            ServerLevel destWorld = entity.getCommandSenderWorld().getServer().getLevel(Registration.DIMENSION);
            if (destWorld == null) {
                entity.sendSystemMessage(ComponentFactory.literal("Error finding Lost City dimension: " + LOSTCITY + "!").withStyle(ChatFormatting.RED));
            } else {
                BlockPos location = findLocation(bedLocation, destWorld);
                CustomTeleporter.teleportToDimension(entity, destWorld, location);
            }

            return Player.BedSleepingProblem.OTHER_PROBLEM;
        }
    }

    private BlockPos findLocation(BlockPos bedLocation, ServerLevel destWorld) {
        BlockPos top = bedLocation.above(5);//destWorld.getHeight(Heightmap.Type.MOTION_BLOCKING, bedLocation).up(10);
        BlockPos location = top;
        while (top.getY() > 1 && destWorld.getBlockState(location).isAir()) {
            location = location.below();
        }
//        BlockPos location = findValidTeleportLocation(destWorld, top);
        if (destWorld.isEmptyBlock(location.below())) {
            // No place to teleport
            destWorld.setBlockAndUpdate(bedLocation, Blocks.COBBLESTONE.defaultBlockState());
        }
        return location.above(1);
    }
}
