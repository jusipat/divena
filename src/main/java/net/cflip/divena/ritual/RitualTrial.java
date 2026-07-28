package net.cflip.divena.ritual;

import net.cflip.divena.block.blockentity.CelestialAltarBlockEntity;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public abstract class RitualTrial {
    private final ServerBossEvent bossEvent = new ServerBossEvent(
            UUID.randomUUID(), Component.empty(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_12
    );

    private final int totalObjectives;
    private final int totalTime;

    private int remainingObjectives;
    private int remainingTime;

    protected Level level;
    protected CelestialAltarBlockEntity altar;

    public RitualTrial(Level level, CelestialAltarBlockEntity altar, int objectives, int duration) {
        this.level = level;
        this.altar = altar;
        remainingObjectives = totalObjectives = objectives;
        remainingTime = totalTime = duration;
    }

    private void showPopup(ServerPlayer player, @NonNull Component title, @Nullable Component subtitle) {
        // For some reason there isn't a proper interface for showing titles in the Java code
        // so you just have to rawdog it with packets
        player.connection.send(new ClientboundSetTitleTextPacket(title));
        if (subtitle != null) {
            player.connection.send(new ClientboundSetSubtitleTextPacket(subtitle));
        }
    }

    protected void playSound(SoundEvent sound, float volume, float pitch) {
        level.playSound(null, altar.getBlockPos(), sound, SoundSource.BLOCKS, volume, pitch);
    }

    public void begin(ServerPlayer player) {
        setup(player);
        showPopup(player, Component.translatable("event.divena.trial.title"), getObjectiveSubtitle());
        playSound(SoundEvents.WITHER_SPAWN, 1.0f, 1.0f);
        bossEvent.addPlayer(player);
        bossEvent.setCreateWorldFog(true);
        bossEvent.setVisible(true);
    }

    // Try not to call directly, tell the altar to call this instead
    public void end(boolean success) {
        finish(success);
        if (success) {
            playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 10.0f, 2.0f);
        } else {
            playSound(SoundEvents.ANVIL_DESTROY, 10.0f, 1.0f);
        }
        bossEvent.removeAllPlayers();
        bossEvent.setVisible(false);
    }

    public void tick() {
        remainingTime--;

        if (remainingTime <= 0) {
            altar.endRitual(remainingObjectives <= 0);
            return;
        }

        int lastRemainingObjectives = remainingObjectives;
        remainingObjectives = updateRemainingObjectives();
        if (remainingObjectives < lastRemainingObjectives) {
            playSound(SoundEvents.FIRECHARGE_USE, 10.0f, 0.75f);
        }

        if (remainingObjectives == 0) {
            altar.endRitual(true);
            return;
        }

        int tickSoundRate = SharedConstants.TICKS_PER_SECOND;
        if (remainingTime < 80) {
            tickSoundRate /= 2;
        }
        if (remainingTime % tickSoundRate == 0) {
            playSound(SoundEvents.LODESTONE_COMPASS_LOCK, 10.0f, 1.0f);
        }

        bossEvent.setName(getRemainingObjectivesMessage(remainingObjectives, totalObjectives));
        bossEvent.setProgress((float) remainingTime / (float) totalTime);
    }

    protected abstract void setup(ServerPlayer player);

    protected abstract void finish(boolean success);

    protected abstract int updateRemainingObjectives();

    protected abstract Component getObjectiveSubtitle();

    protected abstract Component getRemainingObjectivesMessage(int remaining, int total);
}
