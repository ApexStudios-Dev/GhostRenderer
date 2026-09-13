package dev.apexstudios.ghostrenderer.api.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.ticks.ScheduledTick;
import net.minecraft.world.ticks.TickAccess;

public interface DelegatedTickAccess<T> extends TickAccess<T> {
    TickAccess<T> delegate();

    @Override
    default void schedule(ScheduledTick<T> tick) {
        delegate().schedule(tick);
    }

    @Override
    default boolean hasScheduledTick(BlockPos pos, T type) {
        return delegate().hasScheduledTick(pos, type);
    }

    @Override
    default int count() {
        return delegate().count();
    }
}
