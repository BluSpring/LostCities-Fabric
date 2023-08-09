package mcjty.lostcities;

import mcjty.lostcities.setup.Config;
import mcjty.lostcities.setup.CustomRegistries;
import mcjty.lostcities.setup.ModSetup;
import mcjty.lostcities.setup.Registration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;

public class LostCities implements ModInitializer {
    public static final String MODID = "lostcities";

    public static final Logger LOGGER = LogManager.getLogger(LostCities.MODID);

    public static final ModSetup setup = new ModSetup();
    public static LostCities instance;
    public static final LostCitiesImp lostCitiesImp = new LostCitiesImp();

    @Override
    public void onInitialize() {
        instance = this;

        Registration.init();
        CustomRegistries.init();

        Path configPath = FabricLoader.getInstance().getConfigDir();
        File dir = new File(configPath + File.separator + "lostcities");
        dir.mkdirs();

        ModLoadingContext.registerConfig(MODID, ModConfig.Type.CLIENT, Config.CLIENT_CONFIG, "lostcities/client.toml");
        ModLoadingContext.registerConfig(MODID, ModConfig.Type.COMMON, Config.COMMON_CONFIG, "lostcities/common.toml");
        ModLoadingContext.registerConfig(MODID, ModConfig.Type.SERVER, Config.SERVER_CONFIG);

        setup.init();
        onConstructModEvent();
        processIMC();
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    private void onConstructModEvent() {
        /*event.enqueueWork(() -> {
            event.getIMCStream(ILostCities.GET_LOST_CITIES_PRE::equals).forEach(message -> {
                Supplier<Function<ILostCitiesPre, Void>> supplier = message.getMessageSupplier();
                supplier.get().apply(new LostCitiesPreImp());
            });
        });*/
    }

    private void processIMC() {
        var impl = new LostCitiesImp();

        /*event.getIMCStream(ILostCities.GET_LOST_CITIES::equals).forEach(message -> {
            Supplier<Function<ILostCities, Void>> supplier = message.getMessageSupplier();
            supplier.get().apply(new LostCitiesImp());
        });*/
    }
}
