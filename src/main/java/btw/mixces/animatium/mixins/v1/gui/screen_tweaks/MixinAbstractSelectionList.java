package btw.mixces.animatium.mixins.v1.gui.screen_tweaks;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.network.chat.CommonComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSelectionList.class)
public abstract class MixinAbstractSelectionList extends AbstractContainerWidget {
    public MixinAbstractSelectionList(int width, int height, int y, int itemHeight) {
        super(0, y, width, height, CommonComponents.EMPTY);
    }

    @Inject(method = "renderListBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V", shift = At.Shift.AFTER))
    private void animatium$renderListBackgroundGradient(GuiGraphics guiGraphics, CallbackInfo ci) {
        if (AnimatiumClient.ENABLED && AnimatiumConfig.instance().screen.listBackgroundGradient) {
            guiGraphics.fillGradient(this.getX(), this.getY(), this.getRight(), this.getY() + 4, -16777216, 0);
            guiGraphics.fillGradient(this.getX(), this.getBottom() - 4, this.getRight(), this.getBottom(), 0, -16777216);
        }
    }
}
