package net.cflip.divena.celestial;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class StarList {
    public static final int NUM_STARS = 1500; // Matches the value in SkyRenderer.buildStars()

    public static final Vec3[] starData = new Vec3[NUM_STARS];

    public static void buildStarList() {
        // NOTE: This star generation algorithm is copied from the SkyRenderer.buildStars() method, therefore it should
        // build the same star positions as what is seen in the sky.
        // If that method is changed for whatever reason (e.g. mixin from another mod), then the "gameplay" list of
        // stars will be out of sync from what is seen in the sky.

        RandomSource random = RandomSource.createThreadLocalInstance(10842L);

        for (int i = 0; i < NUM_STARS; ++i) {
            // 3D points in the range [-1, 1] are generated, which are then normalized.

            float x = random.nextFloat() * 2.0F - 1.0F;
            float y = random.nextFloat() * 2.0F - 1.0F;
            float z = random.nextFloat() * 2.0F - 1.0F;

            float starSize = 0.15F + random.nextFloat() * 0.1F;

            float lengthSq = Mth.lengthSquared(x, y, z);

            // TODO: Since this occasionally fails, there are gaps in the vertex list where the vector is null.
            // We should only have indices for stars that were actually generated
            if (lengthSq > 0.01f && lengthSq < 1.0f) {
                Vector3f starDirection = (new Vector3f(x, y, z)).normalize(100);

                // This value isn't used, but it needs to call random.nextDouble() to match the random number sequence
                // from the other method.
                float zRot = (float) (random.nextDouble() * (double) (float) Math.PI * (double) 2.0F);

                starData[i] = new Vec3(starDirection);
            }
        }
    }

    public static Vec3 getStarVector(int targetStar) {
        return starData[targetStar];
    }
}
