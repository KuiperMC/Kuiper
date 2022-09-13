/*
 * UtilsI
 *
 * 0.0.1
 *
 * 08/09/2022
 */
package fr.enimaloc.kuiper.utils.mathsUtils.vector;

import static java.lang.Math.PI;

/**
 *
 */
public class VectorUtils {

    public static final double TWO_PI = 2 * PI;

    private static final int SIN_BITS = 22;
    private static final int SIN_SIZE = 1 << SIN_BITS;
    private static final int SIN_MASK = SIN_SIZE - 1;
    private static final float[] SIN_TABLE = new float[SIN_SIZE];
    private static final double SIN_CONVERSION_FACTOR = (SIN_SIZE / TWO_PI);
    private static final int COS_OFFSET = SIN_SIZE / 4;

    static {
        for (int i = 0; i < SIN_SIZE; i++) {
            SIN_TABLE[i] = (float) Math.sin((i * TWO_PI) / SIN_SIZE);
        }
    }

    /**
     * Sine calculation using a table.
     *
     * <p><b>No interpolation is performed:</b> Accuracy is up to the 6th decimal place.</p>
     *
     * @param angle the angle in radians
     * @return the sine of the angle
     */
    public static float sin(double angle) {
        return sinRaw(floor(angle * SIN_CONVERSION_FACTOR));
    }

    /**
     * Cosine calculation using a table.
     *
     * <p><b>No interpolation is performed:</b> Accuracy is up to the 6th decimal place.</p>
     *
     * @param angle the angle in radians
     * @return the cosine of the angle
     */
    public static float cos(double angle) {
        return cosRaw(floor(angle * SIN_CONVERSION_FACTOR));
    }

    private static float sinRaw(int idx) {
        return SIN_TABLE[idx & SIN_MASK];
    }

    private static float cosRaw(int idx) {
        return SIN_TABLE[(idx + COS_OFFSET) & SIN_MASK];
    }

    /**
     * A "close to zero" double epsilon value for use
     */
    public static final double DBL_EPSILON = Double.longBitsToDouble(0x3cb0000000000000L);
    /**
     * A "close to zero" float epsilon value for use
     */
    public static final float FLT_EPSILON = Float.intBitsToFloat(0x34000000);

    /**
     * Rounds 'a' down to the closest integer
     *
     * @param a The value to floor
     * @return The closest integer
     */
    public static int floor(final double a) {
        final int y = (int) a;
        if (a < y) {
            return y - 1;
        }
        return y;
    }

    /**
     * Rounds 'a' down to the closest integer
     *
     * @param a The value to floor
     * @return The closest integer
     */
    public static int floor(final float a) {
        final int y = (int) a;
        if (a < y) {
            return y - 1;
        }
        return y;
    }

    /**
     * Rounds 'a' down to the closest long
     *
     * @param a The value to floor
     * @return The closest long
     */
    public static long floorl(final double a) {
        final long y = (long) a;
        if (a < y) {
            return y - 1;
        }
        return y;
    }

    /**
     * Rounds 'a' down to the closest long
     *
     * @param a The value to floor
     * @return The closest long
     */
    public static long floorl(final float a) {
        final long y = (long) a;
        if (a < y) {
            return y - 1;
        }
        return y;
    }
}
