package btw.mixces.animatium.config.category;

import btw.mixces.animatium.config.AnimatiumConfig;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class FixesConfigCategory {
    public static ConfigCategory setup(AnimatiumConfig defaults, AnimatiumConfig config) {
        ConfigCategory.Builder category = ConfigCategory.createBuilder();
        category.name(Component.translatable("animatium.category.fixes"));
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.fixMirrorArmSwing"))
                .description(OptionDescription.of(Component.translatable("animatium.fixMirrorArmSwing.description")))
                .binding(
                        defaults.fixMirrorArmSwing,
                        () -> config.fixMirrorArmSwing,
                        (newVal) -> config.fixMirrorArmSwing = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.upMinPixelTransparencyLimit"))
                .description(OptionDescription.of(Component.translatable("animatium.upMinPixelTransparencyLimit.description")))
                .binding(
                        defaults.upMinPixelTransparencyLimit,
                        () -> config.upMinPixelTransparencyLimit,
                        (newVal) -> {
                            config.upMinPixelTransparencyLimit = newVal;
                            Minecraft.getInstance().reloadResourcePacks();
                        })
                .controller(TickBoxControllerBuilder::create)
                .build());
        category.option(Option.<Boolean>createBuilder()
                .name(Component.translatable("animatium.fixOffHandUsingPose"))
                .description(OptionDescription.of(Component.translatable("animatium.fixOffHandUsingPose.description")))
                .binding(
                        defaults.fixOffHandUsingPose,
                        () -> config.fixOffHandUsingPose,
                        (newVal) -> config.fixOffHandUsingPose = newVal)
                .controller(TickBoxControllerBuilder::create)
                .build());
        return category.build();
    }
}
