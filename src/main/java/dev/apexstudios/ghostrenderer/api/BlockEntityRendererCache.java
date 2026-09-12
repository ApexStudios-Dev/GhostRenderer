package dev.apexstudios.ghostrenderer.api;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public final class BlockEntityRendererCache {
    private static final ContextKey<List<RenderState>> RENDER_STATE_KEY = new ContextKey<>(GhostRenderer.identifier("block_entities"));

    private final Long2ObjectMap<BlockEntityState<?>> blockEntities = new Long2ObjectOpenHashMap<>();

    public void newFrame() {
        blockEntities.values().forEach(BlockEntityState::clear);
        blockEntities.clear();
    }

    @SuppressWarnings("unchecked")
    public void setBlockEntity(BlockPos pos, BlockState blockState, Level level, ItemStack data, boolean isValidPlacement) {
        var key = pos.asLong();

        if(blockEntities.containsKey(key)) {
            blockEntities.get(key).clear();
            blockEntities.remove(key);
        }

        if(!(blockState.getBlock() instanceof EntityBlock block)) {
            return;
        }

        var blockEntity = block.newBlockEntity(pos, blockState);

        if(blockEntity == null) {
            return;
        }

        blockEntity.setLevel(level);
        blockEntity.applyComponentsFromItemStack(data);

        blockEntities.put(key, new BlockEntityState<>(
                blockEntity,
                (BlockEntityTicker<? super BlockEntity>) block.getTicker(level, blockState, blockEntity.getType()),
                isValidPlacement
        ));
    }

    public @Nullable BlockEntity getBlockEntity(BlockPos pos) {
        var key = pos.asLong();
        return blockEntities.containsKey(key) ? blockEntities.get(key).blockEntity : null;
    }

    public void extract(LevelRenderState levelRenderState, float partialTick) {
        var dispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
        var renderStates = new ArrayList<RenderState>();

        blockEntities.values().forEach(state -> {
            var renderState = extract(dispatcher, state.blockEntity, partialTick, levelRenderState.cameraRenderState.pos);

            if(renderState != null) {
                renderStates.add(new RenderState(renderState, state.isValidPlacement));
            }
        });

        if(!renderStates.isEmpty()) {
            levelRenderState.setRenderData(RENDER_STATE_KEY, List.copyOf(renderStates));
        }
    }

    public void submit(SubmitNodeCollector nodeCollector, PoseStack poseStack, LevelRenderState levelRenderState) {
        var renderStates = levelRenderState.getRenderData(RENDER_STATE_KEY);

        if(renderStates == null || renderStates.isEmpty()) {
            return;
        }

        var dispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();

        poseStack.pushPose();
        poseStack.translate(levelRenderState.cameraRenderState.pos.scale(-1D));

        for(var renderState : renderStates) {
            submit(dispatcher, renderState, nodeCollector, poseStack, levelRenderState);
        }

        poseStack.popPose();
    }

    private @Nullable BlockEntityRenderState extract(
            BlockEntityRenderDispatcher dispatcher,
            BlockEntity blockEntity,
            float partialTick,
            Vec3 cameraPosition
    ) {
        var renderer = dispatcher.getRenderer(blockEntity);

        if(renderer == null) {
            return null;
        }

        var renderState = renderer.createRenderState();
        renderer.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, null);
        return renderState;
    }

    private void submit(
            BlockEntityRenderDispatcher dispatcher,
            RenderState renderState,
            SubmitNodeCollector nodeCollector,
            PoseStack poseStack,
            LevelRenderState levelRenderState
    ) {
        var renderer = Objects.requireNonNull(dispatcher.getRenderer(renderState.blockEntityRenderState));

        poseStack.pushPose();
        poseStack.translate(
                renderState.blockEntityRenderState.blockPos.getX(),
                renderState.blockEntityRenderState.blockPos.getY(),
                renderState.blockEntityRenderState.blockPos.getZ()
        );

        renderer.submit(renderState.blockEntityRenderState, poseStack, new GhostSubmitNodeCollector(nodeCollector, renderState.isValidPlacement), levelRenderState.cameraRenderState);
        poseStack.popPose();
    }

    private record BlockEntityState<TBlockEntity extends BlockEntity>(
            TBlockEntity blockEntity,
            @Nullable BlockEntityTicker<TBlockEntity> ticker,
            boolean isValidPlacement
    ) {
        @SuppressWarnings("DataFlowIssue")
        private void clear() {
            blockEntity.setLevel(null);
            blockEntity.setRemoved();
        }
    }

    private record RenderState(
            BlockEntityRenderState blockEntityRenderState,
            boolean isValidPlacement
    ) { }
}
