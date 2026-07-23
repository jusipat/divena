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
    public static final DeferredItem<Item> LASER_POINTER = ITEMS.registerItem("laser_pointer", LaserPointerItem::new);

    // Block Items
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", DivenaBlocks.EXAMPLE_BLOCK);

}
