package net.cflip.divena.item;

import net.cflip.divena.Divena;
import net.cflip.divena.block.DivenaBlocks;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DivenaItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Divena.MODID);

    // Items
    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", p -> p.food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

    // Block Items
    public static final DeferredItem<BlockItem> CM_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("cosmic_transceiver_block", DivenaBlocks.COSMIC_TRANSCEIVER_BLOCK);
    public static final DeferredItem<BlockItem> CELESTIAL_ALTAR_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("celestial_altar_block", DivenaBlocks.CELESTIAL_ALTAR_BLOCK);


}
