package net.cflip.divena.celestial;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class StarList {
    // How many stars the loop will attempt to generate, matches the value in SkyRenderer.buildStars()
    private static final int GENERATE_COUNT = 1500;

    // How many stars are actually generated, since the loop skips some positions.
    // Since the same sequence of stars is always generated, this value is known ahead of time.
    public static final int NUM_STARS = 780;

    private static final float[] positions = new float[NUM_STARS * 3];

    public static void buildStarList() {
        // NOTE: This star generation algorithm is copied from the SkyRenderer.buildStars() method, therefore it should
        // build the same star positions as what is seen in the sky.
        // If that method is changed for whatever reason (e.g. mixin from another mod), then the "gameplay" list of
        // stars will be out of sync from what is seen in the sky.

        RandomSource random = RandomSource.createThreadLocalInstance(10842L);
        int starId = 0;

        for (int i = 0; i < GENERATE_COUNT; ++i) {
            // 3D points in the range [-1, 1] are generated, which are then normalized.

            float x = random.nextFloat() * 2.0F - 1.0F;
            float y = random.nextFloat() * 2.0F - 1.0F;
            float z = random.nextFloat() * 2.0F - 1.0F;

            // This value isn't used, but it needs to call random.nextDouble() to match the random number sequence
            // from the other method.
            float starSize = 0.15F + random.nextFloat() * 0.1F;

            float lengthSq = Mth.lengthSquared(x, y, z);

            if (lengthSq > 0.01f && lengthSq < 1.0f) {
                Vec3 starDirection = (new Vec3(x, y, z)).normalize().yRot(-Mth.PI / 2.0f);

                // Another unused variable, see comment above
                float zRot = (float) (random.nextDouble() * (double) (float) Math.PI * (double) 2.0F);

                positions[starId * 3]     = (float) starDirection.x;
                positions[starId * 3 + 1] = (float) starDirection.y;
                positions[starId * 3 + 2] = (float) starDirection.z;

                starId++;
            }
        }

        assert starId == NUM_STARS;
    }

    public static Vec3 getStarVector(int star) {
        return new Vec3(positions[star * 3], positions[star * 3 + 1], positions[star * 3 + 2]);
    }

    public static int findStar(Vec3 lookAngle, float starAngle) {
        for (int i = 0; i < NUM_STARS; i++) {
            Vec3 starVec = StarList.getStarVector(i).zRot(-starAngle);
            if (lookAngle.dot(starVec) > 0.9999) {
                return i;
            }
        }
        return -1;
    }
}
