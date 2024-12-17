package me.mixces.animatium.mixins.screen.widget;

import me.mixces.animatium.config.AnimatiumConfig;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(SliderWidget.class)
public abstract class MixinSliderWidget extends ClickableWidget {
    public MixinSliderWidget(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    @ModifyConstant(method = "renderWidget", constant = @Constant(intValue = 0xFFFFFF))
    private int renderWidget$old$textColor(int constant) {
        if (AnimatiumConfig.oldButtonTextColors) {
            return !active ? 0xE0E0E0 : (isSelected() ? 0xFFFFA0 : 0xE0E0E0);
        } else {
            return constant;
        }
    }
}
