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

package org.visuals.legacy.animatium.util.rendering.renderer;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import org.joml.*;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public record Uniform<T>(Type<T> type, T value) {
    public void put(final Std140Builder builder) {
        this.type.put(builder, this.value);
    }

    public void size(final Std140SizeCalculator calculator) {
        this.type.size(calculator);
    }

    public abstract static class Type<T> {
        public static final Type<Integer> INT = of(Std140SizeCalculator::putInt, Std140Builder::putInt);
        public static final Type<Float> FLOAT = of(Std140SizeCalculator::putFloat, Std140Builder::putFloat);
        public static final Type<Vector2ic> VECTOR2I = of(Std140SizeCalculator::putIVec2, Std140Builder::putIVec2);
        public static final Type<Vector2fc> VECTOR2F = of(Std140SizeCalculator::putVec2, Std140Builder::putVec2);
        public static final Type<Vector3ic> VECTOR3I = of(Std140SizeCalculator::putIVec3, Std140Builder::putIVec3);
        public static final Type<Vector3fc> VECTOR3F = of(Std140SizeCalculator::putVec3, Std140Builder::putVec3);
        public static final Type<Vector4ic> VECTOR4I = of(Std140SizeCalculator::putIVec4, Std140Builder::putIVec4);
        public static final Type<Vector4fc> VECTOR4F = of(Std140SizeCalculator::putVec4, Std140Builder::putVec4);
        public static final Type<Matrix4fc> MATRIX4F = of(Std140SizeCalculator::putMat4f, Std140Builder::putMat4f);

        public static <T> Type<T> of(final Consumer<Std140SizeCalculator> sizeCalculator, final BiConsumer<Std140Builder, T> biConsumer) {
            return new Type<>() {
                @Override
                public void put(final Std140Builder builder, final T value) {
                    biConsumer.accept(builder, value);
                }

                @Override
                public void size(final Std140SizeCalculator calculator) {
                    sizeCalculator.accept(calculator);
                }
            };
        }

        public abstract void put(final Std140Builder builder, final T value);

        public abstract void size(final Std140SizeCalculator calculator);
    }
}
