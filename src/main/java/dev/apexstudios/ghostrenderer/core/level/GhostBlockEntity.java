package dev.apexstudios.ghostrenderer.core.level;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public record GhostBlockEntity(
        BlockEntity blockEntity,
        boolean isValid
) {
    public @Nullable GhostBlockEntityRenderState extract(BlockEntityRenderDispatcher dispatcher, float partialTicks, Vec3 cameraPosition) {
        var renderer = dispatcher.getRenderer(blockEntity);

        if(renderer == null) {
            return null;
        }

        var renderState = renderer.createRenderState();
        renderer.extractRenderState(blockEntity, renderState, partialTicks, cameraPosition, null);
        return new GhostBlockEntityRenderState(renderState, isValid);
    }
}
