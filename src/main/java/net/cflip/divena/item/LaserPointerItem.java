package net.cflip.divena.item;

import net.cflip.divena.block.blockentity.CosmicTransceiverBlockEntity;
import net.cflip.divena.celestial.StarList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.NonNull;

// Feel free to change this item in any way. I just chose laser pointer as I needed something to test stuff with.
public class LaserPointerItem extends Item {
    public LaserPointerItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        ItemStack item = player.getItemInHand(hand);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockEntity be = level.getBlockEntity(hitResult.getBlockPos());

            int starIndex = item.getOrDefault(DivenaItems.STAR_INDEX, -1);
            if (starIndex == -1) {
                return InteractionResult.PASS;
            }

            if (!level.isClientSide() && be instanceof CosmicTransceiverBlockEntity transceiver) {
                if (transceiver.connectToStar(starIndex)) {
                    player.sendOverlayMessage(Component.literal("Transceiver connected to #" + starIndex));
                    item.remove(DivenaItems.STAR_INDEX);
                    return InteractionResult.SUCCESS;
                }
            }

            return InteractionResult.PASS;
        }

        // Must be looking at the sky if not using on the transceiver
        if (hitResult.getType() != HitResult.Type.MISS) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide()) {
            float starAngle = level.environmentAttributes().getValue(EnvironmentAttributes.STAR_ANGLE, player.getEyePosition()) * ((float) Math.PI / 180F);
            int star = StarList.findStar(player.getLookAngle(), starAngle);

            if (star == -1) {
                return InteractionResult.PASS;
            }

            player.sendOverlayMessage(Component.literal("Connected to #" + star));
            item.set(DivenaItems.STAR_INDEX, star);
        }

        return InteractionResult.SUCCESS;
    }
}
