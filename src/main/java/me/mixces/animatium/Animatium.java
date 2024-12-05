package me.mixces.animatium;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	 */

	public static final String MOD_ID = "animatium";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Animatium initialized!");
	}
}