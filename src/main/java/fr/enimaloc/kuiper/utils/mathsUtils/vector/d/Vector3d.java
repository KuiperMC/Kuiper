package fr.enimaloc.kuiper.utils.mathsUtils.vector.d;

import fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.f.Vector3f;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.i.Vector3i;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.l.Vector3l;
import java.io.Serializable;
import java.util.Random;

import static fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils.DBL_EPSILON;

public final class Vector3d implements Vectord, Comparable<Vector3d>, Serializable {

    private static final       long     serialVersionUID = 1;

    public static final        Vector3d ZERO             = new Vector3d(0, 0, 0);
    public static final        Vector3d UNIT_X           = new Vector3d(1, 0, 0);
    public static final        Vector3d UNIT_Y           = new Vector3d(0, 1, 0);
    public static final        Vector3d UNIT_Z           = new Vector3d(0, 0, 1);
    public static final        Vector3d ONE              = new Vector3d(1, 1, 1);
    public static final        Vector3d RIGHT            = Vector3d.UNIT_X;
    public static final        Vector3d UP               = Vector3d.UNIT_Y;
    public static final        Vector3d FORWARD          = Vector3d.UNIT_Z;

    private final              double   x;
    private final              double   y;
    private final              double   z;

    private transient volatile boolean  hashed           = false;
    private transient volatile int      hashCode         = 0;

    public Vector3d(final Vector2d v) {
        this(v, 0);
    }

    public Vector3d(final Vector2d v, final float z) {
        this(v, (double) z);
    }

    public Vector3d(final Vector2d v, final double z) {
        this(v.x(), v.y(), z);
    }

    public Vector3d(final VectorNd v) {
        this(v.get(0), v.get(1), v.size() > 2 ? v.get(2) : 0);
    }

    public Vector3d(final float x, final float y, final float z) {
        this((double) x, (double) y, (double) z);
    }

