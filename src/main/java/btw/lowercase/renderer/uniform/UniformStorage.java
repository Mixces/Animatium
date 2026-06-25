package btw.lowercase.renderer.uniform;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.system.MemoryStack;
import org.visuals.legacy.animatium.renderer.uniform.UniformKey;
import org.visuals.legacy.animatium.renderer.uniform.UniformSerializer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class UniformStorage {
    private final GpuBuffer ubo;
    private final List<UniformKey<?>> keys;
    private final Map<UniformKey<?>, Object> values;
    private final int size;

    private UniformStorage(final String name, final List<UniformKey<?>> keys, final int size) {
        this.ubo = RenderSystem.getDevice().createBuffer(() -> "Dynamic Uniform Storage (" + name + ")", GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_COPY_DST, size);
        this.keys = keys;
        this.values = new LinkedHashMap<>();
        this.size = size;
    }

    public static Builder builder(final String name) {
        return new Builder(name);
    }

    public <T> void set(final UniformKey<T> key, final T value) {
        if (!this.keys.contains(key)) {
            throw new UnsupportedOperationException("Uniform storage does not contain key '" + key.name() + "'!");
        } else {
            this.values.put(key, value);
        }
    }

    public <T> T get(final UniformKey<T> key) {
        if (!this.keys.contains(key)) {
            return null;
        } else {
            return (T) this.values.get(key);
        }
    }

    public void update() {
        final GpuBufferSlice slice = this.ubo.slice();
        try (final MemoryStack stack = MemoryStack.stackPush()) {
            final ByteBuffer buffer = stack.malloc(this.size);
            for (final Map.Entry<UniformKey<?>, Object> entry : this.values.entrySet()) {
                final UniformKey<?> key = entry.getKey();
                ((UniformSerializer<Object>) key.serializer()).put(buffer, entry.getValue());
            }

            RenderSystem.getDevice().createCommandEncoder().writeToBuffer(slice, buffer);
        }
    }

    public GpuBufferSlice slice() {
        return this.ubo.slice();
    }

    public static class Builder {
        private final String name;

        private final List<UniformKey<?>> keys = new ArrayList<>();
        private final Std140SizeCalculator calculator = new Std140SizeCalculator();

        private Builder(final String name) {
            this.name = name;
        }

        public Builder with(final UniformKey<?> key) {
            this.keys.add(key);
            key.serializer().size(this.calculator);
            return this;
        }

        public UniformStorage build() {
            return new UniformStorage(this.name, List.copyOf(this.keys), this.calculator.get());
        }
    }
}
