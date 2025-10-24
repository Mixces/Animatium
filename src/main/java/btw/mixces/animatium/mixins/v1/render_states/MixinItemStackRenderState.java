package btw.mixces.animatium.mixins.v1.render_states;

import btw.mixces.animatium.util.states.ItemUtilityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemStackRenderState.class)
public abstract class MixinItemStackRenderState implements ItemUtilityRenderState {
    @Unique
    private ItemStack animatium$stack = ItemStack.EMPTY;

    @Override
    public ItemStack animatium$getItemStack() {
        return animatium$stack;
    }

    @Override
    public void animatium$setItemStack(ItemStack itemStack) {
        animatium$stack = itemStack;
    }
}
