package dev.apexstudios.ghostrenderer.api.level;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.ColorResolver;

public interface DelegatedBlockAndTintGetter extends DelegatedBlockAndLightGetter, BlockAndTintGetter {
    @Override
    BlockAndTintGetter delegate();

    @Override
    default CardinalLighting cardinalLighting() {
        return delegate().cardinalLighting();
    }

    @Override
    default int getBlockTint(BlockPos pos, ColorResolver color) {
        return delegate().getBlockTint(pos, color);
    }
}
