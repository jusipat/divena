package net.cflip.divena.ritual;

import net.cflip.divena.block.blockentity.CelestialAltarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RitualTrial {
    private static final int DURATION = 20 * 30;
    private static final int ENEMY_COUNT = 10;

    private final ServerBossEvent bossEvent = new ServerBossEvent(
            UUID.randomUUID(), Component.literal("Transception Ceremony"), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_12
    );

    private final Set<UUID> activeEnemies = new HashSet<>();

    private final int totalEnemies;
    private final int totalTime;

    private int remainingEnemies;
    private int remainingTime;

    public RitualTrial() {
        remainingEnemies = totalEnemies = ENEMY_COUNT;
        remainingTime = totalTime = DURATION;
    }

    private void showPopup(ServerPlayer player, @NonNull Component title, @Nullable Component subtitle) {
        // For some reason there isn't a proper interface for showing titles in the Java code
        // so you just have to rawdog it with packets
        player.connection.send(new ClientboundSetTitleTextPacket(title));
        if (subtitle != null) {
            player.connection.send(new ClientboundSetSubtitleTextPacket(subtitle));
        }
    }

    public void begin(Level level, ServerPlayer player) {
        showPopup(player, Component.literal("Ritual Challenge"), Component.literal("Defeat 10 zombies in 30 seconds"));

        for (int i = 0; i < totalEnemies; i++) {
            LivingEntity enemy = new Zombie(level);
            enemy.setPos(player.position());
            enemy.addEffect(new MobEffectInstance(MobEffects.GLOWING, DURATION, 0));

            activeEnemies.add(enemy.getUUID());
            level.addFreshEntity(enemy);
        }

        bossEvent.addPlayer(player);
        bossEvent.setCreateWorldFog(true);
        bossEvent.setVisible(true);
    }

    // Try not to call directly, tell the altar to call this instead
    public void end() {
        bossEvent.removeAllPlayers();
        bossEvent.setVisible(false);
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState, CelestialAltarBlockEntity altar) {
        remainingTime--;

        if (remainingTime <= 0) {
            if (remainingEnemies > 0) {
                activeEnemies.forEach(uuid -> {
                    Entity enemy = level.getEntity(uuid);
                    if (enemy != null && enemy.isAlive()) {
                        level.explode(enemy, enemy.getX(), enemy.getY(), enemy.getZ(), 2.0f, Level.ExplosionInteraction.MOB);
                        enemy.remove(Entity.RemovalReason.KILLED);
                    }
                });
            }
            altar.endRitual();
        }

        activeEnemies.removeIf(uuid -> {
            Entity enemy = level.getEntity(uuid);
            return enemy == null || !enemy.isAlive();
        });
        remainingEnemies = activeEnemies.size();

        if (remainingEnemies == 0) {
            altar.endRitual();
            return;
        }

        bossEvent.setName(Component.literal(remainingEnemies + "/" + totalEnemies + " zombies left"));
        bossEvent.setProgress((float) remainingTime / (float) totalTime);
    }
}
