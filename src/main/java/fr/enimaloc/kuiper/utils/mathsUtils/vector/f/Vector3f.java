package fr.enimaloc.kuiper.utils.mathsUtils.vector.f;

import fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.d.Vector3d;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.i.Vector3i;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.l.Vector3l;
import java.io.Serializable;
import java.util.Random;

import static fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils.FLT_EPSILON;

public final class Vector3f implements Vectorf, Comparable<Vector3f>, Serializable {

    private static final       long     serialVersionUID = 1;

    public static final        Vector3f ZERO             = new Vector3f(0, 0, 0);
    public static final        Vector3f UNIT_X           = new Vector3f(1, 0, 0);
    public static final        Vector3f UNIT_Y           = new Vector3f(0, 1, 0);
    public static final        Vector3f UNIT_Z           = new Vector3f(0, 0, 1);
    public static final        Vector3f ONE              = new Vector3f(1, 1, 1);
    public static final        Vector3f RIGHT            = Vector3f.UNIT_X;
    public static final        Vector3f UP               = Vector3f.UNIT_Y;
    public static final        Vector3f FORWARD          = Vector3f.UNIT_Z;

    private final              float    x;
    private final              float    y;
    private final              float    z;

    private transient volatile boolean  hashed           = false;
    private transient volatile int      hashCode         = 0;

    public Vector3f(final Vector2f v) {
        this(v, 0);
    }

    public Vector3f(final Vector2f v, final double z) {
        this(v, (float) z);
    }

    public Vector3f(final Vector2f v, final float z) {
        this(v.x(), v.y(), z);
    }

    public Vector3f(final VectorNf v) {
        this(v.get(0), v.get(1), v.size() > 2 ? v.get(2) : 0);
    }

    public Vector3f(final double x, final double y, final double z) {
        this((float) x, (float) y, (float) z);
    }

