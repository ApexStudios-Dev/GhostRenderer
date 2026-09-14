package dev.apexstudios.ghostrenderer.api.level;

import net.minecraft.world.level.LevelHeightAccessor;

public interface DelegatedLevelHeightAccessor extends LevelHeightAccessor {
    LevelHeightAccessor delegate();

    @Override
    default int getHeight() {
        return delegate().getHeight();
    }

    @Override
    default int getMinY() {
        return delegate().getMinY();
    }
}
