package mcjty.lostcities.setup;


import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import mcjty.lostcities.LostCities;
import mcjty.lostcities.worldgen.LostCityFeature;
import mcjty.lostcities.worldgen.LostCitySphereFeature;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class Registration {

    public static final LazyRegistrar<Feature<?>> FEATURES = LazyRegistrar.create(Registry.FEATURE_REGISTRY, LostCities.MODID);
    public static final LazyRegistrar<ConfiguredFeature<?,?>> CONFIGURED_FEATURES = LazyRegistrar.create(Registry.CONFIGURED_FEATURE_REGISTRY, LostCities.MODID);
    public static final LazyRegistrar<PlacedFeature> PLACED_FEATURES = LazyRegistrar.create(Registry.PLACED_FEATURE_REGISTRY, LostCities.MODID);

    public static void init() {
        FEATURES.register();
        CONFIGURED_FEATURES.register();
        PLACED_FEATURES.register();
    }

    public static final RegistryObject<LostCityFeature> LOSTCITY_FEATURE = FEATURES.register("lostcity", LostCityFeature::new);
    public static final RegistryObject<LostCitySphereFeature> LOSTCITY_SPHERE_FEATURE = FEATURES.register("spheres", LostCitySphereFeature::new);

    public static final RegistryObject<ConfiguredFeature<?, ?>> CONFIGURED_LOSTCITY_FEATURE = CONFIGURED_FEATURES.register(
            "lostcities", () -> new ConfiguredFeature<>(LOSTCITY_FEATURE.get(), FeatureConfiguration.NONE));
    public static final RegistryObject<PlacedFeature> PLACED_LOSTCITY_FEATURE = PLACED_FEATURES.register(
            "lostcities", () -> new PlacedFeature(CONFIGURED_LOSTCITY_FEATURE.getHolder().get(), List.of(CountPlacement.of(1))));

    public static final RegistryObject<ConfiguredFeature<?, ?>> CONFIGURED_LOSTCITY_SPHERE_FEATURE = CONFIGURED_FEATURES.register(
            "spheres", () -> new ConfiguredFeature<>(LOSTCITY_SPHERE_FEATURE.get(), FeatureConfiguration.NONE));
    public static final RegistryObject<PlacedFeature> PLACED_LOSTCITY_SPHERE_FEATURE = PLACED_FEATURES.register(
            "spheres", () -> new PlacedFeature(CONFIGURED_LOSTCITY_SPHERE_FEATURE.getHolder().get(), List.of(CountPlacement.of(1))));

    public static final ResourceLocation LOSTCITY = new ResourceLocation(LostCities.MODID, "lostcity");

    public static final ResourceKey<DimensionType> DIMENSION_TYPE = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, LOSTCITY);
    public static final ResourceKey<Level> DIMENSION = ResourceKey.create(Registry.DIMENSION_REGISTRY, LOSTCITY);
}
