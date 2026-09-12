package dev.apexstudios.ghostrenderer.api.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jspecify.annotations.Nullable;

public interface DelegatedBlockGetter extends DelegatedLevelHeightAccessor, BlockGetter {
    @Override
    BlockGetter delegate();

    @Override
    default @Nullable BlockEntity getBlockEntity(BlockPos pos) {
        return delegate().getBlockEntity(pos);
    }

    @Override
    default BlockState getBlockState(BlockPos pos) {
        return delegate().getBlockState(pos);
    }

    @Override
    default FluidState getFluidState(BlockPos pos) {
        return delegate().getFluidState(pos);
    }
}
