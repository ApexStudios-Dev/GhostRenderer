package dev.apexstudios.ghostrenderer.api.level;

import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public interface DelegatedCollisionGetter extends DelegatedBlockGetter, CollisionGetter {
    @Override
    CollisionGetter delegate();

    @Override
    default WorldBorder getWorldBorder() {
        return delegate().getWorldBorder();
    }

    @Override
    default @Nullable BlockGetter getChunkForCollisions(int chunkX, int chunkZ) {
        return delegate().getChunkForCollisions(chunkX, chunkZ);
    }

    @Override
    default List<VoxelShape> getEntityCollisions(final @Nullable Entity source, final AABB testArea) {
        return delegate().getEntityCollisions(source, testArea);
    }
}
