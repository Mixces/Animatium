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

package org.visuals.legacy.animatium.util.rendering;

import com.mojang.blaze3d.buffers.Std140SizeCalculator;

import java.util.function.BiConsumer;

public class Uniform<T> {
    private final Type type;
    private final T value;
    private final int size;

    public Uniform(final Type type, final T value) {
        this.type = type;
        this.value = value;
        this.size = calculateSize();
    }

    private int calculateSize() {
        final Std140SizeCalculator calculator = new Std140SizeCalculator();
        if (this.type == Type.INT_ARRAY) {
            this.type.put(calculator, ((int[]) this.value).length);
        } else if (this.type == Type.FLOAT_ARRAY) {
            this.type.put(calculator, ((float[]) this.value).length);
        } else {
            this.type.put(calculator);
        }

        return calculator.get();
    }

    public Type type() {
        return this.type;
    }

    public T value() {
        return this.value;
    }

    public int size() {
        return this.size;
    }

    public enum Type {
        INT((calculator, _) -> calculator.putInt()),
        INT_ARRAY((calculator, size) -> {
            for (int i = 0; i < size; ++i) {
                calculator.putInt();
            }
        }),
        FLOAT((calculator, _) -> calculator.putFloat()),
        FLOAT_ARRAY((calculator, size) -> {
            for (int i = 0; i < size; ++i) {
                calculator.putFloat();
            }
        }),
        VECTOR2I((calculator, _) -> calculator.putIVec2()),
        VECTOR2F((calculator, _) -> calculator.putVec2()),
        VECTOR3I((calculator, _) -> calculator.putIVec3()),
        VECTOR4I((calculator, _) -> calculator.putVec3()),
        VECTOR3F((calculator, _) -> calculator.putIVec4()),
        VECTOR4F((calculator, _) -> calculator.putVec4()),
        MATRIX4F((calculator, _) -> calculator.putMat4f());

        private final BiConsumer<Std140SizeCalculator, Integer> calculator;

        Type(final BiConsumer<Std140SizeCalculator, Integer> calculator) {
            this.calculator = calculator;
        }

        public void put(final Std140SizeCalculator calculator, final int size) {
            this.calculator.accept(calculator, size);
        }

        public void put(final Std140SizeCalculator calculator) {
            this.put(calculator, 0);
        }
    }
}
