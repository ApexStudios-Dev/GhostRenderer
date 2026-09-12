package dev.apexstudios.ghostrenderer.api;

import dev.apexstudios.ghostrenderer.api.level.DelegatedBlockAndTintGetter;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiConsumer;

public sealed class GhostBlockAndTintGetter implements DelegatedBlockAndTintGetter {
    private final BlockAndTintGetter delegate;
    protected final Long2ObjectMap<GhostBlock> blockStates;

    private GhostBlockAndTintGetter(BlockAndTintGetter delegate, Long2ObjectMap<GhostBlock> blockStates) {
        this.delegate = delegate;
        this.blockStates = blockStates;
    }

    public boolean isEmpty() {
        return blockStates.isEmpty();
    }

    public void forEach(BiConsumer<BlockPos, GhostBlock> action) {
        Long2ObjectMaps.fastForEach(blockStates, entry -> action.accept(
                BlockPos.of(entry.getLongKey()),
                entry.getValue()
        ));
    }

    @Override
    public BlockAndTintGetter delegate() {
        return delegate;
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        var key = pos.asLong();
        return blockStates.containsKey(key) ? blockStates.get(key).blockState : DelegatedBlockAndTintGetter.super.getBlockState(pos);
    }

    public GhostBlockAndTintGetter immutable() {
        return this;
    }

    public static Mutable create(BlockAndTintGetter delegate) {
        return new Mutable(delegate);
    }

    public static final class Mutable extends GhostBlockAndTintGetter {
        private Mutable(BlockAndTintGetter delegate) {
            super(delegate, new Long2ObjectOpenHashMap<>());
        }

        public void setBlockState(BlockPos pos, GhostBlock ghostBlock) {
            blockStates.put(pos.asLong(), ghostBlock);
        }

        public void setBlockState(BlockPos pos, BlockState blockState) {
            setBlockState(pos, new GhostBlock(blockState, blockState.getSeed(pos), true));
        }

        @Override
        public GhostBlockAndTintGetter immutable() {
            return new GhostBlockAndTintGetter(delegate(), Long2ObjectMaps.unmodifiable(blockStates));
        }
    }

    public record GhostBlock(
            BlockState blockState,
            long seed,
            boolean isValid
    ) { }
}
