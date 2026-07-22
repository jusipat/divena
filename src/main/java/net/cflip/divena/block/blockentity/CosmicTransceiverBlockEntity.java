package net.cflip.divena.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CosmicTransceiverBlockEntity extends BlockEntity {
    public CosmicTransceiverBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(DivenaBlockEntities.COSMIC_TRANSCEIVER_BE.get(), worldPosition, blockState);
    }
}
