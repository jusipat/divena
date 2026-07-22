package net.cflip.divena.block;

import net.cflip.divena.Divena;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DivenaBlocks {
    // Create a Deferred Register to hold Blocks which will all be registered under the "divena" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Divena.MODID);

    // Blocks
    public static final DeferredBlock<CosmicTransceiverBlock> COSMIC_TRANSCEIVER_BLOCK = BLOCKS.registerBlock(
            "cosmic_transceiver_block",
            CosmicTransceiverBlock::new, // The factory that the properties will be passed into.
            BlockBehaviour.Properties::of // The supplied properties to use.
    );
}
