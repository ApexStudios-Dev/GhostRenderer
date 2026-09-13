package dev.apexstudios.ghostrenderer.api.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.ticks.LevelTickAccess;

public interface DelegatedLevelTickAccess<T> extends DelegatedTickAccess<T>, LevelTickAccess<T> {
    @Override
    LevelTickAccess<T> delegate();

    @Override
    default boolean willTickThisTick(BlockPos pos, T type) {
        return delegate().willTickThisTick(pos, type);
    }
}
