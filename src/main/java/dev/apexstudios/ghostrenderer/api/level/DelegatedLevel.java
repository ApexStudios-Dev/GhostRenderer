package dev.apexstudios.ghostrenderer.api.level;

import dev.apexstudios.ghostrenderer.mixin.LevelAccessor;
import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.clock.ClockManager;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.crafting.RecipeAccess;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.scores.Scoreboard;
import net.neoforged.neoforge.entity.PartEntity;
import org.jspecify.annotations.Nullable;

public class DelegatedLevel extends Level implements DelegatedAttachmentHolder, DelegatedLevelAccessor {
    private final Level delegate;

    protected DelegatedLevel(Level delegate) {
        super(
                (WritableLevelData) delegate.getLevelData(),
                delegate.dimension(),
                delegate.registryAccess(),
                delegate.dimensionTypeRegistration(),
                delegate.isClientSide(),
                delegate.isDebug(),
                delegate.getBiomeManager().biomeZoomSeed,
                delegate.neighborUpdater.maxChainedNeighborUpdates
        );

        this.delegate = delegate;
    }

    @Override
    public Level delegate() {
        return delegate;
    }

    @Override
    public void sendBlockUpdated(BlockPos pos, BlockState old, BlockState current, @Block.UpdateFlags int updateFlags) {
        delegate().sendBlockUpdated(pos, old, current, updateFlags);
    }

    @Override
    public void playSeededSound(@Nullable Entity except, double x, double y, double z, Holder<SoundEvent> sound, SoundSource source, float volume, float pitch, long seed) {
        delegate().playSeededSound(except, x, y, z, sound, source, volume, pitch, seed);
    }

    @Override
    public void playSeededSound(@Nullable Entity except, Entity sourceEntity, Holder<SoundEvent> sound, SoundSource source, float volume, float pitch, long seed) {
        delegate().playSeededSound(except, sourceEntity, sound, source, volume, pitch, seed);
    }

    @Override
    public void explode(@Nullable Entity source, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator damageCalculator, double x, double y, double z, float r, boolean fire, ExplosionInteraction interactionType, ParticleOptions smallExplosionParticles, ParticleOptions largeExplosionParticles, WeightedList<ExplosionParticleInfo> blockParticles, Holder<SoundEvent> explosionSound) {
        delegate().explode(source, damageSource, damageCalculator, x, y, z, r, fire, interactionType, smallExplosionParticles, largeExplosionParticles, blockParticles, explosionSound);
    }

    @Override
    public String gatherChunkSourceStats() {
        return delegate().gatherChunkSourceStats();
    }

    @Override
    public void setRespawnData(LevelData.RespawnData respawnData) {
        delegate().setRespawnData(respawnData);
    }

    @Override
    public LevelData.RespawnData getRespawnData() {
        return delegate().getRespawnData();
    }

    @Override
    public @Nullable Entity getEntity(int id) {
        return delegate().getEntity(id);
    }

    @Override
    public Collection<? extends PartEntity<?>> dragonParts() {
        return delegate().dragonParts();
    }

    @Override
    public TickRateManager tickRateManager() {
        return delegate().tickRateManager();
    }

    @Override
    public @Nullable MapItemSavedData getMapData(MapId id) {
        return delegate().getMapData(id);
    }

    @Override
    public void destroyBlockProgress(int id, BlockPos blockPos, int progress) {
        delegate().destroyBlockProgress(id, blockPos, progress);
    }

    @Override
    public Scoreboard getScoreboard() {
        return delegate().getScoreboard();
    }

    @Override
    public RecipeAccess recipeAccess() {
        return delegate().recipeAccess();
    }

    @Override
    protected LevelEntityGetter<Entity> getEntities() {
        return ((LevelAccessor) delegate()).GhostRenderer$getEntities();
    }

    @Override
    public ClockManager clockManager() {
        return delegate().clockManager();
    }

    @Override
    public EnvironmentAttributeSystem environmentAttributes() {
        return delegate().environmentAttributes();
    }
}
