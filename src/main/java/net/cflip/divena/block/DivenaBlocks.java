package net.cflip.divena.block;

import net.cflip.divena.Divena;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Properties;

public class DivenaBlocks {
    // Create a Deferred Register to hold Blocks which will all be registered under the "divena" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Divena.MODID);

    // Blocks
    public static final DeferredBlock<CosmicTransceiverBlock> COSMIC_TRANSCEIVER = BLOCKS.registerBlock(
            "cosmic_transceiver",
            CosmicTransceiverBlock::new, // The factory that the properties will be passed into.
            BlockBehaviour.Properties::noOcclusion // The supplied properties to use.
    );

    public static final DeferredBlock<CelestialAltarBlock> CELESTIAL_ALTAR = BLOCKS.registerBlock(
            "celestial_altar",
            CelestialAltarBlock::new, // The factory that the properties will be passed into.
            BlockBehaviour.Properties::of // The supplied properties to use.
    );
}
