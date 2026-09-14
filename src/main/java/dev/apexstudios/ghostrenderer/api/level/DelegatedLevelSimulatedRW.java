package dev.apexstudios.ghostrenderer.api.level;

import net.minecraft.world.level.LevelSimulatedRW;

public interface DelegatedLevelSimulatedRW extends DelegatedLevelSimulatedReader, DelegatedLevelWriter, LevelSimulatedRW {
    @Override
    LevelSimulatedRW delegate();
}
