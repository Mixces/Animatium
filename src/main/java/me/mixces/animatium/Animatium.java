package me.mixces.animatium;

import me.mixces.animatium.config.AnimatiumConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class Animatium implements ModInitializer {

	/*
	TODO FIX LIST:
	projectile position (potions, rod)
	centered scrollable gui
	armor trim damage overlay
	eye height under slabs
	simplify sneak body rotation reassignment
	cape rotations

	TODO FEATURE LIST:
	item reequip
	item pickup
	remove thrown/dropped swing
	debug menu
	tab menu?
	 */

	public static AnimatiumConfig CONFIG;

	@Override
	public void onInitialize() {
		AutoConfig.register(AnimatiumConfig.class, GsonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(AnimatiumConfig.class).getConfig();
	}
}