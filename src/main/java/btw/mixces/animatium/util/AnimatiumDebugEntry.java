package btw.mixces.animatium.util;

import btw.mixces.animatium.AnimatiumClient;
import btw.mixces.animatium.util.enums.Feature;
import net.minecraft.client.gui.components.debug.DebugEntryCategory;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AnimatiumDebugEntry implements DebugScreenEntry {
    public static final DebugEntryCategory CATEGORY = new DebugEntryCategory(Component.translatable("animatium.category.debug"), 9999.0F);
    public static final ResourceLocation GROUP = AnimatiumClient.id("debug");

    @Override
    public void display(DebugScreenDisplayer debugScreenDisplayer, @Nullable Level level, @Nullable LevelChunk levelChunk, @Nullable LevelChunk levelChunk2) {
        if (!AnimatiumClient.ENABLED_FEATURES.isEmpty()) {
            List<String> list = new ArrayList<>();
            list.add("Animatium Enabled Server Features:");
            for (Feature feature : AnimatiumClient.ENABLED_FEATURES) {
                list.add(" - " + Component.translatable(feature.getTranslateKey()).getString());
            }

            debugScreenDisplayer.addToGroup(GROUP, list);
        }
    }

    @Override
    public boolean isAllowed(boolean bl) {
        return true;
    }

    @Override
    public DebugEntryCategory category() {
        return CATEGORY;
    }
}
