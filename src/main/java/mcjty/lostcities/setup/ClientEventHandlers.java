package mcjty.lostcities.setup;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.lostcities.LostCities;
import mcjty.lostcities.gui.GuiLCConfig;
import mcjty.lostcities.gui.LostCitySetup;
import mcjty.lostcities.mixin.CreateWorldScreenAccessor;
import mcjty.lostcities.varia.ComponentFactory;
import mcjty.lostcities.worldgen.LostCityFeature;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.impl.client.screen.ScreenExtensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.resources.ResourceLocation;

public class ClientEventHandlers {

    //
//    @SubscribeEvent
//    public void onFogEvent(EntityViewRenderEvent.FogColors event) {
//        if (WorldTypeTools.isLostCities(Minecraft.getInstance().world)) {
//            LostCityProfile profile = WorldTypeTools.getProfile(Minecraft.getInstance().world);
//            if (profile.FOG_RED >= 0) {
//                event.setRed(profile.FOG_RED);
//            }
//            if (profile.FOG_GREEN >= 0) {
//                event.setGreen(profile.FOG_GREEN);
//            }
//            if (profile.FOG_BLUE >= 0) {
//                event.setBlue(profile.FOG_BLUE);
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public void onFogDensity(EntityViewRenderEvent.FogDensity event) {
//        if (WorldTypeTools.isLostCities(Minecraft.getInstance().world)) {
//            LostCityProfile profile = WorldTypeTools.getProfile(Minecraft.getInstance().world);
//            if (profile.FOG_DENSITY >= 0) {
//                event.setDensity(profile.FOG_DENSITY);
//                event.setCanceled(true);
//            }
//        }
//    }

    private Button lostCitiesButton = null;

    public ClientEventHandlers() {
        ScreenEvents.BEFORE_INIT.register(((client, screen, scaledWidth, scaledHeight) -> {
            ScreenEvents.afterRender(screen).register(this::onGuiDraw);
        }));

        ScreenEvents.AFTER_INIT.register(this::onGuiPost);
        ClientPlayConnectionEvents.DISCONNECT.register(this::onPlayerLoggedOut);
    }

    public void onGuiDraw(Screen scr, PoseStack poseStack, int mouseX, int mouseY, float tickDelta) {
        if (scr instanceof CreateWorldScreen screen && lostCitiesButton != null) {
            lostCitiesButton.visible = ((CreateWorldScreenAccessor) screen).isWorldGenSettingsVisible();
            if (lostCitiesButton.visible) {
                RenderSystem.setShaderTexture(0, new ResourceLocation(LostCities.MODID, "textures/gui/configicon.png"));
                GuiComponent.blit(poseStack, screen.width - 100, 30, 70, 70, 256, 256, 256, 256, 256, 256);
            }
        }
    }

    public void onGuiPost(Minecraft client, Screen scr, int scaledWidth, int scaledHeight) {
        if (scr instanceof CreateWorldScreen screen) {
            lostCitiesButton = new Button(screen.width - 100, 10, 70, 20, ComponentFactory.literal("Cities"), p_onPress_1_ -> {
//                WorldType worldType = WorldType.WORLD_TYPES[screen.selectedIndex];
                Minecraft.getInstance().setScreen(new GuiLCConfig(screen /* @todo 1.16, worldType*/));
            });
            ScreenExtensions.getExtensions(screen).fabric_getButtons().add(lostCitiesButton);
            //event.addListener(lostCitiesButton);
        }
    }

    // To clean up client-side and single player
    public void onPlayerLoggedOut(ClientPacketListener handler, Minecraft client) {
        LostCitySetup.CLIENT_SETUP.reset();
        Config.reset();
        LostCityFeature.globalDimensionInfoDirtyCounter++;
    }
}