    public Vector3d(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double x() {
        return this.x;
    }

    public double y() {
        return this.y;
    }

    public double z() {
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

    public Vector3d add(final Vector3d v) {
        return this.add(v.x, v.y, v.z);
    }

    public Vector3d add(final float x, final float y, final float z) {
        return this.add((double) x, (double) y, (double) z);
    }

    public Vector3d add(final double x, final double y, final double z) {
        return new Vector3d(this.x + x, this.y + y, this.z + z);
    }

    public Vector3d sub(final Vector3d v) {
        return this.sub(v.x, v.y, v.z);
    }

    public Vector3d sub(final float x, final float y, final float z) {
        return this.sub((double) x, (double) y, (double) z);
    }

    public Vector3d sub(final double x, final double y, final double z) {
        return new Vector3d(this.x - x, this.y - y, this.z - z);
    }

    public Vector3d mul(final float a) {
        return this.mul((double) a);
    }

    @Override
    public Vector3d mul(final double a) {
        return this.mul(a, a, a);
    }

    public Vector3d mul(final Vector3d v) {
        return this.mul(v.x, v.y, v.z);
    }

    public Vector3d mul(final float x, final float y, final float z) {
        return this.mul((double) x, (double) y, (double) z);
    }

    public Vector3d mul(final double x, final double y, final double z) {
        return new Vector3d(this.x * x, this.y * y, this.z * z);
    }

    public Vector3d div(final float a) {
        return this.div((double) a);
    }

    @Override
    public Vector3d div(final double a) {
        return this.div(a, a, a);
    }

    public Vector3d div(final Vector3d v) {
        return this.div(v.x, v.y, v.z);
    }

    public Vector3d div(final float x, final float y, final float z) {
        return this.div((double) x, (double) y, (double) z);
    }

    public Vector3d div(final double x, final double y, final double z) {
        return new Vector3d(this.x / x, this.y / y, this.z / z);
    }

    public double dot(final Vector3d v) {
        return this.dot(v.x, v.y, v.z);
    }

    public double dot(final float x, final float y, final float z) {
        return this.dot((double) x, (double) y, (double) z);
    }

    public double dot(final double x, final double y, final double z) {
        return this.x * x + this.y * y + this.z * z;
    }

    public Vector3d project(final Vector3d v) {
        return this.project(v.x, v.y, v.z);
    }

    public Vector3d project(final float x, final float y, final float z) {
        return this.project((double) x, (double) y, (double) z);
    }

    public Vector3d project(final double x, final double y, final double z) {
        final double lengthSquared = x * x + y * y + z * z;
        if (Math.abs(lengthSquared) < DBL_EPSILON) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        final double a = this.dot(x, y, z) / lengthSquared;
        return new Vector3d(a * x, a * y, a * z);
    }

    public Vector3d cross(final Vector3d v) {
        return this.cross(v.x, v.y, v.z);
    }

    public Vector3d cross(final float x, final float y, final float z) {
        return this.cross((double) x, (double) y, (double) z);
    }

    public Vector3d cross(final double x, final double y, final double z) {
        return new Vector3d(this.y * z - this.z * y, this.z * x - this.x * z, this.x * y - this.y * x);
    }

    public Vector3d pow(final float pow) {
        return this.pow((double) pow);
    }

    @Override
    public Vector3d pow(final double power) {
        return new Vector3d(Math.pow(this.x, power), Math.pow(this.y, power), Math.pow(this.z, power));
    }

    @Override
    public Vector3d ceil() {
        return new Vector3d(Math.ceil(this.x), Math.ceil(this.y), Math.ceil(this.z));
    }

    @Override
    public Vector3d floor() {
        return new Vector3d(VectorUtils.floor(this.x), VectorUtils.floor(this.y), VectorUtils.floor(this.z));
    }

    @Override
    public Vector3d round() {
        return new Vector3d(Math.round(this.x), Math.round(this.y), Math.round(this.z));
    }

    @Override
    public Vector3d abs() {
        return new Vector3d(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }

    @Override
    public Vector3d negate() {
        return new Vector3d(-this.x, -this.y, -this.z);
    }

    public Vector3d min(final Vector3d v) {
        return this.min(v.x, v.y, v.z);
    }

    public Vector3d min(final float x, final float y, final float z) {
        return this.min(x, y, (double) z);
    }

    public Vector3d min(final double x, final double y, final double z) {
        return new Vector3d(Math.min(this.x, x), Math.min(this.y, y), Math.min(this.z, z));
    }

    public Vector3d max(final Vector3d v) {
        return this.max(v.x, v.y, v.z);
    }

    public Vector3d max(final float x, final float y, final float z) {
        return this.max(x, y, (double) z);
    }

    public Vector3d max(final double x, final double y, final double z) {
        return new Vector3d(Math.max(this.x, x), Math.max(this.y, y), Math.max(this.z, z));
    }

    public double distanceSquared(final Vector3d v) {
        return this.distanceSquared(v.x, v.y, v.z);
    }

    public double distanceSquared(final float x, final float y, final float z) {
        return this.distanceSquared(x, y, (double) z);
    }

    public double distanceSquared(final double x, final double y, final double z) {
        final double dx = this.x - x;
        final double dy = this.y - y;
        final double dz = this.z - z;
        return dx * dx + dy * dy + dz * dz;
    }

    public double distance(final Vector3d v) {
        return this.distance(v.x, v.y, v.z);
    }

    public double distance(final float x, final float y, final float z) {
        return this.distance(x, y, (double) z);
    }

    public double distance(final double x, final double y, final double z) {
        return (double) Math.sqrt(this.distanceSquared(x, y, z));
    }

    @Override
    public double lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    @Override
    public double length() {
        return (double) Math.sqrt(this.lengthSquared());
    }

    @Override
    public Vector3d normalize() {
        final double length = this.length();
        if (Math.abs(length) < DBL_EPSILON) {
            throw new ArithmeticException("Cannot normalize the zero vector");
        }
        return new Vector3d(this.x / length, this.y / length, this.z / length);
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

    public Vector2d toVector2() {
        return new Vector2d(this);
    }

    public Vector2d toVector2(final boolean useZ) {
        return new Vector2d(this.x, useZ ? this.z : this.y);
    }

    public VectorNd toVectorN() {
        return new VectorNd(this);
    }

    @Override
    public double[] toArray() {
        return new double[]{this.x, this.y, this.z};
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
    public int compareTo(final Vector3d v) {
        return (int) Math.signum(this.lengthSquared() - v.lengthSquared());
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof final Vector3d that)) {
            return false;
        }
        if (Double.compare(that.x, this.x) != 0) {
            return false;
        }
        if (Double.compare(that.y, this.y) != 0) {
            return false;
        }
        if (Double.compare(that.z, this.z) != 0) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        if (!this.hashed) {
            int result = (this.x != +0.0f ? Double.hashCode(this.x) : 0);
            result        = 31 * result + (this.y != +0.0f ? Double.hashCode(this.y) : 0);
            this.hashCode = 31 * result + (this.z != +0.0f ? Double.hashCode(this.z) : 0);
            this.hashed   = true;
        }
        return this.hashCode;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public static Vector3d from(final double n) {
        return n == 0 ? Vector3d.ZERO : new Vector3d(n, n, n);
    }

    public static Vector3d from(final double x, final double y, final double z) {
        return x == 0 && y == 0 && z == 0 ? Vector3d.ZERO : new Vector3d(x, y, z);
    }

    /**
     * Gets the direction vector of a random pitch and yaw using the random specified.
     *
     * @param random to use
     * @return the random direction vector
     */
    public static Vector3d createRandomDirection(final Random random) {
        return Vector3d.createDirectionRad(random.nextDouble() * (double) VectorUtils.TWO_PI,
                                           random.nextDouble() * (double) VectorUtils.TWO_PI);
    }

    /**
     * Gets the direction vector of a certain theta and phi in degrees. This uses the standard math spherical coordinate system.
     *
     * @param theta in degrees
     * @param phi   in degrees
     * @return the direction vector
     */
    public static Vector3d createDirectionDeg(final float theta, final float phi) {
        return Vector3d.createDirectionDeg((double) theta, (double) phi);
    }

    /**
     * Gets the direction vector of a certain theta and phi in degrees. This uses the standard math spherical coordinate system.
     *
     * @param theta in degrees
     * @param phi   in degrees
     * @return the direction vector
     */
    public static Vector3d createDirectionDeg(final double theta, final double phi) {
        return Vector3d.createDirectionRad(Math.toRadians(theta), Math.toRadians(phi));
    }

    /**
     * Gets the direction vector of a certain theta and phi in radians. This uses the standard math spherical coordinate system.
     *
     * @param theta in radians
     * @param phi   in radians
     * @return the direction vector
     */
    public static Vector3d createDirectionRad(final float theta, final float phi) {
        return Vector3d.createDirectionRad(theta, (double) phi);
    }

    /**
     * Gets the direction vector of a certain theta and phi in radians. This uses the standard math spherical coordinate system.
     *
     * @param theta in radians
     * @param phi   in radians
     * @return the direction vector
     */
    public static Vector3d createDirectionRad(final double theta, final double phi) {
        final double f = VectorUtils.sin(phi);
        return new Vector3d(f * VectorUtils.cos(theta), f * VectorUtils.sin(theta), VectorUtils.cos(phi));
    }

}