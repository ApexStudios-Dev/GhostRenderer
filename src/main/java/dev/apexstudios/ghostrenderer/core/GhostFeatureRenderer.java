package dev.apexstudios.ghostrenderer.core;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.AddressMode;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexSorting;
import dev.apexstudios.ghostrenderer.api.GhostBlockAndTintGetter;
import dev.apexstudios.ghostrenderer.api.GhostRenderer;
import dev.apexstudios.ghostrenderer.api.GhostVertexConsumer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.BiFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.StagedVertexBuffer;
import net.minecraft.client.renderer.block.BlockQuadOutput;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.FeatureRenderer;
import net.minecraft.client.renderer.feature.FeatureRendererType;
import net.minecraft.client.renderer.feature.submit.TranslucentSubmit;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

public final class GhostFeatureRenderer implements FeatureRenderer<GhostFeatureRenderer.Submit> {
    private final BiFunction<Boolean, BlockColors, ModelBlockRenderer> rendererCache = Util.memoize((ao, colors) -> new ModelBlockRenderer(ao, false, colors));
    private final List<StagedVertexBuffer.Draw> draws = new ArrayList<>();
    private @Nullable GpuBufferSlice dynamicTransforms = null;

    GhostFeatureRenderer() { }

    @SuppressWarnings("resource")
    @Override
    public void prepareGroup(FeatureFrameContext context, List<Submit> submits, boolean strictlyOrdered) {
        if(submits.isEmpty()) {
            return;
        }

        var renderer = rendererCache.apply(
                Minecraft.getInstance().options.ambientOcclusion().get(),
                context.blockColors()
        );

        var vertexBuffer = context.stagedVertexBuffer();
        var draw = vertexBuffer.appendDraw(DefaultVertexFormat.BLOCK, PrimitiveTopology.QUADS, VertexSorting.DISTANCE_TO_ORIGIN);
        var vanillaBuffer = vertexBuffer.getVertexBuilder(draw);

        for(var submit : submits) {
            submit.level.forEach((pos, ghost) -> {
                var blockPos = submit.pose.copy();
                var blockState = ghost.blockState();

                blockPos.translate(pos.getX(), pos.getY(), pos.getZ());

                renderer.tesselateBlock(
                        putQuad(new GhostVertexConsumer(vanillaBuffer, ghost.isValid()), blockPos),
                        0F, 0F, 0F,
                        submit.level,
                        pos,
                        blockState,
                        context.blockStateModelSet().get(blockState),
                        ghost.seed()
                );
            });
        }

        draws.add(draw);
    }

    @Override
    public void finishPrepare(FeatureFrameContext context) {
        dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(RenderSystem.getModelViewMatrixCopy());
    }

    @SuppressWarnings({"resource", "deprecation"})
    @Override
    public void executeGroup(FeatureFrameContext context, int groupIndex, List<Submit> submits, boolean strictlyOrdered) {
        var draw = draws.get(groupIndex);
        var executeInfo = context.stagedVertexBuffer().getExecuteInfo(draw);

        if(executeInfo == null) {
            return;
        }

        var target = OutputTarget.MAIN_TARGET.getRenderTarget();

        try(var renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(
                GhostRenderer.FEATURE_RENDERER_TYPE::name,
                Objects.requireNonNull(target.getColorTextureView()),
                Optional.empty(),
                target.getDepthTextureView(),
                OptionalDouble.empty()
        )) {
            renderPass.setPipeline(RenderPipelines.TRANSLUCENT_BLOCK);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", Objects.requireNonNull(dynamicTransforms));
            renderPass.setVertexBuffer(0, executeInfo.vertexBuffer().slice());
            renderPass.setIndexBuffer(executeInfo.indexBuffer(), executeInfo.indexType());

            renderPass.bindTexture(
                    "Sampler0",
                    context.textureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).getTextureView(),
                    RenderSystem.getSamplerCache().getSampler(AddressMode.CLAMP_TO_EDGE, AddressMode.CLAMP_TO_EDGE, FilterMode.LINEAR, FilterMode.NEAREST, true)
            );

            renderPass.bindTexture(
                    "Sampler2",
                    context.lightmap(),
                    RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)
            );

            renderPass.drawIndexed(executeInfo.indexCount(), 1, executeInfo.firstIndex(), executeInfo.baseVertex(), 0);
        }
    }

    @Override
    public void finishExecute(FeatureFrameContext context) {
        draws.clear();
        dynamicTransforms = null;
    }

    private static BlockQuadOutput putQuad(VertexConsumer buffer, PoseStack.Pose pose) {
        return (x, y, z, quad, instance) -> {
            pose.translate(x, y, z);
            buffer.putBakedQuad(pose, quad, instance);
            pose.translate(-x, -y, -z);
        };
    }

    public record Submit(
            PoseStack.Pose pose,
            GhostBlockAndTintGetter level
    ) implements TranslucentSubmit {
        public Submit {
            pose = pose.copy();
            level = level.immutable();
        }

        @Override
        public float distanceToCameraSq() {
            return TranslucentSubmit.computeDistanceToCameraSq(pose.pose(), .5F, .5F, .5F);
        }

        @Override
        public FeatureRendererType<Submit> featureType() {
            return GhostRenderer.FEATURE_RENDERER_TYPE;
        }
    }
}
