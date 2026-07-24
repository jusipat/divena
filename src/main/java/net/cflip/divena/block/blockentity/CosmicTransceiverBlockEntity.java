package net.cflip.divena.block.blockentity;

import net.cflip.divena.block.DivenaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class CosmicTransceiverBlockEntity extends BlockEntity {
    private int targetStar = -1;

    public CosmicTransceiverBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(DivenaBlockEntities.COSMIC_TRANSCEIVER_BE.get(), worldPosition, blockState);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        this.targetStar = input.getIntOr("target_star", -1);
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("target_star", targetStar);
    }

    @Override
    public @NonNull CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private static final BlockPattern combo = BlockPatternBuilder.start().aisle(
                    "000",
                    "010",
                    "000")
            .where('1', BlockInWorld.hasState(BlockStatePredicate.forBlock(DivenaBlocks.COSMIC_TRANSCEIVER_BLOCK.get())))
            .where('0', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK))).build();

    public boolean checkPattern() {
        //            System.out.println("Multiblock complete");
        return combo.find(level, getBlockPos()) != null;
    }

    public boolean connectToStar(int star) {
        if (!checkPattern()) {
            return false;
        }
        targetStar = star;
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        return true;
    }

    public int getTargetStar() {
        return targetStar;
    }
}
