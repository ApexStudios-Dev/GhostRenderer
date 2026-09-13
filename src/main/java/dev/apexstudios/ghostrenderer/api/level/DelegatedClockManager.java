package dev.apexstudios.ghostrenderer.api.level;

import net.minecraft.core.Holder;
import net.minecraft.world.clock.ClockManager;
import net.minecraft.world.clock.WorldClock;

public interface DelegatedClockManager extends ClockManager {
    ClockManager delegate();

    @Override
    default long getTotalTicks(Holder<WorldClock> definition) {
        return delegate().getTotalTicks(definition);
    }
}
