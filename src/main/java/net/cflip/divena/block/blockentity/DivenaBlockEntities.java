package net.cflip.divena.block.blockentity;

import net.cflip.divena.Divena;
import net.cflip.divena.block.DivenaBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DivenaBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Divena.MODID);

    public static final Supplier<BlockEntityType<CosmicTransceiverBlockEntity>> COSMIC_TRANSCEIVER_BE = BLOCK_ENTITY_TYPES.register(
            "cosmic_transceiver_be",
            // The block entity type.
            () -> new BlockEntityType<>(
                    // The supplier to use for constructing the block entity instances.
                    CosmicTransceiverBlockEntity::new,
                    // An optional value that, when true, only allows players with OP permissions
                    // to load NBT data (e.g. placing a block item)
                    false,
                    // A vararg of blocks that can have this block entity.
                    // This assumes the existence of the referenced blocks as DeferredBlock<Block>s.
                    DivenaBlocks.COSMIC_TRANSCEIVER_BLOCK.get()
            )
    );

    public static final Supplier<BlockEntityType<CelestialAltarBlockEntity>> CELESTIAL_ALTAR_BE = BLOCK_ENTITY_TYPES.register(
            "celestial_altar_be",
            // The block entity type.
            () -> new BlockEntityType<>(
                    // The supplier to use for constructing the block entity instances.
                    CelestialAltarBlockEntity::new,
                    // An optional value that, when true, only allows players with OP permissions
                    // to load NBT data (e.g. placing a block item)
                    false,
                    // A vararg of blocks that can have this block entity.
                    // This assumes the existence of the referenced blocks as DeferredBlock<Block>s.
                    DivenaBlocks.CELESTIAL_ALTAR_BLOCK.get()
            )
    );
}
