package net.cflip.divena.block;

import net.cflip.divena.block.blockentity.CosmicTransceiverBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

    public class CosmicTransceiverBlock extends Block implements EntityBlock {
        public CosmicTransceiverBlock(BlockBehaviour.Properties properties) {
            super(properties);
        }

        // Return a new instance of our block entity here.
        @Override
        public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
            return new CosmicTransceiverBlockEntity(pos, state);
        }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if(!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof CosmicTransceiverBlockEntity tr) {
                tr.checkPattern();
            }
        }
    }
}