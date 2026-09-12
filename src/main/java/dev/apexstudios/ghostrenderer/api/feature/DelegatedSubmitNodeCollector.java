package dev.apexstudios.ghostrenderer.api.feature;

import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;

public interface DelegatedSubmitNodeCollector extends DelegatedOrderedSubmitNodeCollector, SubmitNodeCollector {
    @Override
    SubmitNodeCollector delegate();

    @Override
    default OrderedSubmitNodeCollector order(int order) {
        return delegate().order(order);
    }
}
