package dev.apexstudios.ghostrenderer.api.level.fake;

import dev.apexstudios.ghostrenderer.api.level.DelegatedLevel;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.clock.ClockManager;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.ticks.LevelTickAccess;
import net.minecraft.world.ticks.TickPriority;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.world.AuxiliaryLightManager;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.model.data.ModelDataManager;
import org.jspecify.annotations.Nullable;


public class FakeLevel extends DelegatedLevel {
    private final FakeTickRateManager tickRateManager;
    private final FakeClockManager clockManager;
    private final FakeLevelTickAccess<Block> blockTicks;
    private final FakeLevelTickAccess<Fluid> fluidTicks;

    public FakeLevel(Level delegate) {
        super(delegate);

        tickRateManager = new FakeTickRateManager(delegate.tickRateManager());
        clockManager = new FakeClockManager(delegate.clockManager());
        blockTicks = new FakeLevelTickAccess<>(delegate.getBlockTicks());
        fluidTicks = new FakeLevelTickAccess<>(delegate.getFluidTicks());
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState blockState, @Block.UpdateFlags int updateFlags, int updateLimit) {
        return false;
    }

    @Override
    public boolean addFreshEntity(Entity entity) {
        return false;
    }

    @Override
    public Collection<? extends PartEntity<?>> dragonParts() {
        return List.copyOf(super.dragonParts());
    }

    @Override
    public TickRateManager tickRateManager() {
        return tickRateManager;
    }

    @Override
    public @Nullable MapItemSavedData getMapData(MapId id) {
        // TODO: FakeMapItemSavedData
        return super.getMapData(id);
    }

    @Override
    public Scoreboard getScoreboard() {
        // TODO: FakeScoreboard
        return super.getScoreboard();
    }

    @Override
    public ClockManager clockManager() {
        return clockManager;
    }

    @Override
    public ChunkSource getChunkSource() {
        // TODO: FakeChunkSource?
        return super.getChunkSource();
    }

    @Override
    public List<VoxelShape> getEntityCollisions(@Nullable Entity source, AABB testArea) {
        return List.copyOf(super.getEntityCollisions(source, testArea));
    }

    @Override
    public List<? extends Player> players() {
        return List.copyOf(super.players());
    }

    @Override
    public WorldBorder getWorldBorder() {
        // TODO: FakeWorldBorder
        return super.getWorldBorder();
    }

    @Override
    public LevelTickAccess<Block> getBlockTicks() {
        return blockTicks;
    }

    @Override
    public LevelTickAccess<Fluid> getFluidTicks() {
        return fluidTicks;
    }

    @Override
    public LevelLightEngine getLightEngine() {
        // TODO: FakeLevelLightEngine?
        return LevelLightEngine.EMPTY;
    }

    @Override
    public List<Entity> getEntities(@Nullable Entity except, AABB bb, Predicate<? super Entity> selector) {
        return List.copyOf(super.getEntities(except, bb, selector));
    }

