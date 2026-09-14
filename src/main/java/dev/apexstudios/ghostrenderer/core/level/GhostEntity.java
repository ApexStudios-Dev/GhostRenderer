package dev.apexstudios.ghostrenderer.core.level;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.Nullable;

public record GhostEntity(
        Entity entity,
        boolean isValid
) {
    @SuppressWarnings({"ConstantValue", "DataFlowIssue"})
    public @Nullable GhostEntityRenderState extract(EntityRenderDispatcher dispatcher, float partialTicks) {
        var renderer = dispatcher.getRenderer(entity);

        // not marked nullable but it technicly is
        if(renderer == null) {
            return null;
        }

        return new GhostEntityRenderState(renderer.createRenderState(entity, partialTicks), isValid);
    }
}
