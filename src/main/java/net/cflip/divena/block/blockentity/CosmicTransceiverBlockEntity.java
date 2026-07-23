package net.cflip.divena.block.blockentity;

import net.cflip.divena.block.DivenaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;

public class CosmicTransceiverBlockEntity extends BlockEntity {
    public CosmicTransceiverBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(DivenaBlockEntities.COSMIC_TRANSCEIVER_BE.get(), worldPosition, blockState);
    }

    private static final BlockPattern combo = BlockPatternBuilder.start().aisle(
                    "000",
                    "010",
                    "000")
            .where('1', BlockInWorld.hasState(BlockStatePredicate.forBlock(DivenaBlocks.COSMIC_TRANSCEIVER_BLOCK.get())))
            .where('0', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK))).build();

    public void checkPattern() {
        Level level = getLevel();
        if (level == null) {
            return;
        }

        BlockPattern.BlockPatternMatch match = combo.find(level, getBlockPos());

        if (match != null) {
            System.out.println("Multiblock complete");
        }
    }
}
