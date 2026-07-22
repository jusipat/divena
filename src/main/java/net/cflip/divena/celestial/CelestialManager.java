package net.cflip.divena.celestial;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Vector3f;

public class CelestialManager {
    public static final int NUM_STARS = 1500;

    // Star positions using the equatorial coordinate system
    public static final double ra[]  = new double[NUM_STARS];
    public static final double dec[] = new double[NUM_STARS];

    public static void buildStarList() {
        RandomSource rng = RandomSource.createThreadLocalInstance(12345L);
        for (int i = 0; i < NUM_STARS; i++) {
            ra[i]  =  rng.nextDouble() * Math.TAU;
            dec[i] = (rng.nextDouble() * 2.0 - 1.0) * (Math.PI / 2);
        }
    }

    public static Vector3f getSkyRenderVector(int i) {
        final float distance = 100.0f;

        float ra_x = Mth.sin(ra[i]);
        float ra_z = Mth.cos(ra[i]);

        float y = Mth.sin(dec[i]);
        float x = Mth.cos(dec[i]) * ra_x;
        float z = Mth.cos(dec[i]) * ra_z;

        return (new Vector3f(x, y, z)).normalize(distance);
    }
}
