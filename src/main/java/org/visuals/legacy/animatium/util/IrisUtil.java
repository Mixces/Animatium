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

import com.mojang.blaze3d.pipeline.RenderPipeline;

import java.lang.reflect.Method;

public final class IrisUtil {
    private static Object IRIS_INSTANCE = null;
    private static Method IRIS_ASSIGN_PIPELINE_METHOD = null;

    private static Enum<?> IRIS_PROGRAM_BASIC = null;
    private static Enum<?> IRIS_PROGRAM_TEXTURED = null;
    private static Enum<?> IRIS_PROGRAM_TERRAIN = null;
    private static Enum<?> IRIS_PROGRAM_TERRAIN_SOLID = null;
    private static Enum<?> IRIS_PROGRAM_TERRAIN_CUTOUT = null;
    private static Enum<?> IRIS_PROGRAM_TRANSLUCENT = null;
    private static Enum<?> IRIS_PROGRAM_SKY_BASIC = null;
    private static Enum<?> IRIS_PROGRAM_SKY_TEXTURED = null;
    private static Enum<?> IRIS_PROGRAM_ARMOR_GLINT = null;
    private static Enum<?> IRIS_PROGRAM_ENTITIES = null;
    private static Enum<?> IRIS_PROGRAM_ENTITIES_TRANSLUCENT = null;
    private static Enum<?> IRIS_PROGRAM_CLOUDS = null;
    private static Enum<?> IRIS_PROGRAM_BLOCK = null;
    private static Enum<?> IRIS_PROGRAM_BLOCK_TRANSLUCENT = null;
    private static Enum<?> IRIS_PROGRAM_HAND = null;
    private static Enum<?> IRIS_PROGRAM_HAND_TRANSLUCENT = null;
    private static Enum<?> IRIS_PROGRAM_PARTICLES = null;
    private static Enum<?> IRIS_PROGRAM_PARTICLES_TRANSLUCENT = null;
    private static Enum<?> IRIS_PROGRAM_EMISSIVE_ENTITIES = null;
    private static Enum<?> IRIS_PROGRAM_BEACON_BEAM = null;
    private static Enum<?> IRIS_PROGRAM_LINES = null;

    private IrisUtil() {
    }

    private static void initialize() {
        try {
            // Instance/API
            Class<?> irisApiClass = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
            IRIS_INSTANCE = irisApiClass.getMethod("getInstance").invoke(null);
            Class<?> irisInstanceClass = IRIS_INSTANCE.getClass();

            // Enum
            Class<?> irisProgramEnum = Class.forName("net.irisshaders.iris.api.v0.IrisProgram");
            IRIS_PROGRAM_BASIC = Enum.valueOf(irisProgramEnum.asSubclass(Enum.class), "BASIC");
            IRIS_PROGRAM_TEXTURED = Enum.valueOf(irisProgramEnum.asSubclass(Enum.class), "TEXTURED");
            IRIS_PROGRAM_TERRAIN = Enum.valueOf(irisProgramEnum.asSubclass(Enum.class), "TERRAIN");
            IRIS_PROGRAM_TERRAIN_SOLID = Enum.valueOf(irisProgramEnum.asSubclass(Enum.class), "TERRAIN_SOLID");
            IRIS_PROGRAM_TERRAIN_CUTOUT = Enum.valueOf(irisProgramEnum.asSubclass(Enum.class), "TERRAIN_CUTOUT");
            IRIS_PROGRAM_TRANSLUCENT = Enum.valueOf(irisProgramEnum.asSubclass(Enum.class), "TRANSLUCENT");
            IRIS_PROGRAM_SKY_BASIC = Enum.valueOf(irisProgramEnum.asSubclass(Enum.class), "SKY_BASIC");
            IRIS_PROGRAM_SKY_TEXTURED = Enum.valueOf(irisProgramEnum.asSubclass(Enum.class), "SKY_TEXTURED");
            IRIS_PROGRAM_ARMOR_GLINT = Enum.valueOf(irisProgramEnum.asSubclass(Enum.class), "ARMOR_GLINT");
            IRIS_PROGRAM_ENTITIES = Enum.valueOf(irisProgramEnum.asSubclass(Enum.class), "ENTITIES");
            IRIS_PROGRAM_ENTITIES_TRANSLUCENT = Enum.valueOf(irisProgramEnum.asSubclass(Enum.class), "ENTITIES_TRANSLUCENT");
            IRIS_PROGRAM_CLOUDS = Enum.valueOf(irisProgramEnum.asSubclass(Enum.class), "CLOUDS");
            IRIS_PROGRAM_BLOCK = Enum.valueOf(irisProgramEnum.asSubclass(Enum.class), "BLOCK");
            IRIS_PROGRAM_BLOCK_TRANSLUCENT = Enum.valueOf(irisProgramEnum.asSubclass(Enum.class), "BLOCK_TRANSLUCENT");
            IRIS_PROGRAM_HAND = Enum.valueOf(irisProgramEnum.asSubclass(Enum.class), "HAND");
            IRIS_PROGRAM_HAND_TRANSLUCENT = Enum.valueOf(irisProgramEnum.asSubclass(Enum.class), "HAND_TRANSLUCENT");
            IRIS_PROGRAM_PARTICLES = Enum.valueOf(irisProgramEnum.asSubclass(Enum.class), "PARTICLES");
            IRIS_PROGRAM_PARTICLES_TRANSLUCENT = Enum.valueOf(irisProgramEnum.asSubclass(Enum.class), "PARTICLES_TRANSLUCENT");
            IRIS_PROGRAM_EMISSIVE_ENTITIES = Enum.valueOf(irisProgramEnum.asSubclass(Enum.class), "EMISSIVE_ENTITIES");
            IRIS_PROGRAM_BEACON_BEAM = Enum.valueOf(irisProgramEnum.asSubclass(Enum.class), "BEACON_BEAM");
            IRIS_PROGRAM_LINES = Enum.valueOf(irisProgramEnum.asSubclass(Enum.class), "LINES");

            // Methods
            IRIS_ASSIGN_PIPELINE_METHOD = irisInstanceClass.getMethod("assignPipeline", RenderPipeline.class, irisProgramEnum);
        } catch (Exception ignored) {
        }
    }

