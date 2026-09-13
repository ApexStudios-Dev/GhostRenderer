package dev.apexstudios.ghostrenderer.core.level;

import dev.apexstudios.ghostrenderer.api.GhostLevel;
import dev.apexstudios.ghostrenderer.api.level.DelegatedBlockAndTintGetter;
import dev.apexstudios.ghostrenderer.api.level.fake.FakeLevel;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jspecify.annotations.Nullable;

public final class GhostLevelImpl implements GhostLevel, DelegatedBlockAndTintGetter {
    private final FakeLevel reality;
    private final BlockAndTintGetter delegate;

    private final Long2ObjectMap<GhostBlock> blockStates = new Long2ObjectOpenHashMap<>();
    private final Long2ObjectMap<GhostBlockEntity> blockEntities = new Long2ObjectOpenHashMap<>();

    public GhostLevelImpl(ClientLevel reality) {
        this.reality = new FakeLevel(reality);
        delegate = reality;
    }

    public Long2ObjectMap<GhostBlock> getBlockStates() {
        return blockStates;
    }

    public Long2ObjectMap<GhostBlockEntity> getBlockEntities() {
        return blockEntities;
    }

    @Override
    public Level reality() {
        return reality;
    }

    @Override
    public boolean setBlockState(BlockPos pos, BlockState blockState, boolean isValid) {
        blockStates.put(pos.asLong(), new GhostBlock(blockState, isValid));
        return true;
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return blockStates.getOrDefault(pos.asLong(), GhostBlock.AIR).blockState();
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void setBlockEntity(BlockPos pos, BlockState blockState, @Nullable ItemStack components, boolean isValid) {
        var key = pos.asLong();

        if(blockEntities.containsKey(key)) {
            var blockEntity = blockEntities.remove(key).blockEntity();
            blockEntity.setLevel(null);
            blockEntity.setRemoved();
        }

        if(!blockState.hasBlockEntity()) {
            return;
        }

        var blockEntity = ((EntityBlock) blockState.getBlock()).newBlockEntity(pos, blockState);

        if(blockEntity == null) {
            return;
        }

        blockEntity.setLevel(reality);

        if(components != null) {
            blockEntity.applyComponentsFromItemStack(components);
        }

        blockEntities.put(key, new GhostBlockEntity(blockEntity, isValid));
    }

    @Override
    public @Nullable BlockEntity getBlockEntity(BlockPos pos) {
        var key = pos.asLong();

        if(blockEntities.containsKey(key)) {
            return blockEntities.get(key).blockEntity();
        }

        return null;
    }

    @Override
    public void addEntity(Entity entity, boolean isValid) {
        // TODO
    }

    @Override
    public BlockAndTintGetter delegate() {
        return delegate;
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return getBlockState(pos).getFluidState();
    }
}
