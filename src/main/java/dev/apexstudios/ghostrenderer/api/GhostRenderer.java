package dev.apexstudios.ghostrenderer.api;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.apexstudios.ghostrenderer.core.GhostFeatureRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.FeatureRendererType;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.TriPredicate;

public interface GhostRenderer {
    String ID = "ghostrenderer";

    FeatureRendererType<GhostFeatureRenderer.Submit> FEATURE_RENDERER_TYPE = FeatureRendererType.create(id("feature_renderer"));
    Identifier ALWAYS_ACTIVE = identifier("always_active");

    static void setRenderData(LevelRenderState levelRenderState, float partialTicks, GhostContextKeys keys, GhostLevel level) {
        keys.extract(levelRenderState, partialTicks, level);
    }

    static void setRenderData(ExtractLevelRenderStateEvent event, GhostContextKeys keys, GhostLevel level) {
        setRenderData(event.getRenderState(), event.getDeltaTracker().getGameTimeDeltaPartialTick(false), keys, level);
    }

    static void submit(SubmitNodeCollector nodeCollector, PoseStack poseStack, LevelRenderState levelRenderState, GhostContextKeys keys) {
        keys.submit(nodeCollector, poseStack, levelRenderState);
    }

    static void submit(SubmitCustomGeometryEvent event, GhostContextKeys keys) {
        submit(event.getSubmitNodeCollector(), event.getPoseStack(), event.getLevelRenderState(), keys);
    }

    static void registerEvents(GhostContextKeys keys, TriPredicate<GhostLevel, Player, BlockHitResult> extractor) {
        NeoForge.EVENT_BUS.addListener(ExtractLevelRenderStateEvent.class, event -> {
            var level = event.getLevel();
            var client = Minecraft.getInstance();
            var player = client.player;

            if(player == null || player.isSpectator()) {
                return;
            }

            if(!(client.hitResult instanceof BlockHitResult hitResult)) {
                return;
            }

            if(hitResult.getType() == HitResult.Type.MISS) {
                if(!client.debugEntries.isCurrentlyEnabled(ALWAYS_ACTIVE)) {
                    return;
                }

                // vanillas hit result flickers between top and bottom of air blocks
                // as you move your view around, this simple hack "fixes" that
                // simply replace the direction with what ever is closest to the players view
                // if we are looking up or down
                if(hitResult.getDirection().getAxis().isVertical()) {
                    hitResult = hitResult.withDirection(player.getNearestViewDirection());
                }
            }

            var ghostLevel = GhostLevel.create(level);

            if(extractor.test(ghostLevel, player, hitResult)) {
                setRenderData(event, keys, ghostLevel);
            }
        });

        NeoForge.EVENT_BUS.addListener(SubmitCustomGeometryEvent.class, event -> submit(event, keys));
    }

    static void registerEvents(String namespace, TriPredicate<GhostLevel, Player, BlockHitResult> extractor) {
        registerEvents(GhostContextKeys.create(namespace), extractor);
    }

    static Identifier identifier(String identifier) {
        return Identifier.fromNamespaceAndPath(ID, identifier);
    }

    static String id(String identifier) {
        return ID + Identifier.NAMESPACE_SEPARATOR + identifier;
    }
}
