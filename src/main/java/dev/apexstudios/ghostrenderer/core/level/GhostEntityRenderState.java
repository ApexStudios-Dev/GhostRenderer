package dev.apexstudios.ghostrenderer.core.level;

import net.minecraft.client.renderer.entity.state.EntityRenderState;

public record GhostEntityRenderState(
        EntityRenderState renderState,
        boolean isValid
) { }
