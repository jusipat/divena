package net.cflip.divena.item;

import net.cflip.divena.Divena;
import net.cflip.divena.block.DivenaBlocks;
import net.cflip.divena.item.component.TargetStar;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
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
    public static final DeferredItem<Item> LASER_POINTER = ITEMS.registerItem("laser_pointer", LaserPointerItem::new);

    // Block Items
    public static final DeferredItem<BlockItem> CM_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("cosmic_transceiver", DivenaBlocks.COSMIC_TRANSCEIVER);
    public static final DeferredItem<BlockItem> CELESTIAL_ALTAR_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("celestial_altar", DivenaBlocks.CELESTIAL_ALTAR);

    // Data Components
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<TargetStar>> TARGET_STAR = DATA_COMPONENTS.registerComponentType(
            "target_star", b -> b.persistent(TargetStar.CODEC).networkSynchronized(TargetStar.STREAM_CODEC));

}
