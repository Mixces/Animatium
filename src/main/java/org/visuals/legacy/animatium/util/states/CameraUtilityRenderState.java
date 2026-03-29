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

package org.visuals.legacy.animatium.util.states;

public interface CameraUtilityRenderState {
    default float animatium$getPartialTickTime() {
        throw new UnsupportedOperationException();
    }

    default void animatium$setPartialTickTime(final float partialTickTime) {
        throw new UnsupportedOperationException();
    }

    default float animatium$getOldEyeHeight() {
        throw new UnsupportedOperationException();
    }

    default void animatium$setOldEyeHeight(final float oldEyeHeight) {
        throw new UnsupportedOperationException();
    }

    default float animatium$getEyeHeight() {
        throw new UnsupportedOperationException();
    }

    default void animatium$setEyeHeight(final float eyeHeight) {
        throw new UnsupportedOperationException();
    }

    default float animatium$getYRot() {
        throw new UnsupportedOperationException();
    }

    default void animatium$setYRot(final float yRot) {
        throw new UnsupportedOperationException();
    }

    default float animatium$getXRot() {
        throw new UnsupportedOperationException();
    }

    default void animatium$setXRot(final float xRot) {
        throw new UnsupportedOperationException();
    }
}