    public Vector3f(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float x() {
        return this.x;
    }

    public float y() {
        return this.y;
    }

    public float z() {
        return this.z;
    }

    public int floorX() {
        return VectorUtils.floor(this.x);
    }

    public int floorY() {
        return VectorUtils.floor(this.y);
    }

    public int floorZ() {
        return VectorUtils.floor(this.z);
    }

    public Vector3f add(final Vector3f v) {
        return this.add(v.x, v.y, v.z);
    }

    public Vector3f add(final double x, final double y, final double z) {
        return this.add((float) x, (float) y, (float) z);
    }

    public Vector3f add(final float x, final float y, final float z) {
        return new Vector3f(this.x + x, this.y + y, this.z + z);
    }

    public Vector3f sub(final Vector3f v) {
        return this.sub(v.x, v.y, v.z);
    }

    public Vector3f sub(final double x, final double y, final double z) {
        return this.sub((float) x, (float) y, (float) z);
    }

    public Vector3f sub(final float x, final float y, final float z) {
        return new Vector3f(this.x - x, this.y - y, this.z - z);
    }

    public Vector3f mul(final double a) {
        return this.mul((float) a);
    }

    @Override
    public Vector3f mul(final float a) {
        return this.mul(a, a, a);
    }

    public Vector3f mul(final Vector3f v) {
        return this.mul(v.x, v.y, v.z);
    }

    public Vector3f mul(final double x, final double y, final double z) {
        return this.mul((float) x, (float) y, (float) z);
    }

    public Vector3f mul(final float x, final float y, final float z) {
        return new Vector3f(this.x * x, this.y * y, this.z * z);
    }

    public Vector3f div(final double a) {
        return this.div((float) a);
    }

    @Override
    public Vector3f div(final float a) {
        return this.div(a, a, a);
    }

    public Vector3f div(final Vector3f v) {
        return this.div(v.x, v.y, v.z);
    }

    public Vector3f div(final double x, final double y, final double z) {
        return this.div((float) x, (float) y, (float) z);
    }

    public Vector3f div(final float x, final float y, final float z) {
        return new Vector3f(this.x / x, this.y / y, this.z / z);
    }

    public float dot(final Vector3f v) {
        return this.dot(v.x, v.y, v.z);
    }

    public float dot(final double x, final double y, final double z) {
        return this.dot((float) x, (float) y, (float) z);
    }

    public float dot(final float x, final float y, final float z) {
        return this.x * x + this.y * y + this.z * z;
    }

    public Vector3f project(final Vector3f v) {
        return this.project(v.x, v.y, v.z);
    }

    public Vector3f project(final double x, final double y, final double z) {
        return this.project((float) x, (float) y, (float) z);
    }

    public Vector3f project(final float x, final float y, final float z) {
        final float lengthSquared = x * x + y * y + z * z;
        if (Math.abs(lengthSquared) < FLT_EPSILON) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        final float a = this.dot(x, y, z) / lengthSquared;
        return new Vector3f(a * x, a * y, a * z);
    }

    public Vector3f cross(final Vector3f v) {
        return this.cross(v.x, v.y, v.z);
    }

    public Vector3f cross(final double x, final double y, final double z) {
        return this.cross((float) x, (float) y, (float) z);
    }

    public Vector3f cross(final float x, final float y, final float z) {
        return new Vector3f(this.y * z - this.z * y, this.z * x - this.x * z, this.x * y - this.y * x);
    }

    public Vector3f pow(final double pow) {
        return this.pow((float) pow);
    }

    @Override
    public Vector3f pow(final float power) {
        return new Vector3f(Math.pow(this.x, power), Math.pow(this.y, power), Math.pow(this.z, power));
    }

    @Override
    public Vector3f ceil() {
        return new Vector3f(Math.ceil(this.x), Math.ceil(this.y), Math.ceil(this.z));
    }

    @Override
    public Vector3f floor() {
        return new Vector3f(VectorUtils.floor(this.x), VectorUtils.floor(this.y), VectorUtils.floor(this.z));
    }

    @Override
    public Vector3f round() {
        return new Vector3f(Math.round(this.x), Math.round(this.y), Math.round(this.z));
    }

    @Override
    public Vector3f abs() {
        return new Vector3f(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }

    @Override
    public Vector3f negate() {
        return new Vector3f(-this.x, -this.y, -this.z);
    }

    public Vector3f min(final Vector3f v) {
        return this.min(v.x, v.y, v.z);
    }

    public Vector3f min(final double x, final double y, final double z) {
        return this.min((float) x, (float) y, (float) z);
    }

    public Vector3f min(final float x, final float y, final float z) {
        return new Vector3f(Math.min(this.x, x), Math.min(this.y, y), Math.min(this.z, z));
    }

    public Vector3f max(final Vector3f v) {
        return this.max(v.x, v.y, v.z);
    }

    public Vector3f max(final double x, final double y, final double z) {
        return this.max((float) x, (float) y, (float) z);
    }

    public Vector3f max(final float x, final float y, final float z) {
        return new Vector3f(Math.max(this.x, x), Math.max(this.y, y), Math.max(this.z, z));
    }

    public float distanceSquared(final Vector3f v) {
        return this.distanceSquared(v.x, v.y, v.z);
    }

    public float distanceSquared(final double x, final double y, final double z) {
        return this.distanceSquared((float) x, (float) y, (float) z);
    }

    public float distanceSquared(final float x, final float y, final float z) {
        final float dx = this.x - x;
        final float dy = this.y - y;
        final float dz = this.z - z;
        return dx * dx + dy * dy + dz * dz;
    }

    public float distance(final Vector3f v) {
        return this.distance(v.x, v.y, v.z);
    }

    public float distance(final double x, final double y, final double z) {
        return this.distance((float) x, (float) y, (float) z);
    }

    public float distance(final float x, final float y, final float z) {
        return (float) Math.sqrt(this.distanceSquared(x, y, z));
    }

    @Override
    public float lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    @Override
    public float length() {
        return (float) Math.sqrt(this.lengthSquared());
    }

    @Override
    public Vector3f normalize() {
        final float length = this.length();
        if (Math.abs(length) < FLT_EPSILON) {
            throw new ArithmeticException("Cannot normalize the zero vector");
        }
        return new Vector3f(this.x / length, this.y / length, this.z / length);
    }

    /**
     * Returns the axis with the minimal value.
     *
     * @return {@link int} axis with minimal value
     */
    @Override
    public int minAxis() {
        return this.x < this.y ? (this.x < this.z ? 0 : 2) : (this.y < this.z ? 1 : 2);
    }

    /**
     * Returns the axis with the maximum value.
     *
     * @return {@link int} axis with maximum value
     */
    @Override
    public int maxAxis() {
        return this.x < this.y ? (this.y < this.z ? 2 : 1) : (this.x < this.z ? 2 : 0);
    }

    public Vector2f toVector2() {
        return new Vector2f(this);
    }

    public Vector2f toVector2(final boolean useZ) {
        return new Vector2f(this.x, useZ ? this.z : this.y);
    }

    public VectorNf toVectorN() {
        return new VectorNf(this);
    }

    @Override
    public float[] toArray() {
        return new float[]{this.x, this.y, this.z};
    }

    @Override
    public Vector3i toInt() {
        return new Vector3i(this.x, this.y, this.z);
    }

    @Override
    public Vector3l toLong() {
        return new Vector3l(this.x, this.y, this.z);
    }

    @Override
    public Vector3f toFloat() {
        return new Vector3f(this.x, this.y, this.z);
    }

    @Override
    public Vector3d toDouble() {
        return new Vector3d(this.x, this.y, this.z);
    }

    @Override
    public int compareTo(final Vector3f v) {
        return (int) Math.signum(this.lengthSquared() - v.lengthSquared());
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof final Vector3f that)) {
            return false;
        }
        if (Float.compare(that.x, this.x) != 0) {
            return false;
        }
        if (Float.compare(that.y, this.y) != 0) {
            return false;
        }
        return Float.compare(that.z, this.z) == 0;
    }

