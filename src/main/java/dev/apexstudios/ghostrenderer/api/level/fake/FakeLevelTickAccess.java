package dev.apexstudios.ghostrenderer.api.level.fake;

import dev.apexstudios.ghostrenderer.api.level.DelegatedLevelTickAccess;
import net.minecraft.world.ticks.LevelTickAccess;
import net.minecraft.world.ticks.ScheduledTick;

public class FakeLevelTickAccess<T> implements DelegatedLevelTickAccess<T> {
    private final LevelTickAccess<T> delegate;

    public FakeLevelTickAccess(LevelTickAccess<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public LevelTickAccess<T> delegate() {
        return delegate;
    }

    @Override public void schedule(ScheduledTick<T> tick) {}
}
