package net.cflip.divena.block;

import net.cflip.divena.block.blockentity.CelestialAltarBlockEntity;
import net.cflip.divena.block.blockentity.DivenaBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
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
    protected void neighborChanged(
            @NonNull BlockState state, Level level, @NonNull BlockPos pos, @NonNull Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        if (!level.isClientSide() && level.getBlockState(pos.above()).is(Blocks.FIRE)) {
            if (level.getBlockEntity(pos) instanceof CelestialAltarBlockEntity altar) {
                altar.tryStartRitual();
            }
        }
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
