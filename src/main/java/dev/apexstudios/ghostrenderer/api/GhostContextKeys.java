package dev.apexstudios.ghostrenderer.api;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.apexstudios.ghostrenderer.core.GhostFeatureRenderer;
import dev.apexstudios.ghostrenderer.core.GhostSubmitNodeCollector;
import dev.apexstudios.ghostrenderer.core.level.GhostBlockEntity;
import dev.apexstudios.ghostrenderer.core.level.GhostBlockEntityRenderState;
import dev.apexstudios.ghostrenderer.core.level.GhostEntity;
import dev.apexstudios.ghostrenderer.core.level.GhostEntityRenderState;
import dev.apexstudios.ghostrenderer.core.level.GhostLevelImpl;
import dev.apexstudios.ghostrenderer.core.level.GhostLevelRenderState;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.neoforged.neoforge.client.submit.RenderPhaseKeys;
import org.jspecify.annotations.Nullable;

public record GhostContextKeys(
        ContextKey<GhostLevelRenderState> blocks,
        ContextKey<List<GhostBlockEntityRenderState>> blockEntities,
        ContextKey<List<GhostEntityRenderState>> entities
) {
    public void extract(LevelRenderState levelRenderState, float partialTicks, GhostLevel level) {
        var internal = (GhostLevelImpl) level;
        levelRenderState.setRenderData(blocks, new GhostLevelRenderState(Long2ObjectMaps.unmodifiable(internal.getBlockStates()), internal, internal.isValid()));
        levelRenderState.setRenderData(blockEntities, extractBlockEntities(levelRenderState, partialTicks, internal.getBlockEntities().values()));
        levelRenderState.setRenderData(entities, extractEntites(levelRenderState, partialTicks, internal.getEntities()));
    }

    public void submit(SubmitNodeCollector nodeCollector, PoseStack poseStack, LevelRenderState levelRenderState, GhostProperties properties) {
        var validCollector = new GhostSubmitNodeCollector(nodeCollector, true, properties);
        var invalidCollector = new GhostSubmitNodeCollector(nodeCollector, false, properties);

        var ghost = levelRenderState.getRenderData(blocks);
        var globalIsValid = ghost == null || ghost.isValid();

        submitBlockStates(nodeCollector, poseStack, levelRenderState, ghost, properties);
        submitBlockEntities(validCollector, invalidCollector, poseStack, levelRenderState, levelRenderState.getRenderData(blockEntities), properties, globalIsValid);
        submitEntities(validCollector, invalidCollector, poseStack, levelRenderState, levelRenderState.getRenderData(entities), properties, globalIsValid);
    }

    private List<GhostBlockEntityRenderState> extractBlockEntities(LevelRenderState levelRenderState, float partialTicks, Iterable<GhostBlockEntity> blockEntities) {
        var renderStates = ImmutableList.<GhostBlockEntityRenderState>builder();
        var dispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();

        for(var blockEntity : blockEntities) {
            var renderState = blockEntity.extract(dispatcher, partialTicks, levelRenderState.cameraRenderState.pos);

            if(renderState != null) {
                renderStates.add(renderState);
            }

            blockEntity.blockEntity().setRemoved();
        }

        return renderStates.build();
    }

    private List<GhostEntityRenderState> extractEntites(LevelRenderState levelRenderState, float partialTicks, Iterable<GhostEntity> entities) {
        var renderStates = ImmutableList.<GhostEntityRenderState>builder();
        var dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();

        for(var entity : entities) {
            var renderState = entity.extract(dispatcher, partialTicks);

            if(renderState != null) {
                renderStates.add(renderState);
            }

            entity.entity().discard();
        }

        return renderStates.build();
    }

    private void submitBlockStates(SubmitNodeCollector nodeCollector, PoseStack poseStack, LevelRenderState levelRenderState, @Nullable GhostLevelRenderState renderState, GhostProperties properties) {
        if(renderState == null) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(levelRenderState.cameraRenderState.pos.scale(-1D));

        nodeCollector.submitSpecial(properties.alwaysOnTop() ? RenderPhaseKeys.ALWAYS_ON_TOP : RenderPhaseKeys.AFTER_TERRAIN, new GhostFeatureRenderer.Submit(poseStack.last().copy(), renderState, properties));

        poseStack.popPose();
    }

    private void submitBlockEntities(SubmitNodeCollector validCollector, SubmitNodeCollector invalidCollector, PoseStack poseStack, LevelRenderState levelRenderState, @Nullable List<GhostBlockEntityRenderState> renderStates, GhostProperties properties, boolean globalIsValid) {
        if(renderStates == null || renderStates.isEmpty()) {
            return;
        }

        var dispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();

        poseStack.pushPose();
        poseStack.translate(levelRenderState.cameraRenderState.pos.scale(-1D));

        for(var renderState : renderStates) {
            var blockEntityRenderState = renderState.renderState();
            var renderer = Objects.requireNonNull(dispatcher.getRenderer(blockEntityRenderState));
            var isValid = properties.validPerRender() ? renderState.isValid() : globalIsValid;
            var nodeCollector = isValid ? validCollector : invalidCollector;

            poseStack.pushPose();
            poseStack.translate(
                    blockEntityRenderState.blockPos.getX(),
                    blockEntityRenderState.blockPos.getY(),
                    blockEntityRenderState.blockPos.getZ()
            );

            renderer.submit(blockEntityRenderState, poseStack, nodeCollector, levelRenderState.cameraRenderState);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void submitEntities(SubmitNodeCollector validCollector, SubmitNodeCollector invalidCollector, PoseStack poseStack, LevelRenderState levelRenderState, @Nullable List<GhostEntityRenderState> renderStates, GhostProperties properties, boolean globalIsValid) {
        if(renderStates == null || renderStates.isEmpty()) {
            return;
        }

        var dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();

        poseStack.pushPose();
        poseStack.translate(levelRenderState.cameraRenderState.pos.scale(-1D));

        for(var renderState : renderStates) {
            var entityRenderState = renderState.renderState();
            // should be safe as this renderer was used to extract the state earlier
            var renderer = (EntityRenderer) Objects.requireNonNull(dispatcher.getRenderer(entityRenderState));
            var isValid = properties.validPerRender() ? renderState.isValid() : globalIsValid;
            var nodeCollector = isValid ? validCollector : invalidCollector;
            submitEntity(nodeCollector, poseStack, levelRenderState.cameraRenderState, renderer, entityRenderState);
        }

        poseStack.popPose();
    }

    private <TRenderState extends EntityRenderState> void submitEntity(SubmitNodeCollector nodeCollector, PoseStack poseStack, CameraRenderState cameraRenderState, EntityRenderer<?, TRenderState> renderer, TRenderState renderState) {
        poseStack.pushPose();
        poseStack.translate(renderState.x, renderState.y, renderState.z);

        renderer.submit(renderState, poseStack, nodeCollector, cameraRenderState);
        poseStack.popPose();
    }

    public static GhostContextKeys create(String namespace) {
        return new GhostContextKeys(
                new ContextKey<>(Identifier.fromNamespaceAndPath(namespace, "render_state")),
                new ContextKey<>(Identifier.fromNamespaceAndPath(namespace, "render_state/block_entites")),
                new ContextKey<>(Identifier.fromNamespaceAndPath(namespace, "render_state/entites"))
        );
    }
}
