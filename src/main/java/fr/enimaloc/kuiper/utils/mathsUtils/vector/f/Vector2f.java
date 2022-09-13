package fr.enimaloc.kuiper.utils.mathsUtils.vector.f;

import fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.d.Vector2d;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.i.Vector2i;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.l.Vector2l;
import java.io.Serializable;
import java.util.Random;

import static fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils.FLT_EPSILON;

public final class Vector2f implements Vectorf, Comparable<Vector2f>, Serializable {

    private static final       long     serialVersionUID = 1;

    public static final        Vector2f ZERO             = new Vector2f(0, 0);
    public static final        Vector2f UNIT_X           = new Vector2f(1, 0);
    public static final        Vector2f UNIT_Y           = new Vector2f(0, 1);
    public static final        Vector2f ONE              = new Vector2f(1, 1);

    private final              float    x;
    private final              float    y;

    private transient volatile boolean  hashed           = false;
    private transient volatile int      hashCode         = 0;

    public Vector2f(final Vector3f v) {
        this(v.x(), v.y());
    }

    public Vector2f(final VectorNf v) {
        this(v.get(0), v.get(1));
    }

    public Vector2f(final double x, final double y) {
        this((float) x, (float) y);
    }

    public Vector2f(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    public float x() {
        return this.x;
    }

    public float y() {
        return this.y;
    }

    public int floorX() {
        return VectorUtils.floor(this.x);
    }

    public int floorY() {
        return VectorUtils.floor(this.y);
    }

    public Vector2f add(final Vector2f v) {
        return this.add(v.x, v.y);
    }

    public Vector2f add(final double x, final double y) {
        return this.add((float) x, (float) y);
    }

    public Vector2f add(final float x, final float y) {
        return new Vector2f(this.x + x, this.y + y);
    }

    public Vector2f sub(final Vector2f v) {
        return this.sub(v.x, v.y);
    }

    public Vector2f sub(final double x, final double y) {
        return this.sub((float) x, (float) y);
    }

    public Vector2f sub(final float x, final float y) {
        return new Vector2f(this.x - x, this.y - y);
    }

    public Vector2f mul(final double a) {
        return this.mul((float) a);
    }

    @Override
    public Vector2f mul(final float a) {
        return this.mul(a, a);
    }

    public Vector2f mul(final Vector2f v) {
        return this.mul(v.x, v.y);
    }

    public Vector2f mul(final double x, final double y) {
        return this.mul((float) x, (float) y);
    }

    public Vector2f mul(final float x, final float y) {
        return new Vector2f(this.x * x, this.y * y);
    }

    public Vector2f div(final double a) {
        return this.div((float) a);
    }

    @Override
    public Vector2f div(final float a) {
        return this.div(a, a);
    }

    public Vector2f div(final Vector2f v) {
        return this.div(v.x, v.y);
    }

    public Vector2f div(final double x, final double y) {
        return this.div((float) x, (float) y);
    }

    public Vector2f div(final float x, final float y) {
        return new Vector2f(this.x / x, this.y / y);
    }

    public float dot(final Vector2f v) {
        return this.dot(v.x, v.y);
    }

    public float dot(final double x, final double y) {
        return this.dot((float) x, (float) y);
    }

    public float dot(final float x, final float y) {
        return this.x * x + this.y * y;
    }

    public Vector2f project(final Vector2f v) {
        return this.project(v.x, v.y);
    }

    public Vector2f project(final double x, final double y) {
        return this.project((float) x, (float) y);
    }

    public Vector2f project(final float x, final float y) {
        final float lengthSquared = x * x + y * y;
        if (Math.abs(lengthSquared) < FLT_EPSILON) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        final float a = this.dot(x, y) / lengthSquared;
        return new Vector2f(a * x, a * y);
    }

    public Vector2f pow(final double pow) {
        return this.pow((float) pow);
    }

    @Override
    public Vector2f pow(final float power) {
        return new Vector2f(Math.pow(this.x, power), Math.pow(this.y, power));
    }

    @Override
    public Vector2f ceil() {
        return new Vector2f(Math.ceil(this.x), Math.ceil(this.y));
    }

    @Override
    public Vector2f floor() {
        return new Vector2f(VectorUtils.floor(this.x), VectorUtils.floor(this.y));
    }

    @Override
    public Vector2f round() {
        return new Vector2f(Math.round(this.x), Math.round(this.y));
    }

    @Override
    public Vector2f abs() {
        return new Vector2f(Math.abs(this.x), Math.abs(this.y));
    }

    @Override
    public Vector2f negate() {
        return new Vector2f(-this.x, -this.y);
    }

    public Vector2f min(final Vector2f v) {
        return this.min(v.x, v.y);
    }

    public Vector2f min(final double x, final double y) {
        return this.min((float) x, (float) y);
    }

    public Vector2f min(final float x, final float y) {
        return new Vector2f(Math.min(this.x, x), Math.min(this.y, y));
    }

    public Vector2f max(final Vector2f v) {
        return this.max(v.x, v.y);
    }

    public Vector2f max(final double x, final double y) {
        return this.max((float) x, (float) y);
    }

    public Vector2f max(final float x, final float y) {
        return new Vector2f(Math.max(this.x, x), Math.max(this.y, y));
    }

    public float distanceSquared(final Vector2f v) {
        return this.distanceSquared(v.x, v.y);
    }

    public float distanceSquared(final double x, final double y) {
        return this.distanceSquared((float) x, (float) y);
    }

    public float distanceSquared(final float x, final float y) {
        final float dx = this.x - x;
        final float dy = this.y - y;
        return dx * dx + dy * dy;
    }

    public float distance(final Vector2f v) {
        return this.distance(v.x, v.y);
    }

    public float distance(final double x, final double y) {
        return this.distance((float) x, (float) y);
    }

    public float distance(final float x, final float y) {
        return (float) Math.sqrt(this.distanceSquared(x, y));
    }

    @Override
    public float lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }

    @Override
    public float length() {
        return (float) Math.sqrt(this.lengthSquared());
    }

    @Override
    public Vector2f normalize() {
        final float length = this.length();
        if (Math.abs(length) < FLT_EPSILON) {
            throw new ArithmeticException("Cannot normalize the zero vector");
        }
        return new Vector2f(this.x / length, this.y / length);
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

    public Vector3f toVector3() {
        return this.toVector3(0);
    }

    public Vector3f toVector3(final double z) {
        return this.toVector3((float) z);
    }

    public Vector3f toVector3(final float z) {
        return new Vector3f(this, z);
    }

    public VectorNf toVectorN() {
        return new VectorNf(this);
    }

    @Override
    public float[] toArray() {
        return new float[]{this.x, this.y};
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
    public int compareTo(final Vector2f v) {
        return (int) Math.signum(this.lengthSquared() - v.lengthSquared());
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof final Vector2f that)) {
            return false;
        }
        if (Float.compare(that.x, this.x) != 0) {
            return false;
        }
        return Float.compare(that.y, this.y) == 0;
    }

    @Override
    public int hashCode() {
        if (!this.hashed) {
            final int result = (this.x != +0.0f ? Float.hashCode(this.x) : 0);
            this.hashCode = 31 * result + (this.y != +0.0f ? Float.hashCode(this.y) : 0);
            this.hashed   = true;
        }
        return this.hashCode;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    public static Vector2f from(final float n) {
        return n == 0 ? Vector2f.ZERO : new Vector2f(n, n);
    }

    public static Vector2f from(final float x, final float y) {
        return x == 0 && y == 0 ? Vector2f.ZERO : new Vector2f(x, y);
    }

    /**
     * Gets the direction vector of a random angle using the random specified.
     *
     * @param random to use
     * @return the random direction vector
     */
    public static Vector2f createRandomDirection(final Random random) {
        return Vector2f.createDirectionRad(random.nextFloat() * (float) VectorUtils.TWO_PI);
    }

    /**
     * Gets the direction vector of a certain angle in degrees.
     *
     * @param angle in degrees
     * @return the direction vector
     */
    public static Vector2f createDirectionDeg(final double angle) {
        return Vector2f.createDirectionDeg((float) angle);
    }

    /**
     * Gets the direction vector of a certain angle in degrees.
     *
     * @param angle in degrees
     * @return the direction vector
     */
    public static Vector2f createDirectionDeg(final float angle) {
        return Vector2f.createDirectionRad((float) Math.toRadians(angle));
    }

    /**
     * Gets the direction vector of a certain angle in radians.
     *
     * @param angle in radians
     * @return the direction vector
     */
    public static Vector2f createDirectionRad(final double angle) {
        return Vector2f.createDirectionRad((float) angle);
    }

    /**
     * Gets the direction vector of a certain angle in radians.
     *
     * @param angle in radians
     * @return the direction vector
     */
    public static Vector2f createDirectionRad(final float angle) {
        return new Vector2f(VectorUtils.cos(angle), VectorUtils.sin(angle));
    }
}