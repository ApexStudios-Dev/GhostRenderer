package dev.apexstudios.ghostrenderer.core.level;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import net.minecraft.client.renderer.block.BlockAndTintGetter;

public record GhostLevelRenderState(
        Long2ObjectMap<GhostBlock> blockStates,
        BlockAndTintGetter tintGetter,
        boolean isValid
) {
    public GhostLevelRenderState {
        blockStates = Long2ObjectMaps.unmodifiable(blockStates);
    }
}
