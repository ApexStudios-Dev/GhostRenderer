package dev.apexstudios.ghostrenderer.api.level;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;

public interface DelegatedNoiseBiomeSource extends BiomeManager.NoiseBiomeSource {
    BiomeManager.NoiseBiomeSource delegate();

    @Override
    default Holder<Biome> getNoiseBiome(final int quartX, final int quartY, final int quartZ) {
        return delegate().getNoiseBiome(quartX, quartY, quartZ);
    }
}
