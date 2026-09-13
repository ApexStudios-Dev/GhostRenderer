package dev.apexstudios.ghostrenderer.api.level.fake;

import dev.apexstudios.ghostrenderer.api.level.DelegatedTickRateManager;
import net.minecraft.world.TickRateManager;

public class FakeTickRateManager extends DelegatedTickRateManager {
    public FakeTickRateManager(TickRateManager delegate) {
        super(delegate);
    }

    @Override public void setTickRate(float rate) {}
    @Override public void setFrozenTicksToRun(int timeout) {}
    @Override public void setFrozen(boolean state) {}
    @Override public void tick() {}
}
