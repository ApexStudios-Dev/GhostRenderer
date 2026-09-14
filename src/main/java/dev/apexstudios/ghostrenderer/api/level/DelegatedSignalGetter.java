package dev.apexstudios.ghostrenderer.api.level;

import net.minecraft.world.level.SignalGetter;

public interface DelegatedSignalGetter extends DelegatedBlockGetter, SignalGetter {
    @Override
    SignalGetter delegate();
}
