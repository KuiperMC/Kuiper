package fr.enimaloc.kuiper.utils.mathsUtils.vector.l;

import fr.enimaloc.kuiper.utils.mathsUtils.vector.d.Vector2d;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.f.Vector2f;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.i.Vector2i;
import java.io.Serializable;

import static fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils.floorl;

public final class Vector2l implements Vectorl, Comparable<Vector2l>, Serializable {

    private static final long serialVersionUID = 1;

    public static final Vector2l ZERO = new Vector2l(0, 0);
    public static final Vector2l UNIT_X = new Vector2l(1, 0);
    public static final Vector2l UNIT_Y = new Vector2l(0, 1);
    public static final Vector2l ONE = new Vector2l(1, 1);

    private final long x;
    private final long y;

    private transient volatile boolean hashed = false;
    private transient volatile int hashCode = 0;

    public Vector2l(final Vector3l v) {
        this(v.x(), v.y());
    }

    public Vector2l(final VectorNl v) {
        this(v.get(0), v.get(1));
    }

    public Vector2l(final double x, final double y) {
        this(floorl(x), floorl(y));
    }

    public Vector2l(final long x, final long y) {
        this.x = x;
        this.y = y;
    }

    public long x() {
        return this.x;
    }

    public long y() {
        return this.y;
    }

    public Vector2l add(final Vector2l v) {
        return this.add(v.x, v.y);
    }

    public Vector2l add(final double x, final double y) {
        return this.add(floorl(x), floorl(y));
    }

    public Vector2l add(final long x, final long y) {
        return new Vector2l(this.x + x, this.y + y);
    }

    public Vector2l sub(final Vector2l v) {
        return this.sub(v.x, v.y);
    }

    public Vector2l sub(final double x, final double y) {
        return this.sub(floorl(x), floorl(y));
    }

    public Vector2l sub(final long x, final long y) {
        return new Vector2l(this.x - x, this.y - y);
    }

    public Vector2l mul(final double a) {
        return this.mul(floorl(a));
    }

    @Override
    public Vector2l mul(final long a) {
        return this.mul(a, a);
    }

    public Vector2l mul(final Vector2l v) {
        return this.mul(v.x, v.y);
    }

    public Vector2l mul(final double x, final double y) {
        return this.mul(floorl(x), floorl(y));
    }

    public Vector2l mul(final long x, final long y) {
        return new Vector2l(this.x * x, this.y * y);
    }

    public Vector2l div(final double a) {
        return this.div(floorl(a));
    }

    @Override
    public Vector2l div(final long a) {
        return this.div(a, a);
    }

    public Vector2l div(final Vector2l v) {
        return this.div(v.x, v.y);
    }

    public Vector2l div(final double x, final double y) {
        return this.div(floorl(x), floorl(y));
    }

    public Vector2l div(final long x, final long y) {
        return new Vector2l(this.x / x, this.y / y);
    }

    public long dot(final Vector2l v) {
        return this.dot(v.x, v.y);
    }

    public long dot(final double x, final double y) {
        return this.dot(floorl(x), floorl(y));
    }

    public long dot(final long x, final long y) {
        return this.x * x + this.y * y;
    }

    public Vector2l project(final Vector2l v) {
        return this.project(v.x, v.y);
    }

    public Vector2l project(final double x, final double y) {
        return this.project(floorl(x), floorl(y));
    }

    public Vector2l project(final long x, final long y) {
        final long lengthSquared = x * x + y * y;
        if (lengthSquared == 0) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        final double a = (double) this.dot(x, y) / lengthSquared;
        return new Vector2l(a * x, a * y);
    }

    public Vector2l pow(final double pow) {
        return this.pow(floorl(pow));
    }

    @Override
    public Vector2l pow(final long power) {
        return new Vector2l(Math.pow(this.x, power), Math.pow(this.y, power));
    }

    @Override
    public Vector2l abs() {
        return new Vector2l(Math.abs(this.x), Math.abs(this.y));
    }

    @Override
    public Vector2l negate() {
        return new Vector2l(-this.x, -this.y);
    }

    public Vector2l min(final Vector2l v) {
        return this.min(v.x, v.y);
    }

    public Vector2l min(final double x, final double y) {
        return this.min(floorl(x), floorl(y));
    }

    public Vector2l min(final long x, final long y) {
        return new Vector2l(Math.min(this.x, x), Math.min(this.y, y));
    }

    public Vector2l max(final Vector2l v) {
        return this.max(v.x, v.y);
    }

    public Vector2l max(final double x, final double y) {
        return this.max(floorl(x), floorl(y));
    }

    public Vector2l max(final long x, final long y) {
        return new Vector2l(Math.max(this.x, x), Math.max(this.y, y));
    }

    public long distanceSquared(final Vector2l v) {
        return this.distanceSquared(v.x, v.y);
    }

    public long distanceSquared(final double x, final double y) {
        return this.distanceSquared(floorl(x), floorl(y));
    }

    public long distanceSquared(final long x, final long y) {
        final long dx = this.x - x;
        final long dy = this.y - y;
        return dx * dx + dy * dy;
    }

    public double distance(final Vector2l v) {
        return this.distance(v.x, v.y);
    }

    public double distance(final double x, final double y) {
        return this.distance(floorl(x), floorl(y));
    }

    public double distance(final long x, final long y) {
        return (double) Math.sqrt(this.distanceSquared(x, y));
    }

    @Override
    public long lengthSquared() {
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

    public Vector3l toVector3() {
        return this.toVector3(0);
    }

    public Vector3l toVector3(final double z) {
        return this.toVector3(floorl(z));
    }

    public Vector3l toVector3(final long z) {
        return new Vector3l(this, z);
    }

    public VectorNl toVectorN() {
        return new VectorNl(this);
    }

    @Override
    public long[] toArray() {
        return new long[]{this.x, this.y};
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
    public int compareTo(final Vector2l v) {
        return Long.compare(this.lengthSquared(), v.lengthSquared());
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Vector2l)) {
            return false;
        }
        final Vector2l that = (Vector2l) other;
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
            final int result = (this.x != +0.0f ? Long.hashCode(this.x) : 0);
            this.hashCode = 31 * result + (this.y != +0.0f ? Long.hashCode(this.y) : 0);
            this.hashed = true;
        }
        return this.hashCode;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    public static Vector2l from(final long n) {
         return n == 0 ? Vector2l.ZERO : new Vector2l(n, n);
    }

    public static Vector2l from(final long x, final long y) {
         return x == 0 && y == 0 ? Vector2l.ZERO : new Vector2l(x, y);
    }

}