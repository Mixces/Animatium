package btw.mixces.animatium.mixins.renderer;

import btw.mixces.animatium.LegacyGlintType;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderBuffers.class)
public abstract class MixinRenderBuffers {
    @Inject(method = "put", at = @At("HEAD"))
    private static void animatium$addLegacyGlintLayers(Object2ObjectLinkedOpenHashMap<RenderType, ByteBufferBuilder> object2ObjectLinkedOpenHashMap, RenderType renderType, CallbackInfo ci) {
        if (!object2ObjectLinkedOpenHashMap.containsKey(LegacyGlintType.getGlintTranslucentLayerA())) {
            object2ObjectLinkedOpenHashMap.put(LegacyGlintType.getGlintTranslucentLayerA(), new ByteBufferBuilder(LegacyGlintType.getGlintTranslucentLayerA().bufferSize()));
        }

//        if (!object2ObjectLinkedOpenHashMap.containsKey(LegacyGlintType.getGlintTranslucentLayerB())) {
//            object2ObjectLinkedOpenHashMap.put(LegacyGlintType.getGlintTranslucentLayerB(), new ByteBufferBuilder(LegacyGlintType.getGlintTranslucentLayerB().bufferSize()));
//        }
    }
}
