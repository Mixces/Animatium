package btw.mixces.animatium.mixins.v1.general.items;

import btw.mixces.animatium.util.ItemUtils;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFeatureRenderer.class)
public abstract class MixinItemFeatureRenderer {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderItem(Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II[ILjava/util/List;Lnet/minecraft/client/renderer/RenderType;Lnet/minecraft/client/renderer/item/ItemStackRenderState$FoilType;)V", ordinal = 0, shift = At.Shift.BEFORE))
    private void animatium$storeItemDisplayContext(SubmitNodeCollection submitNodeCollection, MultiBufferSource.BufferSource bufferSource, OutlineBufferSource outlineBufferSource, CallbackInfo ci, @Local SubmitNodeStorage.ItemSubmit itemSubmit) {
        ItemUtils.setDisplayContext(itemSubmit.displayContext());
    }
}
