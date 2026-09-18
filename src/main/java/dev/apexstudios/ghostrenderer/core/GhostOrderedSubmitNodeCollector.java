package dev.apexstudios.ghostrenderer.core;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.apexstudios.ghostrenderer.api.GhostProperties;
import dev.apexstudios.ghostrenderer.api.feature.DelegatedOrderedSubmitNodeCollector;
import java.util.function.BiConsumer;
import net.minecraft.Optionull;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.CustomFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.UvMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.submit.RenderPhaseKeys;
import org.jspecify.annotations.Nullable;

public sealed class GhostOrderedSubmitNodeCollector implements DelegatedOrderedSubmitNodeCollector permits GhostSubmitNodeCollector {
    private final OrderedSubmitNodeCollector delegate;
    protected final boolean isValid;
    protected final GhostProperties properties;

    public GhostOrderedSubmitNodeCollector(OrderedSubmitNodeCollector delegate, boolean isValid, GhostProperties properties) {
        this.delegate = delegate;
        this.isValid = isValid;
        this.properties = properties;
    }

    public void submitGhostGeometry(PoseStack poseStack, RenderType renderType, @Nullable TextureAtlasSprite sprite, SubmitNodeCollector.CustomGeometryRenderer renderer) {
        submitGhostGeometryStack(poseStack, renderType, sprite, (ghosePoseStack, buffer) -> renderer.render(ghosePoseStack.last(), buffer));
    }

    public void submitGhostGeometryStack(PoseStack poseStack, RenderType renderType, @Nullable TextureAtlasSprite sprite, BiConsumer<PoseStack, VertexConsumer> renderer) {
        var atlas = atlas(renderType);

        if(sprite != null) {
            atlas = sprite.atlasLocation();
        }

        submitGhostGeometryStack(poseStack, atlas, sprite, renderer);
    }

    public void submitGhostGeometryStack(PoseStack poseStack, Identifier atlas, @Nullable UvMapping uvMapping, BiConsumer<PoseStack, VertexConsumer> renderer) {
        submitSpecial(properties.alwaysOnTop() ? RenderPhaseKeys.ALWAYS_ON_TOP : RenderPhaseKeys.AFTER_TERRAIN, new CustomFeatureRenderer.Submit(
                poseStack.last().copy(),
                RenderTypes.entityTranslucentCull(atlas),
                (pose, buffer) -> {
                    var newPoseStack = new PoseStack();
                    newPoseStack.setIdentity();
                    newPoseStack.last().set(pose);

                    renderer.accept(newPoseStack, new GhostVertexConsumer(
                            Optionull.mapOrDefault(uvMapping, uv -> uv.wrap(buffer), buffer),
                            isValid,
                            properties
                    ));
                }
        ));
    }

    @Override
    public OrderedSubmitNodeCollector delegate() {
        return delegate;
    }

    @Override
    public <S> void submitModel(Model<? super S> model, S state, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, int tintedColor, @Nullable UvMapping uvMapping, int outlineColor) {
        submitGhostGeometryStack(poseStack, atlas(renderType), uvMapping, (ghostPoseStack, buffer) -> {
            model.setupAnim(state);
            model.renderToBuffer(ghostPoseStack, buffer, lightCoords, overlayCoords, tintedColor);
        });
    }

    @Override
    public void submitCustomGeometry(PoseStack poseStack, RenderType renderType, SubmitNodeCollector.CustomGeometryRenderer renderer) {
        submitGhostGeometry(poseStack, renderType, null, renderer);
    }

    private Identifier atlas(RenderType renderType) {
        var sampler = renderType.state.textures.get("Sampler0");

        if(sampler != null) {
            return sampler.location();
        }

        return TextureAtlas.LOCATION_BLOCKS;
    }
}
