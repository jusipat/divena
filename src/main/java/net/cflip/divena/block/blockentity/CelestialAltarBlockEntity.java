package net.cflip.divena.block.blockentity;

import net.cflip.divena.block.DivenaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class CelestialAltarBlockEntity extends BlockEntity {
    public CelestialAltarBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(DivenaBlockEntities.CELESTIAL_ALTAR_BE.get(), worldPosition, blockState);
    }

    private static final BlockPattern candleCombo = BlockPatternBuilder.start().aisle(
                    "000",
                    "010",
                    "000")
            .where('1', BlockInWorld.hasState(BlockStatePredicate.forBlock(DivenaBlocks.CELESTIAL_ALTAR_BLOCK.get())))
            .where('0', BlockInWorld.hasState(state ->
                    state.is(BlockTags.CANDLES) &&
                            state.hasProperty(CandleBlock.CANDLES) &&
                            state.getValue(BlockStateProperties.LIT) &&
                            state.getValue(CandleBlock.CANDLES) == 4
            )).build();

    public void checkPattern() {
        Level level = getLevel();
        if (level == null) {
            return;
        }

        BlockPattern.BlockPatternMatch match = candleCombo.find(level, getBlockPos());

        if (match != null) {
            System.out.println("Multiblock complete");
        }
    }
}
