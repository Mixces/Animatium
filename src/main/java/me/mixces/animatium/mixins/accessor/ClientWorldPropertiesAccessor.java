package me.mixces.animatium.mixins.accessor;

import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientWorld.Properties.class)
public interface ClientWorldPropertiesAccessor {
    @Accessor("flatWorld")
    boolean isFlatWorld();
}