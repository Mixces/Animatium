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

import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.*;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.Nullable;
import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.visuals.legacy.animatium.mixins.accessor.GameRendererAccessor;
import org.visuals.legacy.animatium.mixins.accessor.GuiRendererAccessor;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Consumer;

public class ImmediateRenderer implements AutoCloseable {
	// Data
	private final Map<String, GpuTextureView> textures;
	private final Map<String, Uniform> uniforms;

	@Getter
	@Setter
	private String displayName;
	@Getter
	@Setter
	private RenderPipeline pipeline;
	@Getter
	@Setter
	private Vector4i viewport;
	@Getter
	@Setter
	private DynamicTransforms dynamicTransforms;

	// Internal
	private GpuBuffer vertexBuffer;
	private GpuBuffer indexBuffer;
	private VertexFormat.IndexType indexType;
	private int indexCount;
	private GpuBuffer uniformBuffer;
	@Getter
	private boolean setup;

	private ImmediateRenderer(final String displayName) {
		// Data
		this.textures = new HashMap<>();
		this.uniforms = new HashMap<>();
		this.displayName = displayName;
		this.pipeline = null;
		this.viewport = null;
		this.dynamicTransforms = new DynamicTransforms(null, null, new Vector4f(1.0F), new Vector3f());

		// Internal
		this.vertexBuffer = null;
		this.indexBuffer = null;
		this.indexType = null;
		this.indexCount = -1;
		this.uniformBuffer = null;
		this.setup = false;
	}

	public static ImmediateRenderer of(final String displayName) {
		return new ImmediateRenderer(displayName);
	}

	public void setup(final GpuBuffer vertexBuffer, final GpuBuffer indexBuffer, final VertexFormat.IndexType type, final int indexCount) {
		this.vertexBuffer = vertexBuffer;
		this.indexBuffer = indexBuffer;
		this.indexType = type;
		this.indexCount = indexCount;
		this.setup = true;
	}

	public void setup(final MeshData meshData) {
		if (this.pipeline == null) {
			throw new RuntimeException("Cannot create mesh data without a pipeline bound!");
		} else {
			final int indexCount = meshData.drawState().indexCount();
			final GpuBuffer vertexBuffer = this.pipeline.getVertexFormat().uploadImmediateVertexBuffer(meshData.vertexBuffer());
			GpuBuffer indexBuffer;
			VertexFormat.IndexType indexType;
			if (meshData.indexBuffer() == null) {
				final RenderSystem.AutoStorageIndexBuffer autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(meshData.drawState().mode());
				indexBuffer = autoStorageIndexBuffer.getBuffer(indexCount);
				indexType = autoStorageIndexBuffer.type();
			} else {
				indexBuffer = this.pipeline.getVertexFormat().uploadImmediateIndexBuffer(meshData.indexBuffer());
				indexType = meshData.drawState().indexType();
			}

			this.setup(vertexBuffer, indexBuffer, indexType, indexCount);
		}
	}

	public void setup(final Consumer<VertexConsumer> renderConsumer, final int vertexCount) {
		if (this.pipeline == null) {
			throw new RuntimeException("Cannot create mesh data without a pipeline bound!");
		} else {
			try (final ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(this.pipeline.getVertexFormat().getVertexSize() * vertexCount)) {
				final BufferBuilder builder = new BufferBuilder(byteBufferBuilder, this.pipeline.getVertexFormatMode(), this.pipeline.getVertexFormat());
				renderConsumer.accept(builder);
				this.setup(builder.buildOrThrow());
			}
		}
	}

	public void setTexture(int id, GpuTextureView textureView) {
		this.textures.put("Sampler" + id, textureView);
	}

	public void setTexture(int id, ResourceLocation resourceLocation) {
		this.setTexture(id, Minecraft.getInstance().getTextureManager().getTexture(resourceLocation).getTextureView());
	}

	public void setTextures(TextureSetup textureSetup) {
		final GpuTextureView texture0 = textureSetup.texure0();
		if (texture0 != null) {
			this.setTexture(0, texture0);
		}

		final GpuTextureView texture1 = textureSetup.texure1();
		if (texture1 != null) {
			this.setTexture(1, texture1);
		}

		final GpuTextureView texture2 = textureSetup.texure2();
		if (texture2 != null) {
			this.setTexture(2, texture2);
		}
	}

	public void setUniform(String name, int value) {
		this.uniforms.put(name, new Uniform<>(Uniform.Type.INT, value));
	}

	public void setUniform(String name, int... value) {
		this.uniforms.put(name, new Uniform<>(Uniform.Type.INT_ARRAY, value));
	}

	public void setUniform(String name, float value) {
		this.uniforms.put(name, new Uniform<>(Uniform.Type.FLOAT, value));
	}

	public void setUniform(String name, float... value) {
		this.uniforms.put(name, new Uniform<>(Uniform.Type.FLOAT_ARRAY, value));
	}

	public void setUniform(String name, Vector2ic value) {
		this.uniforms.put(name, new Uniform<>(Uniform.Type.VECTOR2I, value));
	}

