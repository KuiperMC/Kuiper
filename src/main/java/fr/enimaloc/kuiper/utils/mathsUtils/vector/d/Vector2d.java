package fr.enimaloc.kuiper.utils.mathsUtils.vector.d;

import fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.f.Vector2f;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.i.Vector2i;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.l.Vector2l;
import java.io.Serializable;
import java.util.Random;

import static fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils.DBL_EPSILON;


public final class Vector2d implements Vectord, Comparable<Vector2d>, Serializable {

    private static final       long     serialVersionUID = 1;

    public static final        Vector2d ZERO             = new Vector2d(0, 0);
    public static final        Vector2d UNIT_X           = new Vector2d(1, 0);
    public static final        Vector2d UNIT_Y           = new Vector2d(0, 1);
    public static final        Vector2d ONE              = new Vector2d(1, 1);

    private final              double   x;
    private final              double   y;

    private transient volatile boolean  hashed           = false;
    private transient volatile int      hashCode         = 0;

    public Vector2d(final Vector3d v) {
        this(v.x(), v.y());
    }

    public Vector2d(final VectorNd v) {
        this(v.get(0), v.get(1));
    }

    public Vector2d(final float x, final float y) {
        this((double) x, (double) y);
    }

    public Vector2d(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public double x() {
        return this.x;
    }

    public double y() {
        return this.y;
    }

    public int floorX() {
        return VectorUtils.floor(this.x);
    }

    public int floorY() {
        return VectorUtils.floor(this.y);
    }

    public Vector2d add(final Vector2d v) {
        return this.add(v.x, v.y);
    }

    public Vector2d add(final float x, final float y) {
        return this.add((double) x, (double) y);
    }

    public Vector2d add(final double x, final double y) {
        return new Vector2d(this.x + x, this.y + y);
    }

    public Vector2d sub(final Vector2d v) {
        return this.sub(v.x, v.y);
    }

    public Vector2d sub(final float x, final float y) {
        return this.sub((double) x, (double) y);
    }

    public Vector2d sub(final double x, final double y) {
        return new Vector2d(this.x - x, this.y - y);
    }

    public Vector2d mul(final float a) {
        return this.mul((double) a);
    }

    @Override
    public Vector2d mul(final double a) {
        return this.mul(a, a);
    }

    public Vector2d mul(final Vector2d v) {
        return this.mul(v.x, v.y);
    }

    public Vector2d mul(final float x, final float y) {
        return this.mul((double) x, (double) y);
    }

    public Vector2d mul(final double x, final double y) {
        return new Vector2d(this.x * x, this.y * y);
    }

    public Vector2d div(final float a) {
        return this.div((double) a);
    }

    @Override
    public Vector2d div(final double a) {
        return this.div(a, a);
    }

    public Vector2d div(final Vector2d v) {
        return this.div(v.x, v.y);
    }

    public Vector2d div(final float x, final float y) {
        return this.div((double) x, (double) y);
    }

    public Vector2d div(final double x, final double y) {
        return new Vector2d(this.x / x, this.y / y);
    }

    public double dot(final Vector2d v) {
        return this.dot(v.x, v.y);
    }

    public double dot(final float x, final float y) {
        return this.dot((double) x, (double) y);
    }

    public double dot(final double x, final double y) {
        return this.x * x + this.y * y;
    }

    public Vector2d project(final Vector2d v) {
        return this.project(v.x, v.y);
    }

    public Vector2d project(final float x, final float y) {
        return this.project((double) x, (double) y);
    }

    public Vector2d project(final double x, final double y) {
        final double lengthSquared = x * x + y * y;
        if (Math.abs(lengthSquared) < DBL_EPSILON) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        final double a = this.dot(x, y) / lengthSquared;
        return new Vector2d(a * x, a * y);
    }

    public Vector2d pow(final float pow) {
        return this.pow((double) pow);
    }

    @Override
    public Vector2d pow(final double power) {
        return new Vector2d(Math.pow(this.x, power), Math.pow(this.y, power));
    }

    @Override
    public Vector2d ceil() {
        return new Vector2d(Math.ceil(this.x), Math.ceil(this.y));
    }

    @Override
    public Vector2d floor() {
        return new Vector2d(VectorUtils.floor(this.x), VectorUtils.floor(this.y));
    }

    @Override
    public Vector2d round() {
        return new Vector2d(Math.round(this.x), Math.round(this.y));
    }

    @Override
    public Vector2d abs() {
        return new Vector2d(Math.abs(this.x), Math.abs(this.y));
    }

    @Override
    public Vector2d negate() {
        return new Vector2d(-this.x, -this.y);
    }

    public Vector2d min(final Vector2d v) {
        return this.min(v.x, v.y);
    }

    public Vector2d min(final float x, final float y) {
        return this.min((double) x, (double) y);
    }

    public Vector2d min(final double x, final double y) {
        return new Vector2d(Math.min(this.x, x), Math.min(this.y, y));
    }

    public Vector2d max(final Vector2d v) {
        return this.max(v.x, v.y);
    }

    public Vector2d max(final float x, final float y) {
        return this.max((double) x, (double) y);
    }

    public Vector2d max(final double x, final double y) {
        return new Vector2d(Math.max(this.x, x), Math.max(this.y, y));
    }

    public double distanceSquared(final Vector2d v) {
        return this.distanceSquared(v.x, v.y);
    }

    public double distanceSquared(final float x, final float y) {
        return this.distanceSquared((double) x, (double) y);
    }

    public double distanceSquared(final double x, final double y) {
        final double dx = this.x - x;
        final double dy = this.y - y;
        return dx * dx + dy * dy;
    }

    public double distance(final Vector2d v) {
        return this.distance(v.x, v.y);
    }

    public double distance(final float x, final float y) {
        return this.distance((double) x, (double) y);
    }

    public double distance(final double x, final double y) {
        return (double) Math.sqrt(this.distanceSquared(x, y));
    }

    @Override
    public double lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }

