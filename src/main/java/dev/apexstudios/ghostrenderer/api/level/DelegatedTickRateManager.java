package dev.apexstudios.ghostrenderer.api.level;

import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Entity;

public class DelegatedTickRateManager extends TickRateManager {
    private final TickRateManager delegate;

    public DelegatedTickRateManager(TickRateManager delegate) {
        this.delegate = delegate;
    }

    public TickRateManager delegate() {
        return delegate;
    }

    @Override
    public void setTickRate(float rate) {
        delegate().setTickRate(rate);
    }

    @Override
    public float tickrate() {
        return delegate().tickrate();
    }

    @Override
    public float millisecondsPerTick() {
        return delegate().millisecondsPerTick();
    }

    @Override
    public long nanosecondsPerTick() {
        return delegate().nanosecondsPerTick();
    }

    @Override
    public boolean runsNormally() {
        return delegate().runsNormally();
    }

    @Override
    public boolean isSteppingForward() {
        return delegate().isSteppingForward();
    }

    @Override
    public void setFrozenTicksToRun(int timeout) {
        delegate().setFrozenTicksToRun(timeout);
    }

    @Override
    public int frozenTicksToRun() {
        return delegate().frozenTicksToRun();
    }

    @Override
    public void setFrozen(boolean state) {
        delegate().setFrozen(state);
    }

    @Override
    public boolean isFrozen() {
        return delegate().isFrozen();
    }

    @Override
    public void tick() {
        delegate().tick();
    }

    @Override
    public boolean isEntityFrozen(Entity entity) {
        return delegate().isEntityFrozen(entity);
    }
}
