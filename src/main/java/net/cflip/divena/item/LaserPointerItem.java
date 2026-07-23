package net.cflip.divena.item;

import net.cflip.divena.celestial.StarList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class LaserPointerItem extends Item {
    public LaserPointerItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        if (!level.isClientSide()) {
            float starAngle = level.environmentAttributes().getValue(EnvironmentAttributes.STAR_ANGLE, player.getEyePosition()) * ((float) Math.PI / 180F);
            int star = StarList.findStar(player.getLookAngle(), starAngle);
            if (star == -1) {
                player.sendOverlayMessage(Component.literal("No star"));
                return InteractionResult.FAIL;
            }
            player.sendOverlayMessage(Component.literal("Star #" + star));
        }

        return InteractionResult.SUCCESS;
    }
}
