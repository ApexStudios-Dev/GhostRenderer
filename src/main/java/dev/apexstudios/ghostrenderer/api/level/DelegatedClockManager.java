package dev.apexstudios.ghostrenderer.api.level;

import net.minecraft.core.Holder;
import net.minecraft.world.clock.ClockInstance;
import net.minecraft.world.clock.ClockManager;
import net.minecraft.world.clock.WorldClock;

public interface DelegatedClockManager extends ClockManager {
    ClockManager delegate();

    @Override
    default ClockInstance getInstance(Holder<WorldClock> definition) {
        return delegate().getInstance(definition);
    }

    @Override
    default void setRate(Holder<WorldClock> definition, float rate) {
        delegate().setRate(definition, rate);
    }
}
