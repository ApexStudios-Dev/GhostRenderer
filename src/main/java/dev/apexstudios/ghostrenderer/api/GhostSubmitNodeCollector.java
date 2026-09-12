package dev.apexstudios.ghostrenderer.api;

import dev.apexstudios.ghostrenderer.api.feature.DelegatedSubmitNodeCollector;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;

public final class GhostSubmitNodeCollector extends GhostOrderedSubmitNodeCollector implements DelegatedSubmitNodeCollector {
    public GhostSubmitNodeCollector(SubmitNodeCollector delegate, boolean isValid) {
        super(delegate, isValid);
    }

    @Override
    public SubmitNodeCollector delegate() {
        return (SubmitNodeCollector) super.delegate();
    }

    @Override
    public OrderedSubmitNodeCollector order(int order) {
        var delegate = DelegatedSubmitNodeCollector.super.order(order);
        return new GhostOrderedSubmitNodeCollector(delegate, isValid());
    }
}
