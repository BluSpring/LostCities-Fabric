package mcjty.lostcities.setup;

import com.mojang.serialization.Codec;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import mcjty.lostcities.LostCities;
import mcjty.lostcities.worldgen.lost.regassets.*;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import xyz.bluspring.forgebiomemodifiers.registries.DatapackRegistryInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class CustomRegistries {
    public static class RegistryBuilder<T> {
        Codec<T> codec;

        public RegistryBuilder() {}

        public RegistryBuilder<T> dataPackRegistry(Codec<T> codec) {
            this.codec = codec;
            return this;
        }
    }

    private static final List<DatapackRegistryInfo<?>> datapackRegistryInfoList = new LinkedList<>();

    public static List<DatapackRegistryInfo<?>> getDatapackRegistryInfos() {
        return datapackRegistryInfoList;
    }

    public static <T> Supplier<Registry<T>> makeRegistry(LazyRegistrar<T> registrar, Supplier<RegistryBuilder<T>> builderSupplier) {
        var registry = registrar.makeRegistry();

        datapackRegistryInfoList.add(new DatapackRegistryInfo<>(registrar.getRegistryKey(), builderSupplier.get().codec));

        return registry;
    }

    public static final ResourceKey<Registry<BuildingRE>> BUILDING_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(LostCities.MODID, "lostcities/buildings"));
    public static final LazyRegistrar<BuildingRE> BUILDING_DEFERRED_REGISTER = LazyRegistrar.create(BUILDING_REGISTRY_KEY, LostCities.MODID);
    public static final Supplier<Registry<BuildingRE>> BUILDING_REGISTRY = makeRegistry(BUILDING_DEFERRED_REGISTER, () -> new RegistryBuilder<BuildingRE>().dataPackRegistry(BuildingRE.CODEC));

    public static final ResourceKey<Registry<PaletteRE>> PALETTE_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(LostCities.MODID, "lostcities/palettes"));
    public static final LazyRegistrar<PaletteRE> PALETTE_DEFERRED_REGISTER = LazyRegistrar.create(PALETTE_REGISTRY_KEY, LostCities.MODID);
    public static final Supplier<Registry<PaletteRE>> PALETTE_REGISTRY = makeRegistry(PALETTE_DEFERRED_REGISTER, () -> new RegistryBuilder<PaletteRE>().dataPackRegistry(PaletteRE.CODEC));

    public static final ResourceKey<Registry<BuildingPartRE>> PART_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(LostCities.MODID, "lostcities/parts"));
    public static final LazyRegistrar<BuildingPartRE> PART_DEFERRED_REGISTER = LazyRegistrar.create(PART_REGISTRY_KEY, LostCities.MODID);
    public static final Supplier<Registry<BuildingPartRE>> PART_REGISTRY = makeRegistry(PART_DEFERRED_REGISTER, () -> new RegistryBuilder<BuildingPartRE>().dataPackRegistry(BuildingPartRE.CODEC));

    public static final ResourceKey<Registry<StyleRE>> STYLE_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(LostCities.MODID, "lostcities/styles"));
    public static final LazyRegistrar<StyleRE> STYLE_DEFERRED_REGISTER = LazyRegistrar.create(STYLE_REGISTRY_KEY, LostCities.MODID);
    public static final Supplier<Registry<StyleRE>> STYLE_REGISTRY = makeRegistry(STYLE_DEFERRED_REGISTER, () -> new RegistryBuilder<StyleRE>().dataPackRegistry(StyleRE.CODEC));

    public static final ResourceKey<Registry<ConditionRE>> CONDITIONS_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(LostCities.MODID, "lostcities/conditions"));
    public static final LazyRegistrar<ConditionRE> CONDITIONS_DEFERRED_REGISTER = LazyRegistrar.create(CONDITIONS_REGISTRY_KEY, LostCities.MODID);
    public static final Supplier<Registry<ConditionRE>> CONDITIONS_REGISTRY = makeRegistry(CONDITIONS_DEFERRED_REGISTER, () -> new RegistryBuilder<ConditionRE>().dataPackRegistry(ConditionRE.CODEC));

    public static final ResourceKey<Registry<CityStyleRE>> CITYSTYLES_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(LostCities.MODID, "lostcities/citystyles"));
    public static final LazyRegistrar<CityStyleRE> CITYSTYLES_DEFERRED_REGISTER = LazyRegistrar.create(CITYSTYLES_REGISTRY_KEY, LostCities.MODID);
    public static final Supplier<Registry<CityStyleRE>> CITYSTYLES_REGISTRY = makeRegistry(CITYSTYLES_DEFERRED_REGISTER, () -> new RegistryBuilder<CityStyleRE>().dataPackRegistry(CityStyleRE.CODEC));

    public static final ResourceKey<Registry<MultiBuildingRE>> MULTIBUILDINGS_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(LostCities.MODID, "lostcities/multibuildings"));
    public static final LazyRegistrar<MultiBuildingRE> MULTIBUILDINGS_DEFERRED_REGISTER = LazyRegistrar.create(MULTIBUILDINGS_REGISTRY_KEY, LostCities.MODID);
    public static final Supplier<Registry<MultiBuildingRE>> MULTIBUILDINGS_REGISTRY = makeRegistry(MULTIBUILDINGS_DEFERRED_REGISTER, () -> new RegistryBuilder<MultiBuildingRE>().dataPackRegistry(MultiBuildingRE.CODEC));

    public static final ResourceKey<Registry<VariantRE>> VARIANTS_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(LostCities.MODID, "lostcities/variants"));
    public static final LazyRegistrar<VariantRE> VARIANTS_DEFERRED_REGISTER = LazyRegistrar.create(VARIANTS_REGISTRY_KEY, LostCities.MODID);
    public static final Supplier<Registry<VariantRE>> VARIANTS_REGISTRY = makeRegistry(VARIANTS_DEFERRED_REGISTER, () -> new RegistryBuilder<VariantRE>().dataPackRegistry(VariantRE.CODEC));

    public static final ResourceKey<Registry<WorldStyleRE>> WORLDSTYLES_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(LostCities.MODID, "lostcities/worldstyles"));
    public static final LazyRegistrar<WorldStyleRE> WORLDSTYLES_DEFERRED_REGISTER = LazyRegistrar.create(WORLDSTYLES_REGISTRY_KEY, LostCities.MODID);
    public static final Supplier<Registry<WorldStyleRE>> WORLDSTYLES_REGISTRY = makeRegistry(WORLDSTYLES_DEFERRED_REGISTER, () -> new RegistryBuilder<WorldStyleRE>().dataPackRegistry(WorldStyleRE.CODEC));

    public static final ResourceKey<Registry<PredefinedCityRE>> PREDEFINEDCITIES_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(LostCities.MODID, "lostcities/predefinedcites"));
    public static final LazyRegistrar<PredefinedCityRE> PREDEFINEDCITIES_DEFERRED_REGISTER = LazyRegistrar.create(PREDEFINEDCITIES_REGISTRY_KEY, LostCities.MODID);
    public static final Supplier<Registry<PredefinedCityRE>> PREDEFINEDCITIES_REGISTRY = makeRegistry(PREDEFINEDCITIES_DEFERRED_REGISTER, () -> new RegistryBuilder<PredefinedCityRE>().dataPackRegistry(PredefinedCityRE.CODEC));

    public static final ResourceKey<Registry<PredefinedSphereRE>> PREDEFINEDSPHERES_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(LostCities.MODID, "lostcities/predefinedspheres"));
    public static final LazyRegistrar<PredefinedSphereRE> PREDEFINEDSPHERES_DEFERRED_REGISTER = LazyRegistrar.create(PREDEFINEDSPHERES_REGISTRY_KEY, LostCities.MODID);
    public static final Supplier<Registry<PredefinedSphereRE>> PREDEFINEDSPHERES_REGISTRY = makeRegistry(PREDEFINEDSPHERES_DEFERRED_REGISTER, () -> new RegistryBuilder<PredefinedSphereRE>().dataPackRegistry(PredefinedSphereRE.CODEC));


    public static final ResourceKey<Registry<ScatteredRE>> SCATTERED_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(LostCities.MODID, "lostcities/scattered"));
    public static final LazyRegistrar<ScatteredRE> SCATTERED_DEFERRED_REGISTER = LazyRegistrar.create(SCATTERED_REGISTRY_KEY, LostCities.MODID);
    public static final Supplier<Registry<ScatteredRE>> SCATTERED_REGISTRY = makeRegistry(SCATTERED_DEFERRED_REGISTER, () -> new RegistryBuilder<ScatteredRE>().dataPackRegistry(ScatteredRE.CODEC));

    public static void init() {
        BUILDING_DEFERRED_REGISTER.register();
        PALETTE_DEFERRED_REGISTER.register();
        PART_DEFERRED_REGISTER.register();
        STYLE_DEFERRED_REGISTER.register();
        CONDITIONS_DEFERRED_REGISTER.register();
        CITYSTYLES_DEFERRED_REGISTER.register();
        MULTIBUILDINGS_DEFERRED_REGISTER.register();
        VARIANTS_DEFERRED_REGISTER.register();
        WORLDSTYLES_DEFERRED_REGISTER.register();
        PREDEFINEDCITIES_DEFERRED_REGISTER.register();
        PREDEFINEDSPHERES_DEFERRED_REGISTER.register();
        SCATTERED_DEFERRED_REGISTER.register();

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.RAW_GENERATION, Registration.PLACED_LOSTCITY_FEATURE.getKey());
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.VEGETAL_DECORATION, Registration.PLACED_LOSTCITY_SPHERE_FEATURE.getKey());
    }

}
