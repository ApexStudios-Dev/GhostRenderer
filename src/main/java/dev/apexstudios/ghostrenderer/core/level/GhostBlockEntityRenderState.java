package dev.apexstudios.ghostrenderer.core.level;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public record GhostBlockEntityRenderState(
        BlockEntityRenderState renderState,
        boolean isValid
) { }
