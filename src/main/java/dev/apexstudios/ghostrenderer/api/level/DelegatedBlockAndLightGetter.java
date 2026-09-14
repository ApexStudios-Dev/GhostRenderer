package dev.apexstudios.ghostrenderer.api.level;

import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.lighting.LevelLightEngine;

public interface DelegatedBlockAndLightGetter extends DelegatedBlockGetter, BlockAndLightGetter {
    @Override
    BlockAndLightGetter delegate();

    @Override
    default LevelLightEngine getLightEngine() {
        return delegate().getLightEngine();
    }
}