    public static void assignPipeline(RenderPipeline pipeline, Enum<?> enumValue) {
        if (IRIS_ASSIGN_PIPELINE_METHOD != null) {
            try {
                IRIS_ASSIGN_PIPELINE_METHOD.invoke(IRIS_INSTANCE, pipeline, enumValue);
            } catch (Exception ignored) {
            }
        }
    }

    public static void assignPipeline(Enum<?> enumValue, RenderPipeline... pipelines) {
        for (RenderPipeline pipeline : pipelines) {
            assignPipeline(pipeline, enumValue);
        }
    }

    public static Enum<?> basic() {
        return IRIS_PROGRAM_BASIC;
    }

    public static Enum<?> textured() {
        return IRIS_PROGRAM_TEXTURED;
    }

    public static Enum<?> terrain() {
        return IRIS_PROGRAM_TERRAIN;
    }

    public static Enum<?> terrainSolid() {
        return IRIS_PROGRAM_TERRAIN_SOLID;
    }

    public static Enum<?> terrainCutout() {
        return IRIS_PROGRAM_TERRAIN_CUTOUT;
    }

    public static Enum<?> translucent() {
        return IRIS_PROGRAM_TRANSLUCENT;
    }

    public static Enum<?> skyBasic() {
        return IRIS_PROGRAM_SKY_BASIC;
    }

    public static Enum<?> skyTextured() {
        return IRIS_PROGRAM_SKY_TEXTURED;
    }

    public static Enum<?> armorGlint() {
        return IRIS_PROGRAM_ARMOR_GLINT;
    }

    public static Enum<?> entities() {
        return IRIS_PROGRAM_ENTITIES;
    }

    public static Enum<?> entitiesTranslucent() {
        return IRIS_PROGRAM_ENTITIES_TRANSLUCENT;
    }

    public static Enum<?> clouds() {
        return IRIS_PROGRAM_CLOUDS;
    }

    public static Enum<?> block() {
        return IRIS_PROGRAM_BLOCK;
    }

    public static Enum<?> blockTranslucent() {
        return IRIS_PROGRAM_BLOCK_TRANSLUCENT;
    }

    public static Enum<?> hand() {
        return IRIS_PROGRAM_HAND;
    }

    public static Enum<?> handTranslucent() {
        return IRIS_PROGRAM_HAND_TRANSLUCENT;
    }

    public static Enum<?> particles() {
        return IRIS_PROGRAM_PARTICLES;
    }

    public static Enum<?> particlesTranslucent() {
        return IRIS_PROGRAM_PARTICLES_TRANSLUCENT;
    }

    public static Enum<?> emissiveEntities() {
        return IRIS_PROGRAM_EMISSIVE_ENTITIES;
    }

    public static Enum<?> beaconBeam() {
        return IRIS_PROGRAM_BEACON_BEAM;
    }

    public static Enum<?> lines() {
        return IRIS_PROGRAM_LINES;
    }

    static {
        initialize();
    }
}
