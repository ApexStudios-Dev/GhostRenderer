package dev.apexstudios.ghostrenderer.core;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.apexstudios.ghostrenderer.api.GhostProperties;
import net.minecraft.util.ARGB;
import net.neoforged.neoforge.client.model.pipeline.VertexConsumerWrapper;

public final class GhostVertexConsumer extends VertexConsumerWrapper {
    private final boolean isValid;
    private final GhostProperties properties;

    public GhostVertexConsumer(VertexConsumer parent, boolean isValid, GhostProperties properties) {
        super(parent);

        this.isValid = isValid;
        this.properties = properties;
    }

    @Override
    public VertexConsumer setColor(int r, int g, int b, int a) {
        return super.setColor(ARGB.color(
                properties.fade(isValid),
                properties.tint(ARGB.color(r, g, b), isValid)
        ));
    }

    @Override
    public VertexConsumer setColor(int packedColor) {
        return super.setColor(ARGB.color(
                properties.fade(isValid),
                properties.tint(ARGB.opaque(packedColor), isValid)
        ));
    }
}
