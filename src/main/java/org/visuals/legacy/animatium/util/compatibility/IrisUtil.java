/**
 * Animatium
 * The all-you-could-want legacy animations mod for modern minecraft versions.
 * Brings back animations from the 1.7/1.8 era and more.
 * <p>
 * Copyright (C) 2024-2027 lowercasebtw
 * Copyright (C) 2024-2027 mixces
 * Copyright (C) 2024-2027 Contributors to the project retain their copyright
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

package org.visuals.legacy.animatium.util.compatibility;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public final class IrisUtil {
    private static final Map<RenderPipeline, IrisPipeline> pipelineCache = new Object2ObjectOpenHashMap<>();
    private static Object IRIS_INSTANCE = null;
    private static Method IRIS_ASSIGN_PIPELINE_METHOD = null;

    static {
        try {
            // API
            final Class<?> irisApiClass = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
            IRIS_INSTANCE = irisApiClass.getMethod("getInstance").invoke(null);

            // Enums
            @SuppressWarnings("rawtypes") final Class<? extends Enum> irisProgramEnum = Class.forName("net.irisshaders.iris.api.v0.IrisProgram").asSubclass(Enum.class);
            Arrays.stream(IrisPipeline.VALUES).forEach((program) -> program.initialize(irisProgramEnum));

            // Methods
            IRIS_ASSIGN_PIPELINE_METHOD = IRIS_INSTANCE.getClass().getMethod("assignPipeline", RenderPipeline.class, irisProgramEnum);
        } catch (Exception ignored) {
        }
    }

    public static void assignPipeline(final RenderPipeline pipeline, final IrisPipeline program) {
        try {
            if (pipelineCache.containsKey(pipeline)) {
                final IrisPipeline irisPipeline = pipelineCache.get(pipeline);
                if (irisPipeline == program) {
                    return;
                }
            }

            pipelineCache.put(pipeline, program);
            IRIS_ASSIGN_PIPELINE_METHOD.invoke(IRIS_INSTANCE, pipeline, program.internal());
        } catch (final Exception ignored) {
        }
    }
}
