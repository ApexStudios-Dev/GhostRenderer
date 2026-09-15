package dev.apexstudios.ghostrenderer.api;

import net.minecraft.util.ARGB;
import net.minecraft.util.CommonColors;
import net.minecraft.util.TriState;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Player;

public interface GhostProperties {
    default int tint(int rgb, boolean isValid) {
        if(isValid) {
            return rgb;
        }

        return ARGB.multiply(rgb, CommonColors.RED);
    }

    default int fade(boolean isValid) {
        // breathing (fade up and down)
        var time = Util.getMillis();
        var speed = .003D;
        var minAlpha = 145;
        var maxAlpha = 215;
        return (int) (minAlpha + (maxAlpha - minAlpha) * (.5D + .5D * Math.sin(time * speed)));

        // scanner (fade down then pop back up)
        /*var duration = 1500L;
        var minAlpha = 30;
        var maxAlpha = 180;
        var progress = (time % duration) / (double) duration;
        return (int) (maxAlpha - (progress * (maxAlpha - minAlpha)));*/

        // triangle (linear up and down)
        /*var cycleLength = 2000L;
        var minAlpha = 115;
        var maxAlpha = 215;
        var progress = (time % cycleLength) / (double) cycleLength;
        var bounce = Math.abs(progress - .5D) * 2D;
        return (int) (minAlpha + (maxAlpha - minAlpha) * bounce);*/
    }

    // for block ghosts
    default TriState useAmbientOcclusion() {
        return TriState.DEFAULT; // default == pull from game settings
    }

    default boolean alwaysOnTop() {
        return true;
    }

    default boolean validPerRender() {
        return true;
    }

    default boolean renderBlockEntities() {
        return true;
    }

    default boolean renderFluids() {
        return true;
    }

    default boolean renderEntities() {
        return true;
    }

    default boolean renderForPlayer(Player player) {
        return true;
    }
}
