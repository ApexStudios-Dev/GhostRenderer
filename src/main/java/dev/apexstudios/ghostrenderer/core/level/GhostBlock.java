package dev.apexstudios.ghostrenderer.core.level;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public record GhostBlock(
        BlockState blockState,
        boolean isValid
) {
    public static final GhostBlock AIR = new GhostBlock(Blocks.AIR.defaultBlockState(), true);
}
