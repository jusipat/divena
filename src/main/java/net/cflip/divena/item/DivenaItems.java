package net.cflip.divena.item;

import net.cflip.divena.Divena;
import net.cflip.divena.block.DivenaBlocks;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DivenaItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Divena.MODID);
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Divena.MODID);

    // Items
    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", p -> p.food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));
    public static final DeferredItem<Item> LASER_POINTER = ITEMS.registerItem("laser_pointer", LaserPointerItem::new);

    // Block Items
    public static final DeferredItem<BlockItem> CM_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("cosmic_transceiver_block", DivenaBlocks.COSMIC_TRANSCEIVER_BLOCK);
    public static final DeferredItem<BlockItem> CELESTIAL_ALTAR_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("celestial_altar_block", DivenaBlocks.CELESTIAL_ALTAR_BLOCK);

    // Data Components
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> STAR_INDEX = DATA_COMPONENTS.registerComponentType(
            "star_index", b -> b.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

}
