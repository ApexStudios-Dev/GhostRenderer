package dev.apexstudios.ghostrenderer.api.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.ticks.ScheduledTick;
import net.minecraft.world.ticks.TickPriority;
import org.jspecify.annotations.Nullable;

public interface DelegatedLevelAccessor extends DelegatedCommonLevelAccessor, DelegatedScheduledTickAccess, LevelAccessor {
    @Override
    LevelAccessor delegate();

    @Override
    default long nextSubTickCount() {
        return delegate().nextSubTickCount();
    }

    @Override
    default LevelData getLevelData() {
        return delegate().getLevelData();
    }

    @Override
    default @Nullable MinecraftServer getServer() {
        return delegate().getServer();
    }

    @Override
    default ChunkSource getChunkSource() {
        return delegate().getChunkSource();
    }

    @Override
    default RandomSource getRandom() {
        return delegate().getRandom();
    }

    @Override
    default void playSound(@Nullable Entity except, BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch) {
        delegate().playSound(except, pos, sound, source, volume, pitch);
    }

    @Override
    default void addParticle(ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {
        delegate().addParticle(particle, x, y, z, xd, yd, zd);
    }

    @Override
    default void levelEvent(@Nullable Entity source, int type, BlockPos pos, int data) {
        delegate().levelEvent(source, type, pos, data);
    }

    @Override
    default void gameEvent(Holder<GameEvent> gameEvent, Vec3 position, GameEvent.Context context) {
        delegate().gameEvent(gameEvent, position, context);
    }

    // region Conflicts
    @Override
    default boolean hasChunk(int chunkX, int chunkZ) {
        return delegate().hasChunk(chunkX, chunkZ);
    }

    @Override
    default <T> ScheduledTick<T> createTick(BlockPos pos, T type, int tickDelay) {
        return delegate().createTick(pos, type, tickDelay);
    }

    @Override
    default <T> ScheduledTick<T> createTick(BlockPos pos, T type, int tickDelay, TickPriority priority) {
        return delegate().createTick(pos, type, tickDelay, priority);
    }
    // endregion
}
