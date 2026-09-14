package dev.apexstudios.ghostrenderer.core.level;

import dev.apexstudios.ghostrenderer.api.GhostLevel;
import dev.apexstudios.ghostrenderer.api.level.fake.FakeLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jspecify.annotations.Nullable;

final class GhostedLevel extends FakeLevel {
    private final GhostLevel level;

    public GhostedLevel(GhostLevel level) {
        super(level.reality());

        this.level = level;
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        var ghost = level.getBlockState(pos);
        return ghost.isEmpty() ? super.getBlockState(pos) : ghost;
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return getBlockState(pos).getFluidState();
    }

    @Override
    public @Nullable BlockEntity getBlockEntity(BlockPos pos) {
        var ghost = level.getBlockEntity(pos);
        return ghost == null ? super.getBlockEntity(pos) : ghost;
    }
}
