package dev.apexstudios.ghostrenderer.api;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.apexstudios.ghostrenderer.api.feature.DelegatedOrderedSubmitNodeCollector;
import java.util.function.BiConsumer;
import net.minecraft.Optionull;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.CustomFeatureRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.neoforged.neoforge.client.submit.RenderPhaseKeys;
import org.jspecify.annotations.Nullable;

public sealed class GhostOrderedSubmitNodeCollector implements DelegatedOrderedSubmitNodeCollector permits GhostSubmitNodeCollector {
    private final OrderedSubmitNodeCollector delegate;
    private final boolean isValid;

    public GhostOrderedSubmitNodeCollector(OrderedSubmitNodeCollector delegate, boolean isValid) {
        this.delegate = delegate;
        this.isValid = isValid;
    }

    public void submitGhostGeometry(PoseStack poseStack, RenderType renderType, @Nullable TextureAtlasSprite sprite, SubmitNodeCollector.CustomGeometryRenderer renderer) {
        submitGhostGeometryStack(poseStack, renderType, sprite, (ghosePoseStack, buffer) -> renderer.render(ghosePoseStack.last(), buffer));
    }

    @SuppressWarnings("deprecation")
    public void submitGhostGeometryStack(PoseStack poseStack, RenderType renderType, @Nullable TextureAtlasSprite sprite, BiConsumer<PoseStack, VertexConsumer> renderer) {
        var atlas = TextureAtlas.LOCATION_BLOCKS;

        if(sprite == null) {
            var sampler = renderType.state.textures.get("Sampler0");

            if(sampler != null) {
                atlas = sampler.location();
            }
        } else {
            atlas = sprite.atlasLocation();
        }

        submitSpecial(RenderPhaseKeys.ALWAYS_ON_TOP, new CustomFeatureRenderer.Submit(
                poseStack.last().copy(),
                RenderTypes.entityTranslucentCullItemTarget(atlas),
                (pose, buffer) -> {
                    var newPoseStack = new PoseStack();
                    newPoseStack.setIdentity();
                    newPoseStack.last().set(pose);

                    renderer.accept(newPoseStack, new GhostVertexConsumer(
                            Optionull.mapOrDefault(sprite, texture -> texture.wrap(buffer), buffer),
                            isValid
                    ));
                }
        ));
    }

    public boolean isValid() {
        return isValid;
    }

    @Override
    public OrderedSubmitNodeCollector delegate() {
        return delegate;
    }

    @Override
    public <S> void submitModel(Model<? super S> model, S state, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, int tintedColor, @Nullable TextureAtlasSprite sprite, int outlineColor, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        submitGhostGeometryStack(poseStack, renderType, sprite, (ghostPoseStack, buffer) -> {
            model.setupAnim(state);
            model.renderToBuffer(ghostPoseStack, buffer, lightCoords, overlayCoords, tintedColor);
        });
    }

    @Override
    public void submitCustomGeometry(PoseStack poseStack, RenderType renderType, SubmitNodeCollector.CustomGeometryRenderer renderer) {
        submitGhostGeometry(poseStack, renderType, null, renderer);
    }
}
