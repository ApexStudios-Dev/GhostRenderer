package dev.apexstudios.ghostrenderer.api.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public interface DelegatedLevelWriter extends LevelWriter {
    LevelWriter delegate();

    @Override
    default boolean setBlock(BlockPos pos, BlockState blockState, @Block.UpdateFlags int updateFlags, int updateLimit) {
        return delegate().setBlock(pos, blockState, updateFlags, updateLimit);
    }

    @Override
    default boolean removeBlock(BlockPos pos, boolean movedByPiston) {
        return delegate().removeBlock(pos, movedByPiston);
    }

    @Override
    default boolean destroyBlock(BlockPos pos, boolean dropResources, @Nullable Entity breaker, int updateLimit) {
        return delegate().destroyBlock(pos, dropResources, breaker, updateLimit);
    }
}
