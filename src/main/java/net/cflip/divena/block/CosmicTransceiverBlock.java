package net.cflip.divena.block;

import net.cflip.divena.block.blockentity.CosmicTransceiverBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

// The important part is implementing the EntityBlock interface and overriding the #newBlockEntity method.
    public class CosmicTransceiverBlock extends Block implements EntityBlock {
        // Constructor deferring to super.
        public CosmicTransceiverBlock(BlockBehaviour.Properties properties) {
            super(properties);
        }

        // Return a new instance of our block entity here.
        @Override
        public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
            return new CosmicTransceiverBlockEntity(pos, state);
        }
}