package dev.apexstudios.ghostrenderer.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntitySpawnRequest;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PostSpawnProcessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jspecify.annotations.Nullable;

public interface GhostLevel {
    Level reality();

    Level ghosted();

    boolean setBlockState(BlockPos pos, BlockState blockState, boolean isValid);

    BlockState getBlockState(BlockPos pos);

    default FluidState getFluidState(BlockPos pos) {
        return getBlockState(pos).getFluidState();
    }

    void setBlockEntity(BlockPos pos, BlockState blockState, @Nullable ItemStack components, boolean isValid);

    @Nullable BlockEntity getBlockEntity(BlockPos pos);

    void addEntity(Entity entity, boolean isValid);

    // copy of `EntityType.spawn(ServerLevel level, @Nullable ItemStack itemStack, @Nullable LivingEntity user, BlockPos spawnPos, EntitySpawnReason spawnReason, boolean tryMoveDown, boolean movedUp)`
    // entities are *NOT* ghosted for you, you must call `addEntity`
    @SuppressWarnings("UnnecessaryLocalVariable")
    default @Nullable <TEntity extends Entity> TEntity createEntity(
            EntityType<TEntity> entityType,
            @Nullable ItemStack stack,
            @Nullable LivingEntity user,
            BlockPos spawnPos,
            EntitySpawnReason spawnReason,
            boolean tryMoveDown,
            boolean movedUp
    ) {
        var postSpawnConfig = stack == null ? PostSpawnProcessor.<TEntity>nop() : EntityType.<TEntity>createDefaultStackConfig(reality(), stack, user);
        var entity = createEntity(entityType, postSpawnConfig, spawnPos, spawnReason, tryMoveDown, movedUp);

        /*if(entity != null) {
            entity.execute(ent -> {
                reality.addFreshEntityWithPassengers(ent);

                if(ent instanceof Mob mob) {
                    mob.playAmbientSound();
                }
            });
        }*/

        return entity;
    }

    // copy of `EntityType.create(ServerLevel level, @Nullable PostSpawnProcessor<TEntity> postSpawnConfig, BlockPos spawnPos, EntitySpawnReason spawnReason, boolean tryMoveDown, boolean movedUp)`
    // entities are *NOT* ghosted for you, you must call `addEntity`
    default @Nullable <TEntity extends Entity> TEntity createEntity(
            EntityType<TEntity> entityType,
            @Nullable PostSpawnProcessor<TEntity> postSpawnConfig,
            BlockPos spawnPos,
            EntitySpawnReason spawnReason,
            boolean tryMoveDown,
            boolean movedUp
    ) {
        var level = reality();
        // var entity = entityType.create(reality, spawnReason);
        // we want to ignore as many checks that short circuit and return null
        var entity = entityType.create(level, new EntitySpawnRequest(spawnReason, true));

        if(entity == null) {
            return null;
        }

        var x = spawnPos.getX();
        var y = spawnPos.getY();
        var z = spawnPos.getZ();
        var yOff = 0D;

        if(tryMoveDown) {
            entity.setPos(x + .5D, y + 1D, z + .5D);
            yOff = EntityType.getYOffset(level, spawnPos, movedUp, entity.getBoundingBox());
        }

        entity.snapTo(x + .5D, y + yOff, z + .5D, 0F, 0F);

        // var cancelled = false;

        if(entity instanceof Mob mob) {
            mob.yHeadRot = mob.getYRot();
            mob.yBodyRot = mob.getYRot();
            // vanilla does not set these but they are needed to fix rotation flickers
            mob.yHeadRotO = mob.yRotO;
            mob.yBodyRotO = mob.yRotO;
            // TODO: If we ever get a FakeServerLevel invoke this method to apply random armor and the like to mobs
            /*mob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.SPAWN_ITEM_USE, null);

            if(mob.isSpawnCancelled()) {
                cancelled = true;
            }*/
        }

        if(postSpawnConfig != null) {
            postSpawnConfig.apply(entity);
        }

        return entity;
    }
}
