/**
 * Animatium
 * The all-you-could-want legacy animations mod for modern minecraft versions.
 * Brings back animations from the 1.7/1.8 era and more.
 * <p>
 * Copyright (C) 2024-2025 lowercasebtw
 * Copyright (C) 2024-2025 mixces
 * Copyright (C) 2024-2025 Contributors to the project retain their copyright
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * "MINECRAFT" LINKING EXCEPTION TO THE GPL
 */

package org.visuals.legacy.animatium.util;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariants;

import java.util.ArrayList;
import java.util.List;

public final class SuperSecretButton extends Button {
	public SuperSecretButton(Component message, OnPress onPress, int x, int y) {
		super(x, y, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, message, onPress, ignored -> Component.empty());
	}

	@Override
	// TODO: Find a better way to do this
	public void playDownSound(SoundManager soundManager) {
		final List<SoundEvent> sounds = new ArrayList<>();
		sounds.add(SoundEvents.EXPERIENCE_ORB_PICKUP);
		sounds.add(SoundEvents.GENERIC_DEATH);
		sounds.add(SoundEvents.ENDER_DRAGON_HURT);
		sounds.add(SoundEvents.SPLASH_POTION_BREAK);
		sounds.add(SoundEvents.BAT_DEATH);
		sounds.add(SoundEvents.SPIDER_DEATH);
		sounds.add(SoundEvents.RABBIT_AMBIENT);
		sounds.add(SoundEvents.WITHER_AMBIENT);
		sounds.add(SoundEvents.ZOMBIE_INFECT);
		sounds.add(SoundEvents.ENDERMAN_HURT);
		sounds.add(SoundEvents.SKELETON_STEP);
		sounds.add(SoundEvents.CHICKEN_EGG);
		sounds.add(SoundEvents.GUARDIAN_DEATH_LAND);
		sounds.add(SoundEvents.SILVERFISH_HURT);
		sounds.add(SoundEvents.PLAYER_DEATH);
		sounds.add(SoundEvents.CREEPER_PRIMED);
		sounds.add(SoundEvents.WEATHER_RAIN);
		sounds.add(SoundEvents.CHICKEN_STEP);
		sounds.add(SoundEvents.ELDER_GUARDIAN_DEATH);
		sounds.add(SoundEvents.GHAST_DEATH);
		sounds.add(SoundEvents.GUARDIAN_FLOP);
		sounds.add(SoundEvents.GUARDIAN_ATTACK);
		sounds.add(SoundEvents.WOLF_STEP);
		sounds.add(SoundEvents.TNT_PRIMED);
		sounds.add(SoundEvents.RABBIT_DEATH);
		sounds.add(SoundEvents.ZOMBIE_STEP);
		sounds.add(SoundEvents.SKELETON_DEATH);
		sounds.add(SoundEvents.VILLAGER_HURT);
		sounds.add(SoundEvents.WITHER_SPAWN);
		sounds.add(SoundEvents.MAGMA_CUBE_SQUISH);
		sounds.add(SoundEvents.HORSE_STEP_WOOD);
		sounds.add(SoundEvents.RABBIT_HURT);
		sounds.add(SoundEvents.COW_STEP);
		sounds.add(SoundEvents.RABBIT_JUMP);
		sounds.add(SoundEvents.GRASS_HIT);
		sounds.add(SoundEvents.SNOW_HIT);
		sounds.add(SoundEvents.SKELETON_HORSE_DEATH);
		sounds.add(SoundEvents.ZOMBIE_HORSE_AMBIENT);
		sounds.add(SoundEvents.WOLF_SOUNDS.get(WolfSoundVariants.SoundSet.CLASSIC).ambientSound().value());
		sounds.add(SoundEvents.PIG_DEATH);
		sounds.add(SoundEvents.GRASS_HIT);
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("random.fizz")).orElse(null).value());
		sounds.add(SoundEvents.FIRE_AMBIENT);
		sounds.add(SoundEvents.WOLF_SOUNDS.get(WolfSoundVariants.SoundSet.CLASSIC).pantSound().value());
		sounds.add(SoundEvents.HORSE_ANGRY);
		sounds.add(SoundEvents.ARROW_HIT);
		sounds.add(SoundEvents.ELDER_GUARDIAN_HURT);
		sounds.add(SoundEvents.BAT_TAKEOFF);
		sounds.add(SoundEvents.IRON_GOLEM_STEP);
		sounds.add(SoundEvents.ENDERMAN_TELEPORT);
		sounds.add(SoundEvents.LADDER_STEP);
		sounds.add(SoundEvents.ENDERMAN_STARE);
		sounds.add(SoundEvents.IRON_GOLEM_HURT);
		sounds.add(SoundEvents.GENERIC_EAT.value());
		sounds.add(SoundEvents.ELDER_GUARDIAN_AMBIENT);
		sounds.add(SoundEvents.LAVA_POP);
		sounds.add(SoundEvents.GENERIC_SPLASH);
		sounds.add(SoundEvents.ENDERMAN_AMBIENT);
		sounds.add(SoundEvents.GUARDIAN_HURT);
		sounds.add(SoundEvents.DONKEY_ANGRY);
		sounds.add(SoundEvents.BLAZE_DEATH);
		sounds.add(SoundEvents.ANVIL_USE);
		sounds.add(SoundEvents.PIGLIN_HURT);
		sounds.add(SoundEvents.WOOL_HIT);
		sounds.add(SoundEvents.SNOW_STEP);
		sounds.add(SoundEvents.HOSTILE_DEATH);
		sounds.add(SoundEvents.LAVA_AMBIENT);
		sounds.add(SoundEvents.GENERIC_DRINK.value());
		sounds.add(SoundEvents.WATER_AMBIENT);
		sounds.add(SoundEvents.WOODEN_BUTTON_CLICK_ON);
		sounds.add(SoundEvents.PORTAL_AMBIENT);
		sounds.add(SoundEvents.ZOMBIE_HURT);
		sounds.add(SoundEvents.HORSE_JUMP);
		sounds.add(SoundEvents.HOSTILE_HURT);
		sounds.add(SoundEvents.ENDER_DRAGON_GROWL);
		sounds.add(SoundEvents.DONKEY_AMBIENT);
		sounds.add(SoundEvents.VILLAGER_DEATH);
		sounds.add(SoundEvents.SPIDER_STEP);
		sounds.add(SoundEvents.HORSE_BREATHE);
		sounds.add(SoundEvents.PORTAL_TRIGGER);
		sounds.add(SoundEvents.HORSE_GALLOP);
		sounds.add(SoundEvents.ENDER_DRAGON_FLAP);
		sounds.add(SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR);
		sounds.add(SoundEvents.WOODEN_DOOR_CLOSE);
		sounds.add(SoundEvents.PLAYER_SWIM);
		sounds.add(SoundEvents.PLAYER_SPLASH);
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.zombiepig.zpigangry")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.slime.small")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("minecart.base")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.ghast.fireball")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.villager.haggle")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("game.neutral.hurt.fall.small")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("game.player.hurt.fall.big")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.zombie.infect")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.blaze.breathe")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.wolf.whine")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("game.player.hurt.fall.small")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.silverfish.step")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("game.hostile.hurt.fall.big")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.irongolem.throw")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.horse.land")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.horse.skeleton.hit")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.magmacube.jump")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.chicken.say")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.villager.idle")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.horse.idle")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("random.levelup")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.wolf.death")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.skeleton.say")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("game.hostile.swim")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("creeper.primed")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("random.break")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.wolf.growl")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("step.gravel")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.horse.skeleton.idle")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("random.bow")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.wolf.shake")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.cat.meow")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.horse.hit")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.cat.purreow")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.cow.say")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("step.grass")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("step.wood")).orElse(null).value());
		sounds.add(SoundEvents.VILLAGER_NO);
		sounds.add(SoundEvents.HORSE_DEATH);
		sounds.add(SoundEvents.PIG_AMBIENT);
		sounds.add(SoundEvents.CHEST_CLOSE);
		sounds.add(SoundEvents.ZOMBIE_ATTACK_IRON_DOOR);
		sounds.add(SoundEvents.CHEST_OPEN);
		sounds.add(SoundEvents.ENDERMAN_SCREAM);
		sounds.add(SoundEvents.CREEPER_DEATH);
		sounds.add(SoundEvents.GENERIC_BIG_FALL);
		sounds.add(SoundEvents.ENDER_DRAGON_DEATH);
		sounds.add(SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR);
		sounds.add(SoundEvents.GUARDIAN_AMBIENT);
		sounds.add(SoundEvents.SAND_HIT);
		sounds.add(SoundEvents.GENERIC_HURT);
		sounds.add(SoundEvents.GHAST_SHOOT);
		sounds.add(SoundEvents.VILLAGER_YES);
		sounds.add(SoundEvents.BAT_LOOP);
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.zombiepig.zpig")).orElse(null).value());
		sounds.add(SoundEvents.CHICKEN_HURT);
		sounds.add(SoundEvents.WITHER_SHOOT);
		sounds.add(SoundEvents.STONE_STEP);
		sounds.add(SoundEvents.PLAYER_HURT);
		sounds.add(SoundEvents.ELDER_GUARDIAN_CURSE);
		sounds.add(SoundEvents.GUARDIAN_HURT_LAND);
		sounds.add(SoundEvents.ZOMBIE_DEATH);
		sounds.add(SoundEvents.HOSTILE_SPLASH);
		sounds.add(SoundEvents.GUARDIAN_AMBIENT_LAND);
		sounds.add(SoundEvents.WOODEN_DOOR_OPEN);
		sounds.add(SoundEvents.DONKEY_DEATH);
		sounds.add(SoundEvents.IRON_GOLEM_DEATH);
		sounds.add(SoundEvents.GENERIC_EXPLODE.value());
		sounds.add(SoundEvents.WOOL_STEP);
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.zombie.remedy")).orElse(null).value());
		sounds.add(SoundEvents.LIGHTNING_BOLT_THUNDER);
		sounds.add(SoundEvents.ZOMBIE_HURT);
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("mob.wolf.howl")).orElse(null).value());
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("game.hostile.hurt.fall.small")).orElse(null).value());
		sounds.add(SoundEvents.COW_HURT);
		sounds.add(SoundEvents.WOLF_SOUNDS.get(WolfSoundVariants.SoundSet.CLASSIC).hurtSound().value());
		sounds.add(SoundEvents.PISTON_CONTRACT);
		sounds.add(SoundEvents.BAT_AMBIENT);
		sounds.add(SoundEvents.SAND_STEP);
		sounds.add(SoundEvents.HOSTILE_SPLASH);
		sounds.add(SoundEvents.SPIDER_AMBIENT);
		sounds.add(SoundEvents.ANVIL_LAND);
		sounds.add(SoundEvents.ENDERMAN_DEATH);
		sounds.add(SoundEvents.GRAVEL_HIT);
		sounds.add(SoundEvents.GENERIC_SWIM);
		sounds.add(SoundEvents.DONKEY_HURT);
		sounds.add(SoundEvents.SLIME_ATTACK);
		sounds.add(SoundEvents.SHEEP_SHEAR);
		sounds.add(SoundEvents.WOOD_HIT);
		sounds.add(SoundEvents.PORTAL_TRAVEL);
		sounds.add(SoundEvents.GUARDIAN_DEATH);
		sounds.add(SoundEvents.PIG_STEP);
		sounds.add(SoundEvents.SILVERFISH_AMBIENT);
		sounds.add(SoundEvents.CAT_PURR);
		sounds.add(SoundEvents.LEVER_CLICK);
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("random.pop")).orElse(null).value());
		sounds.add(SoundEvents.FIRECHARGE_USE);
