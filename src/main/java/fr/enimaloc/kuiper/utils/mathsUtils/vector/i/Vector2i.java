package fr.enimaloc.kuiper.utils.mathsUtils.vector.i;

import fr.enimaloc.kuiper.utils.mathsUtils.vector.d.Vector2d;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.f.Vector2f;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.l.Vector2l;
import java.io.Serializable;

import static fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils.*;

public final class Vector2i implements Vectori, Comparable<Vector2i>, Serializable {

    private static final long serialVersionUID = 1;

    public static final Vector2i ZERO = new Vector2i(0, 0);
    public static final Vector2i UNIT_X = new Vector2i(1, 0);
    public static final Vector2i UNIT_Y = new Vector2i(0, 1);
    public static final Vector2i ONE = new Vector2i(1, 1);

    private final int x;
    private final int y;

    private transient volatile boolean hashed = false;
    private transient volatile int hashCode = 0;

    public Vector2i(final Vector3i v) {
        this(v.x(), v.y());
    }

    public Vector2i(final VectorNi v) {
        this(v.get(0), v.get(1));
    }

    public Vector2i(final double x, final double y) {
        this(floor(x), floor(y));
    }

    public Vector2i(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public Vector2i add(final Vector2i v) {
        return this.add(v.x, v.y);
    }

    public Vector2i add(final double x, final double y) {
        return this.add(floor(x), floor(y));
    }

    public Vector2i add(final int x, final int y) {
        return new Vector2i(this.x + x, this.y + y);
    }

    public Vector2i sub(final Vector2i v) {
        return this.sub(v.x, v.y);
    }

    public Vector2i sub(final double x, final double y) {
        return this.sub(floor(x), floor(y));
    }

    public Vector2i sub(final int x, final int y) {
        return new Vector2i(this.x - x, this.y - y);
    }

    public Vector2i mul(final double a) {
        return this.mul(floor(a));
    }

    @Override
    public Vector2i mul(final int a) {
        return this.mul(a, a);
    }

    public Vector2i mul(final Vector2i v) {
        return this.mul(v.x, v.y);
    }

    public Vector2i mul(final double x, final double y) {
        return this.mul(floor(x), floor(y));
    }

    public Vector2i mul(final int x, final int y) {
        return new Vector2i(this.x * x, this.y * y);
    }

    public Vector2i div(final double a) {
        return this.div(floor(a));
    }

    @Override
    public Vector2i div(final int a) {
        return this.div(a, a);
    }

    public Vector2i div(final Vector2i v) {
        return this.div(v.x, v.y);
    }

    public Vector2i div(final double x, final double y) {
        return this.div(floor(x), floor(y));
    }

    public Vector2i div(final int x, final int y) {
        return new Vector2i(this.x / x, this.y / y);
    }

    public int dot(final Vector2i v) {
        return this.dot(v.x, v.y);
    }

    public int dot(final double x, final double y) {
        return this.dot(floor(x), floor(y));
    }

    public int dot(final int x, final int y) {
        return this.x * x + this.y * y;
    }

    public Vector2i project(final Vector2i v) {
        return this.project(v.x, v.y);
    }

    public Vector2i project(final double x, final double y) {
        return this.project(floor(x), floor(y));
    }

    public Vector2i project(final int x, final int y) {
        final int lengthSquared = x * x + y * y;
        if (lengthSquared == 0) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        final double a = (double) this.dot(x, y) / lengthSquared;
        return new Vector2i(a * x, a * y);
    }

    public Vector2i pow(final double pow) {
        return this.pow(floor(pow));
    }

    @Override
    public Vector2i pow(final int power) {
        return new Vector2i(Math.pow(this.x, power), Math.pow(this.y, power));
    }

    @Override
    public Vector2i abs() {
        return new Vector2i(Math.abs(this.x), Math.abs(this.y));
    }

    @Override
    public Vector2i negate() {
        return new Vector2i(-this.x, -this.y);
    }

    public Vector2i min(final Vector2i v) {
        return this.min(v.x, v.y);
    }

    public Vector2i min(final double x, final double y) {
        return this.min(floor(x), floor(y));
    }

    public Vector2i min(final int x, final int y) {
        return new Vector2i(Math.min(this.x, x), Math.min(this.y, y));
    }

    public Vector2i max(final Vector2i v) {
        return this.max(v.x, v.y);
    }

    public Vector2i max(final double x, final double y) {
        return this.max(floor(x), floor(y));
    }

    public Vector2i max(final int x, final int y) {
        return new Vector2i(Math.max(this.x, x), Math.max(this.y, y));
    }

    public int distanceSquared(final Vector2i v) {
        return this.distanceSquared(v.x, v.y);
    }

    public int distanceSquared(final double x, final double y) {
        return this.distanceSquared(floor(x), floor(y));
    }

    public int distanceSquared(final int x, final int y) {
        final int dx = this.x - x;
        final int dy = this.y - y;
        return dx * dx + dy * dy;
    }

    public double distance(final Vector2i v) {
        return this.distance(v.x, v.y);
    }

    public double distance(final double x, final double y) {
        return this.distance(floor(x), floor(y));
    }

    public double distance(final int x, final int y) {
        return (double) Math.sqrt(this.distanceSquared(x, y));
    }

    @Override
    public int lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }

    @Override
    public double length() {
        return (double) Math.sqrt(this.lengthSquared());
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

    public Vector3i toVector3() {
        return this.toVector3(0);
    }

    public Vector3i toVector3(final double z) {
        return this.toVector3(floor(z));
    }

    public Vector3i toVector3(final int z) {
        return new Vector3i(this, z);
    }

    public VectorNi toVectorN() {
        return new VectorNi(this);
    }

    @Override
    public int[] toArray() {
        return new int[]{this.x, this.y};
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
        return new Vector2d((double) this.x, (double) this.y);
    }

    @Override
    public int compareTo(final Vector2i v) {
        return Integer.compare(this.lengthSquared(), v.lengthSquared());
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Vector2i)) {
            return false;
        }
        final Vector2i that = (Vector2i) other;
        if (that.x != this.x) {
            return false;
        }
        if (that.y != this.y) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        if (!this.hashed) {
            final int result = (this.x != +0.0f ? Integer.hashCode(this.x) : 0);
            this.hashCode = 31 * result + (this.y != +0.0f ? Integer.hashCode(this.y) : 0);
            this.hashed = true;
        }
        return this.hashCode;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    public static Vector2i from(final int n) {
         return n == 0 ? Vector2i.ZERO : new Vector2i(n, n);
    }

    public static Vector2i from(final int x, final int y) {
         return x == 0 && y == 0 ? Vector2i.ZERO : new Vector2i(x, y);
    }

}