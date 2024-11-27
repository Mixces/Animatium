package me.mixces.animatium;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Animatium implements ModInitializer {

	public static final String MOD_ID = "animatium";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Animatium initialized!");
	}
}