//		sounds.add(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.withDefaultNamespace("random.successful_hit")).orElse(null).value());
		sounds.add(SoundEvents.HORSE_GALLOP);
		sounds.add(SoundEvents.GHAST_AMBIENT);
		sounds.add(SoundEvents.ANVIL_BREAK);
		sounds.add(SoundEvents.BAT_HURT);
		sounds.add(SoundEvents.PIGLIN_DEATH);
		sounds.add(SoundEvents.SKELETON_HURT);
		sounds.add(SoundEvents.HORSE_ARMOR_UNEQUIP.value());
		sounds.add(SoundEvents.HORSE_ARMOR.value());
		sounds.add(SoundEvents.BLAZE_HURT);
		sounds.add(SoundEvents.WITHER_HURT);
		sounds.add(SoundEvents.GHAST_WARN);
		sounds.add(SoundEvents.SHEEP_AMBIENT);
		sounds.add(SoundEvents.CAT_HISS);
		sounds.add(SoundEvents.SHEEP_STEP);
		sounds.add(SoundEvents.WITHER_DEATH);
		sounds.add(SoundEvents.GHAST_SCREAM);
		sounds.add(SoundEvents.MINECART_INSIDE);
		sounds.add(SoundEvents.PISTON_EXTEND);
		sounds.add(SoundEvents.PLAYER_BURP);
		sounds.add(SoundEvents.SLIME_SQUISH);
		sounds.add(SoundEvents.ZOMBIE_AMBIENT);
		sounds.add(SoundEvents.ZOMBIE_HORSE_DEATH);
		sounds.add(SoundEvents.SILVERFISH_DEATH);
		sounds.add(SoundEvents.MAGMA_CUBE_SQUISH_SMALL);
		sounds.add(SoundEvents.CAT_HURT);
		sounds.add(SoundEvents.STONE_HIT);

		final SoundEvent soundEvent = sounds.get(RandomSource.create().nextInt(sounds.size()));
		if (soundEvent != null) {
			soundManager.play(SimpleSoundInstance.forUI(soundEvent, 0.5F));
		}
	}
}