	public void setUniform(String name, Vector2fc value) {
		this.uniforms.put(name, new Uniform<>(Uniform.Type.VECTOR2F, value));
	}

	public void setUniform(String name, Vector3ic value) {
		this.uniforms.put(name, new Uniform<>(Uniform.Type.VECTOR3I, value));
	}

	public void setUniform(String name, Vector3fc value) {
		this.uniforms.put(name, new Uniform<>(Uniform.Type.VECTOR3F, value));
	}

	public void setUniform(String name, Vector4ic value) {
		this.uniforms.put(name, new Uniform<>(Uniform.Type.VECTOR4I, value));
	}

	public void setUniform(String name, Vector4fc value) {
		this.uniforms.put(name, new Uniform<>(Uniform.Type.VECTOR4F, value));
	}

	public void setUniform(String name, Matrix4fc value) {
		this.uniforms.put(name, new Uniform<>(Uniform.Type.MATRIX4F, value));
	}

	public void drawTo(final RenderTarget renderTarget) {
		if (!this.setup) {
			throw new RuntimeException("Cannot draw because renderer has not been setup yet!");
		} else if (this.pipeline == null) {
			throw new RuntimeException("Cannot draw without a pipeline bound!");
		} else {
			final GpuTextureView colorTextureView = RenderSystem.outputColorTextureOverride != null ? RenderSystem.outputColorTextureOverride : renderTarget.getColorTextureView();
			final GpuTextureView depthTextureView = renderTarget.useDepth ? (RenderSystem.outputDepthTextureOverride != null ? RenderSystem.outputDepthTextureOverride : renderTarget.getDepthTextureView()) : null;
			final GpuBufferSlice transforms = this.dynamicTransforms.buffer();
			final GpuBufferSlice uniformData = this.setupUniformsBuffer();
			try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> this.displayName, colorTextureView, OptionalInt.empty(), depthTextureView, OptionalDouble.empty())) {
				IntBuffer viewportBuffer = null;
				if (this.viewport != null) {
					viewportBuffer = BufferUtils.createIntBuffer(4);
					GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewportBuffer);
					GlStateManager._viewport(this.viewport.x, this.viewport.y, this.viewport.z, this.viewport.w);
				}

				renderPass.setPipeline(this.pipeline);
				renderPass.setVertexBuffer(0, this.vertexBuffer);
				renderPass.setIndexBuffer(this.indexBuffer, this.indexType);
				for (Map.Entry<String, GpuTextureView> entry : this.textures.entrySet()) {
					renderPass.bindSampler(entry.getKey(), entry.getValue());
				}

				RenderSystem.bindDefaultUniforms(renderPass);
				renderPass.setUniform("DynamicTransforms", transforms);
				if (uniformData != null) {
					renderPass.setUniform("Data", uniformData);
				}

