package dev.apexstudios.ghostrenderer.api.level.fake;

import dev.apexstudios.ghostrenderer.api.level.DelegatedClockManager;
import net.minecraft.core.Holder;
import net.minecraft.world.clock.ClockManager;
import net.minecraft.world.clock.WorldClock;

public class FakeClockManager implements DelegatedClockManager {
    private final ClockManager delegate;

    public FakeClockManager(ClockManager delegate) {
        this.delegate = delegate;
    }

    @Override
    public ClockManager delegate() {
        return delegate;
    }

    @Override public void setRate(Holder<WorldClock> definition, float rate) {}
}