    @Override
    public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector) {
        return List.copyOf(super.getEntities(type, bb, selector));
    }

    @Override
    public boolean noSave() {
        return true;
    }

    @Override
    public @Nullable <T> T setData(AttachmentType<T> type, T data) {
        return getData(type);
    }

    @Override
    public @Nullable <T> T removeData(AttachmentType<T> type) {
        return getData(type);
    }

    @Override
    public @Nullable AuxiliaryLightManager getAuxLightManager(ChunkPos pos) {
        // TODO: FakeAuxiliaryLightManager
        return super.getAuxLightManager(pos);
    }

    @Override
    public ModelData getModelData(BlockPos pos) {
        // TODO: FakeModelData
        return super.getModelData(pos);
    }

    @Override
    public @Nullable ModelDataManager getModelDataManager() {
        // TODO: FakeModelDataManager
        return super.getModelDataManager();
    }

    @Override public void addDestroyBlockEffect(BlockPos pos, BlockState blockState) {}
    @Override public void sendBlockUpdated(BlockPos pos, BlockState old, BlockState current, @Block.UpdateFlags int updateFlags) {}
    @Override public void playSeededSound(@Nullable Entity except, double x, double y, double z, Holder<SoundEvent> sound, SoundSource source, float volume, float pitch, long seed) {}
    @Override public void playSeededSound(@Nullable Entity except, Entity sourceEntity, Holder<SoundEvent> sound, SoundSource source, float volume, float pitch, long seed) {}
    @Override public void explode(@Nullable Entity source, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator damageCalculator, double x, double y, double z, float r, boolean fire, ExplosionInteraction interactionType, ParticleOptions smallExplosionParticles, ParticleOptions largeExplosionParticles, WeightedList<ExplosionParticleInfo> blockParticles, Holder<SoundEvent> explosionSound) {}
    @Override public void setRespawnData(LevelData.RespawnData respawnData) {}
    @Override public void destroyBlockProgress(int id, BlockPos blockPos, int progress) {}
    @Override public void levelEvent(@Nullable Entity source, int type, BlockPos pos, int data) {}
    @Override public void gameEvent(Holder<GameEvent> gameEvent, Vec3 position, GameEvent.Context context) {}
    @Override public void markAndNotifyBlock(BlockPos pos, @Nullable LevelChunk chunk, BlockState oldState, BlockState blockState, int updateFlags, int updateLimit) {}
    @Override public void updatePOIOnBlockStateChange(BlockPos pos, BlockState oldState, BlockState newState) {}
    @Override public void setBlocksDirty(BlockPos pos, BlockState oldState, BlockState newState) {}
    @Override public void updateNeighborsAt(BlockPos pos, Block sourceBlock, @Nullable Orientation orientation) {}
    @Override public void updateNeighborsAtExceptFromFacing(BlockPos pos, Block blockObject, Direction skipDirection, @Nullable Orientation orientation) {}
    @Override public void neighborChanged(BlockPos pos, Block changedBlock, @Nullable Orientation orientation) {}
    @Override public void neighborChanged(BlockState state, BlockPos pos, Block changedBlock, @Nullable Orientation orientation, boolean movedByPiston) {}
    @Override public void neighborShapeChanged(Direction direction, BlockPos pos, BlockPos neighborPos, BlockState neighborState, @Block.UpdateFlags int updateFlags, int updateLimit) {}
    @Override public void playSound(@Nullable Entity except, BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch) {}
    @Override public void playSeededSound(@Nullable Entity except, double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch, long seed) {}
    @Override public void playSound(@Nullable Entity except, double x, double y, double z, SoundEvent sound, SoundSource source) {}
    @Override public void playSound(@Nullable Entity except, double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch) {}
    @Override public void playSound(@Nullable Entity except, double x, double y, double z, Holder<SoundEvent> sound, SoundSource source, float volume, float pitch) {}
    @Override public void playSound(@Nullable Entity except, Entity sourceEntity, SoundEvent sound, SoundSource source, float volume, float pitch) {}
    @Override public void playLocalSound(BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch, boolean distanceDelay) {}
    @Override public void playLocalSound(Entity sourceEntity, SoundEvent sound, SoundSource source, float volume, float pitch) {}
    @Override public void playLocalSound(double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch, boolean distanceDelay) {}
    @Override public void playPlayerSound(SoundEvent sound, SoundSource source, float volume, float pitch) {}
    @Override public void addParticle(ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {}
    @Override public void addParticle(ParticleOptions particle, boolean overrideLimiter, boolean alwaysShow, double x, double y, double z, double xd, double yd, double zd) {}
    @Override public void addAlwaysVisibleParticle(ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {}
    @Override public void addAlwaysVisibleParticle(ParticleOptions particle, boolean overrideLimiter, double x, double y, double z, double xd, double yd, double zd) {}
    @Override public void addBlockEntityTicker(TickingBlockEntity ticker) {}
    @Override public void addFreshBlockEntities(Collection<BlockEntity> beList) {}
    @Override public void tickBlockEntities() {}
    @Override public <T extends Entity> void guardEntityTick(Consumer<T> tick, T entity) {}
    @Override public void explode(@Nullable Entity source, double x, double y, double z, float r, ExplosionInteraction blockInteraction) {}
    @Override public void explode(@Nullable Entity source, double x, double y, double z, float r, boolean fire, ExplosionInteraction blockInteraction) {}
    @Override public void explode(@Nullable Entity source, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator damageCalculator, Vec3 boomPos, float r, boolean fire, ExplosionInteraction blockInteraction) {}
    @Override public void explode(@Nullable Entity source, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator damageCalculator, double x, double y, double z, float r, boolean fire, ExplosionInteraction interactionType) {}
    @Override public void setBlockEntity(BlockEntity blockEntity) {}
    @Override public void removeBlockEntity(BlockPos pos) {}
    @Override public void updateSkyBrightness() {}
    @Override public void setSpawnSettings(boolean spawnEnemies) {}
    @Override public void close() throws IOException {}
    @Override public void blockEntityChanged(BlockPos pos) {}
    @Override public void onBlockEntityAdded(BlockEntity blockEntity) {}
    @Override public void broadcastEntityEvent(Entity entity, byte event) {}
    @Override public void broadcastDamageEvent(Entity entity, DamageSource source) {}
    @Override public void blockEvent(BlockPos pos, Block block, int b0, int b1) {}
    @Override public void setThunderLevel(float thunderLevel) {}
    @Override public void setRainLevel(float rainLevel) {}
    @Override public void globalLevelEvent(int type, BlockPos pos, int data) {}
    @Override public void createFireworks(double x, double y, double z, double xd, double yd, double zd, List<FireworkExplosion> explosions, boolean playSound) {}
    @Override public void updateNeighbourForOutputSignal(BlockPos pos, Block changedBlock) {}
    @Override public void setSkyFlashTime(int skyFlashTime) {}
    @Override public void sendPacketToServer(Packet<?> packet) {}
    @Override public void updateNeighborsAt(BlockPos pos, Block sourceBlock) {}
    @Override public void playSound(@Nullable Entity except, BlockPos pos, SoundEvent soundEvent, SoundSource source) {}
    @Override public void levelEvent(int type, BlockPos pos, int data) {}
    @Override public void gameEvent(@Nullable Entity sourceEntity, Holder<GameEvent> gameEvent, Vec3 pos) {}
    @Override public void gameEvent(@Nullable Entity sourceEntity, Holder<GameEvent> gameEvent, BlockPos pos) {}
    @Override public void gameEvent(Holder<GameEvent> gameEvent, BlockPos pos, GameEvent.Context context) {}
    @Override public void gameEvent(ResourceKey<GameEvent> gameEvent, BlockPos pos, GameEvent.Context context) {}
    @Override public void scheduleTick(BlockPos pos, Block type, int tickDelay, TickPriority priority) {}
    @Override public void scheduleTick(BlockPos pos, Block type, int tickDelay) {}
    @Override public void scheduleTick(BlockPos pos, Fluid type, int tickDelay, TickPriority priority) {}
    @Override public void scheduleTick(BlockPos pos, Fluid type, int tickDelay) {}
    @Override public void syncData(AttachmentType<?> type) {}
    @Override public void syncData(Supplier<? extends AttachmentType<?>> type) {}
    @Override public void invalidateCapabilities(BlockPos pos) {}
    @Override public void invalidateCapabilities(ChunkPos pos) {}
}
