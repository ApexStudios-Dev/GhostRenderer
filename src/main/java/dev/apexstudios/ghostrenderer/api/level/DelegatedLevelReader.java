package dev.apexstudios.ghostrenderer.api.level;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.attribute.EnvironmentAttributeReader;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.common.extensions.ILevelReaderExtension;
import org.jspecify.annotations.Nullable;

public interface DelegatedLevelReader extends DelegatedBlockAndLightGetter, DelegatedCollisionGetter, DelegatedSignalGetter, DelegatedNoiseBiomeSource, ILevelReaderExtension, LevelReader {
    @Override
    LevelReader delegate();

    @Override
    default @Nullable ChunkAccess getChunk(int chunkX, int chunkZ, ChunkStatus targetStatus, boolean loadOrGenerate) {
        return delegate().getChunk(chunkX, chunkZ, targetStatus, loadOrGenerate);
    }

    @Override
    default boolean hasChunk(int chunkX, int chunkZ) {
        return delegate().hasChunk(chunkX, chunkZ);
    }

    @Override
    default int getHeight(Heightmap.Types type, int x, int z) {
        return delegate().getHeight(type, x, z);
    }

    @Override
    default int getSkyDarken() {
        return delegate().getSkyDarken();
    }

    @Override
    default BiomeManager getBiomeManager() {
        return delegate().getBiomeManager();
    }

    @Override
    default Holder<Biome> getUncachedNoiseBiome(int quartX, int quartY, int quartZ) {
        return delegate().getUncachedNoiseBiome(quartX, quartY, quartZ);
    }

    @Override
    default boolean isClientSide() {
        return delegate().isClientSide();
    }

    @Override
    default int getSeaLevel() {
        return delegate().getSeaLevel();
    }

    @Override
    default DimensionType dimensionType() {
        return delegate().dimensionType();
    }

    @Override
    default RegistryAccess registryAccess() {
        return delegate().registryAccess();
    }

    @Override
    default FeatureFlagSet enabledFeatures() {
        return delegate().enabledFeatures();
    }

    @Override
    default EnvironmentAttributeReader environmentAttributes() {
        return delegate().environmentAttributes();
    }

    // region: Conflicts
    @Override
    default int getMinY() {
        return delegate().getMinY();
    }

    @Override
    default int getHeight() {
        return delegate().getHeight();
    }

    @Override
    default @Nullable BlockGetter getChunkForCollisions(int chunkX, int chunkZ) {
        return delegate().getChunkForCollisions(chunkX, chunkZ);
    }

    @Override
    default Holder<Biome> getNoiseBiome(final int quartX, final int quartY, final int quartZ) {
        return delegate().getNoiseBiome(quartX, quartY, quartZ);
    }
    // endregion
}
