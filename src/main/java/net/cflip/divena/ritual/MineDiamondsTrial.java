package net.cflip.divena.ritual;

import net.cflip.divena.block.blockentity.CelestialAltarBlockEntity;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.HashSet;
import java.util.Set;

public class MineDiamondsTrial extends RitualTrial {
    private static final int DURATION = SharedConstants.TICKS_PER_SECOND * 15;
    private static final int BLOCK_COUNT = 10;

    private final Set<BlockPos> blocks = new HashSet<>();

    public MineDiamondsTrial(Level level, CelestialAltarBlockEntity altar) {
        super(level, altar, BLOCK_COUNT, DURATION);
    }

    @Override
    protected void setup(ServerPlayer player) {
        BlockPos blockPos = altar.getBlockPos();
        for (int i = 0; i < BLOCK_COUNT; i++) {
            float angle = (i / (float) BLOCK_COUNT) * Mth.TWO_PI;
            float distance = 5;

            int x = (int) (blockPos.getX() + Mth.sin(angle) * distance);
            int z = (int) (blockPos.getZ() + Mth.cos(angle) * distance);

            BlockPos diamondPos = new BlockPos(x, blockPos.getY(), z);

            blocks.add(diamondPos);
            level.setBlockAndUpdate(diamondPos, Blocks.DIAMOND_ORE.defaultBlockState());
        }
    }

    @Override
    protected void finish(boolean success) {
        if (!success) {
            blocks.forEach(pos -> {
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                PrimedTnt tnt = new PrimedTnt(level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, null);
                level.playSound(null, tnt.getX(), tnt.getY(), tnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1, 1);
                level.addFreshEntity(tnt);
            });
        }
    }

    @Override
    protected int updateRemainingObjectives() {
        blocks.removeIf(pos -> !level.getBlockState(pos).is(Blocks.DIAMOND_ORE));
        return blocks.size();
    }

    @Override
    protected Component getObjectiveSubtitle() {
        return Component.literal("Mine " + BLOCK_COUNT + " diamonds in " + DURATION / SharedConstants.TICKS_PER_SECOND + " seconds");
    }

    @Override
    protected Component getRemainingObjectivesMessage(int remaining, int total) {
        return Component.literal(remaining + "/" + total + " diamonds mined");
    }
}