				renderPass.drawIndexed(0, 0, this.indexCount, 1);
				if (viewportBuffer != null) {
					GlStateManager._viewport(viewportBuffer.get(), viewportBuffer.get(), viewportBuffer.get(), viewportBuffer.get());
				}
			}
		}
	}

	public void draw() {
		drawTo(Minecraft.getInstance().getMainRenderTarget());
	}

	public void drawGuiTo(final RenderTarget renderTarget) {
		final Minecraft minecraft = Minecraft.getInstance();
		final Window window = minecraft.getWindow();
		final GuiRendererAccessor guiRendererAccessor = (GuiRendererAccessor) ((GameRendererAccessor) minecraft.gameRenderer).animatium$getGuiRenderer();
		RenderSystem.backupProjectionMatrix();
		RenderSystem.setProjectionMatrix(guiRendererAccessor.animatium$orthoMatrixBuffer().getBuffer((float) window.getWidth() / (float) window.getGuiScale(), (float) window.getHeight() / (float) window.getGuiScale()), ProjectionType.ORTHOGRAPHIC);
		this.setDynamicTransforms(this.dynamicTransforms.withModelViewMatrix(new Matrix4f(this.dynamicTransforms.getModelViewMatrix()).setTranslation(0.0F, 0.0F, -11000.0F)));
		this.drawTo(renderTarget);
		RenderSystem.restoreProjectionMatrix();
	}

	public void drawGui() {
		drawGuiTo(Minecraft.getInstance().getMainRenderTarget());
	}

	private @Nullable GpuBufferSlice setupUniformsBuffer() {
		if (this.uniforms.isEmpty()) {
			return null;
		} else {
			int size = 0;
			for (Uniform uniform : this.uniforms.values()) {
				size += uniform.size();
			}

			try (MemoryStack stack = MemoryStack.stackPush()) {
				final Std140Builder builder = Std140Builder.onStack(stack, size);
				for (Uniform uniform : this.uniforms.values()) {
					switch (uniform.type) {
						case INT -> builder.putInt((int) uniform.value);
						case INT_ARRAY -> {
							int[] array = (int[]) uniform.value;
							for (int value : array) {
								builder.putFloat(value);
							}
						}

						case FLOAT -> builder.putFloat((float) uniform.value);
						case FLOAT_ARRAY -> {
							float[] array = (float[]) uniform.value;
							for (float value : array) {
								builder.putFloat(value);
							}
						}

						case VECTOR2I -> builder.putIVec2((Vector2ic) uniform.value);
						case VECTOR2F -> builder.putVec2((Vector2fc) uniform.value);
						case VECTOR3I -> builder.putIVec3((Vector3ic) uniform.value);
						case VECTOR3F -> builder.putVec3((Vector3fc) uniform.value);
						case VECTOR4I -> builder.putIVec4((Vector4ic) uniform.value);
						case VECTOR4F -> builder.putVec4((Vector4fc) uniform.value);
						case MATRIX4F -> builder.putMat4f((Matrix4fc) uniform.value);
					}
				}

				if (this.uniformBuffer != null) {
					RenderSystem.getDevice().createCommandEncoder().writeToBuffer(this.uniformBuffer.slice(), builder.get());
				} else {
					this.uniformBuffer = RenderSystem.getDevice().createBuffer(() -> this.displayName + " Uniform Buffer", GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_COPY_DST, builder.get());
				}
			}

			return this.uniformBuffer.slice(0, size);
		}
	}

	@Override
	public void close() {
		this.textures.clear();
		this.uniforms.clear();
		if (this.vertexBuffer != null) {
			this.vertexBuffer = null;
		}

		if (this.indexBuffer != null) {
			this.indexBuffer = null;
		}

		if (this.uniformBuffer != null) {
			this.uniformBuffer = null;
		}
	}

	public record DynamicTransforms(
			@Nullable Matrix4f modelViewMatrix,
			@Nullable Matrix4f textureMatrix,
			Vector4f shaderColor,
			Vector3f modelOffset
	) {
		public DynamicTransforms withModelViewMatrix(Matrix4f matrix4f) {
			return new DynamicTransforms(matrix4f, this.textureMatrix, this.shaderColor, this.modelOffset);
		}

		public DynamicTransforms withTextureMatrix(Matrix4f matrix4f) {
			return new DynamicTransforms(this.modelViewMatrix, matrix4f, this.shaderColor, this.modelOffset);
		}

		public DynamicTransforms withShaderColor(Vector4f vector4f) {
			return new DynamicTransforms(this.modelViewMatrix, this.textureMatrix, vector4f, this.modelOffset);
		}

		public DynamicTransforms withShaderColor(float red, float green, float blue, float alpha) {
			return this.withShaderColor(new Vector4f(red, green, blue, alpha));
		}

		public DynamicTransforms withShaderColor(float red, float green, float blue) {
			return this.withShaderColor(red, green, blue, 1.0F);
		}

		public DynamicTransforms withShaderColor(int color) {
			return this.withShaderColor(ARGB.redFloat(color), ARGB.greenFloat(color), ARGB.blueFloat(color), ARGB.alphaFloat(color));
		}

		public DynamicTransforms withModelOffset(Vector3f vector3f) {
			return new DynamicTransforms(this.modelViewMatrix, this.textureMatrix, this.shaderColor, vector3f);
		}

		public Matrix4f getModelViewMatrix() {
			return this.modelViewMatrix == null ? new Matrix4f(RenderSystem.getModelViewMatrix()) : this.modelViewMatrix;
		}

		public Matrix4f getTextureMatrix() {
			return this.textureMatrix == null ? new Matrix4f(RenderSystem.getTextureMatrix()) : this.textureMatrix;
		}

		public GpuBufferSlice buffer() {
			return RenderSystem.getDynamicUniforms().writeTransform(
					this.getModelViewMatrix(), this.shaderColor(),
					this.modelOffset(), this.getTextureMatrix(),
					RenderUtils.getLineState().get(RenderSystem.getShaderLineWidth())
			);
		}
	}

	private record Uniform<T>(Type type, T value) {
		public int size() {
			final Std140SizeCalculator calculator = new Std140SizeCalculator();
			switch (this.type) {
				case INT -> calculator.putInt();
				case INT_ARRAY -> {
					int[] array = (int[]) this.value;
					for (int ignored : array) {
						calculator.putInt();
					}
				}

				case FLOAT -> calculator.putFloat();
				case FLOAT_ARRAY -> {
					float[] array = (float[]) this.value;
					for (float ignored : array) {
						calculator.putFloat();
					}
				}

				case VECTOR2I -> calculator.putIVec2();
				case VECTOR2F -> calculator.putVec2();
				case VECTOR3I -> calculator.putIVec3();
				case VECTOR3F -> calculator.putVec3();
				case VECTOR4I -> calculator.putIVec4();
				case VECTOR4F -> calculator.putVec4();
				case MATRIX4F -> calculator.putMat4f();
			}

			return calculator.get();
		}

		enum Type {
			INT,
			INT_ARRAY,
			FLOAT,
			FLOAT_ARRAY,
			VECTOR2I,
			VECTOR2F,
			VECTOR3I,
			VECTOR4I,
			VECTOR3F,
			VECTOR4F,
			MATRIX4F
		}
	}
}
