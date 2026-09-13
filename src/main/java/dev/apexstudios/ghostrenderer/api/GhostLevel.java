package dev.apexstudios.ghostrenderer.api;

import dev.apexstudios.ghostrenderer.core.level.GhostLevelImpl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jspecify.annotations.Nullable;

public interface GhostLevel {
    Level reality();

    boolean setBlockState(BlockPos pos, BlockState blockState, boolean isValid);

    BlockState getBlockState(BlockPos pos);

    default FluidState getFluidState(BlockPos pos) {
        return getBlockState(pos).getFluidState();
    }

    void setBlockEntity(BlockPos pos, BlockState blockState, @Nullable ItemStack components, boolean isValid);

    @Nullable BlockEntity getBlockEntity(BlockPos pos);

    void addEntity(Entity entity, boolean isValid);

    static GhostLevel create(ClientLevel reality) {
        return new GhostLevelImpl(reality);
    }
}
