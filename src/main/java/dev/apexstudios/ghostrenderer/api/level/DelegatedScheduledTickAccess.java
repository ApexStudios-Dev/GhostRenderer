package dev.apexstudios.ghostrenderer.api.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.ticks.LevelTickAccess;
import net.minecraft.world.ticks.ScheduledTick;
import net.minecraft.world.ticks.TickPriority;

public interface DelegatedScheduledTickAccess extends ScheduledTickAccess {
    ScheduledTickAccess delegate();

    @Override
    default <T> ScheduledTick<T> createTick(BlockPos pos, T type, int tickDelay, TickPriority priority) {
        return delegate().createTick(pos, type, tickDelay, priority);
    }

    @Override
    default <T> ScheduledTick<T> createTick(BlockPos pos, T type, int tickDelay) {
        return delegate().createTick(pos, type, tickDelay);
    }

    @Override
    default LevelTickAccess<Block> getBlockTicks() {
        return delegate().getBlockTicks();
    }

    @Override
    default LevelTickAccess<Fluid> getFluidTicks() {
        return delegate().getFluidTicks();
    }
}
