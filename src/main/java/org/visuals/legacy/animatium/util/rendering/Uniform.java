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
