package net.cflip.divena.block.blockentity;

import net.cflip.divena.block.DivenaBlocks;
import net.cflip.divena.ritual.RitualTrial;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jspecify.annotations.Nullable;

public class CelestialAltarBlockEntity extends BlockEntity {
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

    private @Nullable RitualTrial ongoingTrial;

    public CelestialAltarBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(DivenaBlockEntities.CELESTIAL_ALTAR_BE.get(), worldPosition, blockState);
    }

    public boolean tryStartRitual() {
        BlockPos pos = getBlockPos();

        if (ongoingTrial != null) {
            return false;
        }

        if (candleCombo.find(level, pos) == null) {
            return false;
        }

        Player player = level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 16, false);
        if (player instanceof ServerPlayer serverPlayer) {
            ongoingTrial = new RitualTrial();
//            level.setData(Divena.RITUAL_TRIAL.get(), ongoingTrial);
            ongoingTrial.begin(level, serverPlayer);
        }

        return true;
    }

    public void endRitual() {
        if (ongoingTrial != null) {
            ongoingTrial.end();
            ongoingTrial = null;
        }
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, CelestialAltarBlockEntity altar) {
        if (altar.ongoingTrial != null) {
            altar.ongoingTrial.tick(level, blockPos, blockState, altar);
        }
    }
}
