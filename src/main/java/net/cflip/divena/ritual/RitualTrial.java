package net.cflip.divena.ritual;

import net.cflip.divena.block.blockentity.CelestialAltarBlockEntity;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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

    public void begin(Level level, ServerPlayer player, BlockPos blockPos) {
        showPopup(player, Component.literal("Ritual Challenge"), Component.literal("Defeat 10 zombies in 30 seconds"));
        level.playSound(null, blockPos, SoundEvents.WITHER_SPAWN, SoundSource.BLOCKS);

        RandomSource random = RandomSource.create();

        double xp = blockPos.getX() - player.getX();
        double zp = blockPos.getZ() - player.getZ();
        double playerAngle = Math.atan2(zp, xp);

        for (int i = 0; i < totalEnemies; i++) {
            LivingEntity enemy = new Zombie(level);

            double angle = playerAngle + (random.nextDouble() * 2.0f - 1.0f) * Mth.HALF_PI;
            double dist = 10.0f + random.nextDouble() * 5.0f;

            double x = blockPos.getX() + Math.sin(angle) * dist;
            double y = blockPos.getY();
            double z = blockPos.getZ() + Math.cos(angle) * dist;

            enemy.setPos(x, y, z);
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
                level.playSound(null, blockPos, SoundEvents.ANVIL_DESTROY, SoundSource.BLOCKS, 10.0f, 1.0f);
            } else {
                level.playSound(null, blockPos, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.BLOCKS, 10.0f, 2.0f);
            }
            altar.endRitual();
            return;
        }

        int lastRemainingEnemies = remainingEnemies;
        activeEnemies.removeIf(uuid -> {
            Entity enemy = level.getEntity(uuid);
            return enemy == null || !enemy.isAlive();
        });
        remainingEnemies = activeEnemies.size();
        if (remainingEnemies < lastRemainingEnemies) {
            level.playSound(null, blockPos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 10.0f, 0.75f);
        }

        if (remainingEnemies == 0) {
            altar.endRitual();
            level.playSound(null, blockPos, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.BLOCKS, 10.0f, 2.0f);
            return;
        }

        int tickSoundRate = SharedConstants.TICKS_PER_SECOND;
        if (remainingTime < 80) {
            tickSoundRate /= 2;
        }
        if (remainingTime % tickSoundRate == 0) {
            level.playSound(null, blockPos, SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.BLOCKS, 10.0f, 1.0f);
        }

        bossEvent.setName(Component.literal(remainingEnemies + "/" + totalEnemies + " zombies left"));
        bossEvent.setProgress((float) remainingTime / (float) totalTime);
    }
}
