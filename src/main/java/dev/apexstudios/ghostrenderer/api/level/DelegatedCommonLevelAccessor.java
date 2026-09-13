package dev.apexstudios.ghostrenderer.api.level;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.CommonLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public interface DelegatedCommonLevelAccessor extends DelegatedLevelReader, DelegatedLevelSimulatedRW, DelegatedEntityGetter, CommonLevelAccessor {
    @Override
    CommonLevelAccessor delegate();

    // region Conflicts
    @Override
    default List<VoxelShape> getEntityCollisions(final @Nullable Entity source, final AABB testArea) {
        return delegate().getEntityCollisions(source, testArea);
    }

    @Override
    default BlockPos getHeightmapPos(Heightmap.Types type, BlockPos pos) {
        return delegate().getHeightmapPos(type, pos);
    }

    @Override
    default <T extends BlockEntity> Optional<T> getBlockEntity(BlockPos pos, BlockEntityType<T> type) {
        return delegate().getBlockEntity(pos, type);
    }
    // endregio
}
