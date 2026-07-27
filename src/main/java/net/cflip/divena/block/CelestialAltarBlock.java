package net.cflip.divena.block;

import net.cflip.divena.block.blockentity.CelestialAltarBlockEntity;
import net.cflip.divena.block.blockentity.DivenaBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class CelestialAltarBlock extends Block implements EntityBlock {
    public CelestialAltarBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CelestialAltarBlockEntity(pos, state);
    }

    @Override
    protected @NonNull InteractionResult useItemOn(
            @NonNull ItemStack itemStack, @NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos,
            @NonNull Player player, @NonNull InteractionHand hand, BlockHitResult hitResult) {
        if (hitResult.getDirection() != Direction.UP) {
            return InteractionResult.PASS;
        }

        // TODO: Allow any fire-creating item
        if (itemStack.getItem() != Items.FLINT_AND_STEEL) {
            return InteractionResult.PASS;
        }

        if (level.getBlockEntity(pos) instanceof CelestialAltarBlockEntity altar) {
            if (!altar.canStartRitual(player)) {
                return InteractionResult.SUCCESS;
            }
            if (!level.isClientSide()) {
                altar.startRitual((ServerPlayer) player);
            }
            return InteractionResult.PASS;
        }

        return InteractionResult.SUCCESS;
    }

    private static <E extends BlockEntity, A extends BlockEntity> @Nullable
            BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> type, BlockEntityType<E> checkedType, BlockEntityTicker<? super E> ticker) {
        return checkedType == type ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NonNull Level level, @NonNull BlockState state, @NonNull BlockEntityType<T> type) {
        if (level instanceof ServerLevel) {
            return createTickerHelper(type, DivenaBlockEntities.CELESTIAL_ALTAR_BE.get(), CelestialAltarBlockEntity::tick);
        }
        return null;
    }
}