    @Override
    public int hashCode() {
        if (!this.hashed) {
            int result = (this.x != +0.0f ? Float.hashCode(this.x) : 0);
            result        = 31 * result + (this.y != +0.0f ? Float.hashCode(this.y) : 0);
            this.hashCode = 31 * result + (this.z != +0.0f ? Float.hashCode(this.z) : 0);
            this.hashed   = true;
        }
        return this.hashCode;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public static Vector3f from(final float n) {
        return n == 0 ? Vector3f.ZERO : new Vector3f(n, n, n);
    }

    public static Vector3f from(final float x, final float y, final float z) {
        return x == 0 && y == 0 && z == 0 ? Vector3f.ZERO : new Vector3f(x, y, z);
    }

    /**
     * Gets the direction vector of a random pitch and yaw using the random specified.
     *
     * @param random to use
     * @return the random direction vector
     */
    public static Vector3f createRandomDirection(final Random random) {
        return Vector3f.createDirectionRad(random.nextFloat() * (float) VectorUtils.TWO_PI,
                                           random.nextFloat() * (float) VectorUtils.TWO_PI);
    }

    /**
     * Gets the direction vector of a certain theta and phi in degrees. This uses the standard math spherical coordinate system.
     *
     * @param theta in degrees
     * @param phi   in degrees
     * @return the direction vector
     */
    public static Vector3f createDirectionDeg(final double theta, final double phi) {
        return Vector3f.createDirectionDeg((float) theta, (float) phi);
    }

    /**
     * Gets the direction vector of a certain theta and phi in degrees. This uses the standard math spherical coordinate system.
     *
     * @param theta in degrees
     * @param phi   in degrees
     * @return the direction vector
     */
    public static Vector3f createDirectionDeg(final float theta, final float phi) {
        return Vector3f.createDirectionRad((float) Math.toRadians(theta), (float) Math.toRadians(phi));
    }

    /**
     * Gets the direction vector of a certain theta and phi in radians. This uses the standard math spherical coordinate system.
     *
     * @param theta in radians
     * @param phi   in radians
     * @return the direction vector
     */
    public static Vector3f createDirectionRad(final double theta, final double phi) {
        return Vector3f.createDirectionRad((float) theta, (float) phi);
    }

    /**
     * Gets the direction vector of a certain theta and phi in radians. This uses the standard math spherical coordinate system.
     *
     * @param theta in radians
     * @param phi   in radians
     * @return the direction vector
     */
    public static Vector3f createDirectionRad(final float theta, final float phi) {
        final float f = VectorUtils.sin(phi);
        return new Vector3f(f * VectorUtils.cos(theta), f * VectorUtils.sin(theta), VectorUtils.cos(phi));
    }

}