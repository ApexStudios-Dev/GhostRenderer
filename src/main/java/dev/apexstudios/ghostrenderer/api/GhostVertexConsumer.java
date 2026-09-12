package dev.apexstudios.ghostrenderer.api;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.ARGB;
import net.minecraft.util.CommonColors;
import net.neoforged.neoforge.client.model.pipeline.VertexConsumerWrapper;

public final class GhostVertexConsumer extends VertexConsumerWrapper {
    private final boolean isValid;

    public GhostVertexConsumer(VertexConsumer parent, boolean isValid) {
        super(parent);

        this.isValid = isValid;
    }

    @Override
    public VertexConsumer setColor(int r, int g, int b, int a) {
        return super.setColor(ARGB.color(
                alpha(a),
                color(ARGB.color(r, g, b), isValid)
        ));
    }

    @Override
    public VertexConsumer setColor(int packedColor) {
        return super.setColor(ARGB.color(
                alpha(ARGB.alpha(packedColor)),
                color(ARGB.opaque(packedColor), isValid)
        ));
    }

    public static int alpha(int a) {
        // TODO: This should be configurable
        return 255;
        //return (a * 190) / 0xFF;
    }

    public static int color(int rgb, boolean isValid) {
        if(isValid) {
            return rgb;
        }

        return ARGB.multiply(ARGB.opaque(rgb), CommonColors.SOFT_RED);
    }
}
