package net.cflip.divena.ritual;

import net.cflip.divena.block.blockentity.CelestialAltarBlockEntity;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class KillMonstersTrial extends RitualTrial {
    private static final int DURATION = SharedConstants.TICKS_PER_SECOND * 30;
    private static final int ENEMY_COUNT = 10;

    private final Set<UUID> activeEnemies = new HashSet<>();

    public KillMonstersTrial(Level level, CelestialAltarBlockEntity altar) {
        super(level, altar, ENEMY_COUNT, DURATION);
    }

    @Override
    protected void setup(ServerPlayer player) {
        RandomSource random = RandomSource.create();
        BlockPos blockPos = altar.getBlockPos();

        double xp = blockPos.getX() - player.getX();
        double zp = blockPos.getZ() - player.getZ();
        double playerAngle = Math.atan2(zp, xp);

        for (int i = 0; i < ENEMY_COUNT; i++) {
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
    }

    @Override
    protected void finish(boolean success) {
        if (!success) {
            activeEnemies.forEach(uuid -> {
                Entity enemy = level.getEntity(uuid);
                if (enemy != null && enemy.isAlive()) {
                    level.explode(enemy, enemy.getX(), enemy.getY(), enemy.getZ(), 2.0f, Level.ExplosionInteraction.MOB);
                    enemy.remove(Entity.RemovalReason.KILLED);
                }
            });
        }
    }

    @Override
    protected int updateRemainingObjectives() {
        activeEnemies.removeIf(uuid -> {
            Entity enemy = level.getEntity(uuid);
            return enemy == null || !enemy.isAlive();
        });
        return activeEnemies.size();
    }

    @Override
    protected Component getObjectiveSubtitle() {
        return Component.literal("Defeat 10 zombies in 30 seconds");
    }

    @Override
    protected Component getRemainingObjectivesMessage(int remaining, int total) {
        return Component.literal(remaining + "/" + total + " zombies left");
    }
}
