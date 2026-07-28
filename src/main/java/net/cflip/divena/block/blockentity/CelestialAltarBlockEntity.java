package net.cflip.divena.block.blockentity;

import net.cflip.divena.block.DivenaBlocks;
import net.cflip.divena.ritual.KillMonstersTrial;
import net.cflip.divena.ritual.MineDiamondsTrial;
import net.cflip.divena.ritual.RitualTrial;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class CelestialAltarBlockEntity extends BlockEntity {
    private static final BlockPattern candleCombo = BlockPatternBuilder.start().aisle(
                    "000",
                    "010",
                    "000")
            .where('1', BlockInWorld.hasState(BlockStatePredicate.forBlock(DivenaBlocks.CELESTIAL_ALTAR.get())))
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

    // Needs to be a separate method from startRitual so the client can test the interaction without starting the ritual locally
    public boolean canStartRitual(Player user) {
        // Not sure when this would ever be the case, but it counts as a fail
        if (level == null) {
            return false;
        }

        // TODO: This check fails and causes desync on the client
        if (ongoingTrial != null) {
            user.sendOverlayMessage(Component.translatable("block.divena.celestial_altar.ritual_ongoing"));
            return false;
        }

        if (candleCombo.find(level, getBlockPos()) == null) {
            user.sendOverlayMessage(Component.translatable("block.divena.celestial_altar.bad_multiblock"));
            return false;
        }
        return true;
    }

    public void startRitual(ServerPlayer user) {
        if (!canStartRitual(user)) {
            return;
        }
        if (level.getRandom().nextBoolean()) {
            ongoingTrial = new MineDiamondsTrial(level, this);
        } else {
            ongoingTrial = new KillMonstersTrial(level, this);
        }
        ongoingTrial.begin(user);
    }

    public void endRitual(boolean success) {
        if (ongoingTrial != null) {
            ongoingTrial.end(success);
            ongoingTrial = null;
        }
    }

    @Override
    public void preRemoveSideEffects(@NonNull BlockPos pos, @NonNull BlockState state) {
        endRitual(false);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, CelestialAltarBlockEntity altar) {
        if (altar.ongoingTrial != null) {
            altar.ongoingTrial.tick();
        }
    }
}
