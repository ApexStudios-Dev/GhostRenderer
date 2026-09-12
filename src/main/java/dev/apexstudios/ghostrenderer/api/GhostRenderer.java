package dev.apexstudios.ghostrenderer.api;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.apexstudios.ghostrenderer.core.GhostFeatureRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.FeatureRendererType;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;
import net.neoforged.neoforge.client.submit.RenderPhaseKeys;

public interface GhostRenderer {
    String ID = "ghostrenderer";

    FeatureRendererType<GhostFeatureRenderer.Submit> FEATURE_RENDERER_TYPE = FeatureRendererType.create(id("feature_renderer"));

    static void submit(SubmitNodeCollector nodeCollector, PoseStack poseStack, GhostBlockAndTintGetter ghosts, Vec3 cameraPos) {
        poseStack.pushPose();
        poseStack.translate(cameraPos.scale(-1D));

        nodeCollector.submitSpecial(RenderPhaseKeys.ALWAYS_ON_TOP, new GhostFeatureRenderer.Submit(poseStack.last(), ghosts));

        poseStack.popPose();
    }

    static void submit(SubmitNodeCollector nodeCollector, PoseStack poseStack, LevelRenderState levelRenderState, ContextKey<? extends GhostBlockAndTintGetter> key) {
        var renderState = levelRenderState.getRenderData(key);

        if(renderState != null) {
            submit(
                    nodeCollector,
                    poseStack,
                    renderState,
                    levelRenderState.cameraRenderState.pos
            );
        }
    }

    static void submit(SubmitCustomGeometryEvent event, ContextKey<? extends GhostBlockAndTintGetter> key) {
        submit(event.getSubmitNodeCollector(), event.getPoseStack(), event.getLevelRenderState(), key);
    }

    static Identifier identifier(String identifier) {
        return Identifier.fromNamespaceAndPath(ID, identifier);
    }

    static String id(String identifier) {
        return ID + Identifier.NAMESPACE_SEPARATOR + identifier;
    }
}
