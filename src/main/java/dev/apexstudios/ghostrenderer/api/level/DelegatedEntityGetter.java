package dev.apexstudios.ghostrenderer.api.level;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

public interface DelegatedEntityGetter extends EntityGetter {
    EntityGetter delegate();

    @Override
    default List<Entity> getEntities(@Nullable Entity except, AABB bb, Predicate<? super Entity> selector) {
        return delegate().getEntities(except, bb, selector);
    }

    @Override
    default <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector) {
        return delegate().getEntities(type, bb, selector);
    }

    @Override
    default List<? extends Player> players() {
        return delegate().players();
    }
}
