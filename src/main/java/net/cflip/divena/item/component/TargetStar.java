package net.cflip.divena.item.component;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public record TargetStar(int id) implements TooltipProvider {
    public static final Codec<TargetStar> CODEC = Codec.INT.xmap(TargetStar::new, TargetStar::id);
    public static final StreamCodec<ByteBuf, TargetStar> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(TargetStar::new, TargetStar::id);

    @Override
    public void addToTooltip(Item.@NonNull TooltipContext context, Consumer<Component> consumer, @NonNull TooltipFlag flag, @NonNull DataComponentGetter components) {
        consumer.accept(Component.literal("Tracking star #" + id).withStyle(ChatFormatting.GRAY));
    }
}
