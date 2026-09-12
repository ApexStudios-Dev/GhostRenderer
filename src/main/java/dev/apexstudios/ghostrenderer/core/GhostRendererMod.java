package dev.apexstudios.ghostrenderer.core;

import dev.apexstudios.ghostrenderer.api.GhostRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterFeatureRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = GhostRenderer.ID, dist = Dist.CLIENT)
public final class GhostRendererMod {
    public GhostRendererMod(IEventBus modBus) {
        NeoForge.EVENT_BUS.addListener(RegisterFeatureRenderersEvent.class, event -> event.register(GhostRenderer.FEATURE_RENDERER_TYPE, new GhostFeatureRenderer()));
    }
}