    @Override
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    @Override
    public Vector2d normalize() {
        final double length = this.length();
        if (Math.abs(length) < DBL_EPSILON) {
            throw new ArithmeticException("Cannot normalize the zero vector");
        }
        return new Vector2d(this.x / length, this.y / length);
    }

    /**
     * Return the axis with the minimal value.
     *
     * @return {@link int} axis with minimal value
     */
    @Override
    public int minAxis() {
        return this.x < this.y ? 0 : 1;
    }

    /**
     * Return the axis with the maximum value.
     *
     * @return {@link int} axis with maximum value
     */
    @Override
    public int maxAxis() {
        return this.x > this.y ? 0 : 1;
    }

    public Vector3d toVector3() {
        return this.toVector3(0);
    }

    public Vector3d toVector3(final float z) {
        return this.toVector3((double) z);
    }

    public Vector3d toVector3(final double z) {
        return new Vector3d(this, z);
    }

    public VectorNd toVectorN() {
        return new VectorNd(this);
    }

    @Override
    public double[] toArray() {
        return new double[]{this.x, this.y};
    }

    @Override
    public Vector2i toInt() {
        return new Vector2i(this.x, this.y);
    }

    @Override
    public Vector2l toLong() {
        return new Vector2l(this.x, this.y);
    }

    @Override
    public Vector2f toFloat() {
        return new Vector2f(this.x, this.y);
    }

    @Override
    public Vector2d toDouble() {
        return new Vector2d(this.x, this.y);
    }

    @Override
    public int compareTo(final Vector2d v) {
        return (int) Math.signum(this.lengthSquared() - v.lengthSquared());
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Vector2d)) {
            return false;
        }
        final Vector2d that = (Vector2d) other;
        if (Double.compare(that.x, this.x) != 0) {
            return false;
        }
        if (Double.compare(that.y, this.y) != 0) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        if (!this.hashed) {
            final int result = (this.x != +0.0f ? Double.hashCode(this.x) : 0);
            this.hashCode = 31 * result + (this.y != +0.0f ? Double.hashCode(this.y) : 0);
            this.hashed   = true;
        }
        return this.hashCode;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    public static Vector2d from(final double n) {
        return n == 0 ? Vector2d.ZERO : new Vector2d(n, n);
    }

    public static Vector2d from(final double x, final double y) {
        return x == 0 && y == 0 ? Vector2d.ZERO : new Vector2d(x, y);
    }

    /**
     * Gets the direction vector of a random angle using the random specified.
     *
     * @param random to use
     * @return the random direction vector
     */
    public static Vector2d createRandomDirection(final Random random) {
        return Vector2d.createDirectionRad(random.nextDouble() * VectorUtils.TWO_PI);
    }

    /**
     * Gets the direction vector of a certain angle in degrees.
     *
     * @param angle in degrees
     * @return the direction vector
     */
    public static Vector2d createDirectionDeg(final float angle) {
        return Vector2d.createDirectionDeg((double) angle);
    }

    /**
     * Gets the direction vector of a certain angle in degrees.
     *
     * @param angle in degrees
     * @return the direction vector
     */
    public static Vector2d createDirectionDeg(final double angle) {
        return Vector2d.createDirectionRad(Math.toRadians(angle));
    }

    /**
     * Gets the direction vector of a certain angle in radians.
     *
     * @param angle in radians
     * @return the direction vector
     */
    public static Vector2d createDirectionRad(final float angle) {
        return Vector2d.createDirectionRad((double) angle);
    }

    /**
     * Gets the direction vector of a certain angle in radians.
     *
     * @param angle in radians
     * @return the direction vector
     */
    public static Vector2d createDirectionRad(final double angle) {
        return new Vector2d(VectorUtils.cos(angle), VectorUtils.sin(angle));
    }
}