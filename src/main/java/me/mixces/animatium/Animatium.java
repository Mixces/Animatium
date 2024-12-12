package me.mixces.animatium;

import me.mixces.animatium.config.AnimatiumConfig;
import net.fabricmc.api.ClientModInitializer;

public class Animatium implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AnimatiumConfig.init("animatium", AnimatiumConfig.class);
    }
}