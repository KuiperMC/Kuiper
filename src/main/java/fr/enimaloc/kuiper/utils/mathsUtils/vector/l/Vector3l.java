package fr.enimaloc.kuiper.utils.mathsUtils.vector.l;

import fr.enimaloc.kuiper.utils.mathsUtils.vector.d.Vector3d;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.f.Vector3f;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.i.Vector3i;
import java.io.Serializable;

import static fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils.floorl;

public final class Vector3l implements Vectorl, Comparable<Vector3l>, Serializable {

    private static final long serialVersionUID = 1;

    public static final Vector3l ZERO = new Vector3l(0, 0, 0);
    public static final Vector3l UNIT_X = new Vector3l(1, 0, 0);
    public static final Vector3l UNIT_Y = new Vector3l(0, 1, 0);
    public static final Vector3l UNIT_Z = new Vector3l(0, 0, 1);
    public static final Vector3l ONE = new Vector3l(1, 1, 1);
    public static final Vector3l RIGHT = Vector3l.UNIT_X;
    public static final Vector3l UP = Vector3l.UNIT_Y;
    public static final Vector3l FORWARD = Vector3l.UNIT_Z;

    private final long x;
    private final long y;
    private final long z;

    private transient volatile boolean hashed = false;
    private transient volatile int hashCode = 0;

    public Vector3l(final Vector2l v) {
        this(v, 0);
    }

    public Vector3l(final Vector2l v, final double z) {
        this(v, floorl(z));
    }

    public Vector3l(final Vector2l v, final long z) {
        this(v.x(), v.y(), z);
    }

    public Vector3l(final VectorNl v) {
        this(v.get(0), v.get(1), v.size() > 2 ? v.get(2) : 0);
    }

    public Vector3l(final double x, final double y, final double z) {
        this(floorl(x), floorl(y), floorl(z));
    }

    public Vector3l(final long x, final long y, final long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long x() {
        return this.x;
    }

    public long y() {
        return this.y;
    }

    public long z() {
        return this.z;
    }

    public Vector3l add(final Vector3l v) {
        return this.add(v.x, v.y, v.z);
    }

    public Vector3l add(final double x, final double y, final double z) {
        return this.add(floorl(x), floorl(y), floorl(z));
    }

    public Vector3l add(final long x, final long y, final long z) {
        return new Vector3l(this.x + x, this.y + y, this.z + z);
    }

    public Vector3l sub(final Vector3l v) {
        return this.sub(v.x, v.y, v.z);
    }

    public Vector3l sub(final double x, final double y, final double z) {
        return this.sub(floorl(x), floorl(y), floorl(z));
    }

    public Vector3l sub(final long x, final long y, final long z) {
        return new Vector3l(this.x - x, this.y - y, this.z - z);
    }

    public Vector3l mul(final double a) {
        return this.mul(floorl(a));
    }

    @Override
    public Vector3l mul(final long a) {
        return this.mul(a, a, a);
    }

    public Vector3l mul(final Vector3l v) {
        return this.mul(v.x, v.y, v.z);
    }

    public Vector3l mul(final double x, final double y, final double z) {
        return this.mul(floorl(x), floorl(y), floorl(z));
    }

    public Vector3l mul(final long x, final long y, final long z) {
        return new Vector3l(this.x * x, this.y * y, this.z * z);
    }

    public Vector3l div(final double a) {
        return this.div(floorl(a));
    }

    @Override
    public Vector3l div(final long a) {
        return this.div(a, a, a);
    }

    public Vector3l div(final Vector3l v) {
        return this.div(v.x, v.y, v.z);
    }

    public Vector3l div(final double x, final double y, final double z) {
        return this.div(floorl(x), floorl(y), floorl(z));
    }

    public Vector3l div(final long x, final long y, final long z) {
        return new Vector3l(this.x / x, this.y / y, this.z / z);
    }

    public long dot(final Vector3l v) {
        return this.dot(v.x, v.y, v.z);
    }

    public long dot(final double x, final double y, final double z) {
        return this.dot(floorl(x), floorl(y), floorl(z));
    }

    public long dot(final long x, final long y, final long z) {
        return this.x * x + this.y * y + this.z * z;
    }

    public Vector3l project(final Vector3l v) {
        return this.project(v.x, v.y, v.z);
    }

    public Vector3l project(final double x, final double y, final double z) {
        return this.project(floorl(x), floorl(y), floorl(z));
    }

    public Vector3l project(final long x, final long y, final long z) {
        final long lengthSquared = x * x + y * y + z * z;
        if (lengthSquared == 0) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        final double a = (double) this.dot(x, y, z) / lengthSquared;
        return new Vector3l(a * x, a * y, a * z);
    }

    public Vector3l cross(final Vector3l v) {
        return this.cross(v.x, v.y, v.z);
    }

    public Vector3l cross(final double x, final double y, final double z) {
        return this.cross(floorl(x), floorl(y), floorl(z));
    }

    public Vector3l cross(final long x, final long y, final long z) {
        return new Vector3l(this.y * z - this.z * y, this.z * x - this.x * z, this.x * y - this.y * x);
    }

    public Vector3l pow(final double pow) {
        return this.pow(floorl(pow));
    }

    @Override
    public Vector3l pow(final long power) {
        return new Vector3l(Math.pow(this.x, power), Math.pow(this.y, power), Math.pow(this.z, power));
    }

    @Override
    public Vector3l abs() {
        return new Vector3l(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }

    @Override
    public Vector3l negate() {
        return new Vector3l(-this.x, -this.y, -this.z);
    }

    public Vector3l min(final Vector3l v) {
        return this.min(v.x, v.y, v.z);
    }

    public Vector3l min(final double x, final double y, final double z) {
        return this.min(floorl(x), floorl(y), floorl(z));
    }

    public Vector3l min(final long x, final long y, final long z) {
        return new Vector3l(Math.min(this.x, x), Math.min(this.y, y), Math.min(this.z, z));
    }

    public Vector3l max(final Vector3l v) {
        return this.max(v.x, v.y, v.z);
    }

    public Vector3l max(final double x, final double y, final double z) {
        return this.max(floorl(x), floorl(y), floorl(z));
    }

    public Vector3l max(final long x, final long y, final long z) {
        return new Vector3l(Math.max(this.x, x), Math.max(this.y, y), Math.max(this.z, z));
    }

    public long distanceSquared(final Vector3l v) {
        return this.distanceSquared(v.x, v.y, v.z);
    }

    public long distanceSquared(final double x, final double y, final double z) {
        return this.distanceSquared(floorl(x), floorl(y), floorl(z));
    }

    public long distanceSquared(final long x, final long y, final long z) {
        final long dx = this.x - x;
        final long dy = this.y - y;
        final long dz = this.z - z;
        return dx * dx + dy * dy + dz * dz;
    }

    public double distance(final Vector3l v) {
        return this.distance(v.x, v.y, v.z);
    }

    public double distance(final double x, final double y, final double z) {
        return this.distance(floorl(x), floorl(y), floorl(z));
    }

    public double distance(final long x, final long y, final long z) {
        return (double) Math.sqrt(this.distanceSquared(x, y, z));
    }

    @Override
    public long lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    @Override
    public double length() {
        return (double) Math.sqrt(this.lengthSquared());
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

    public Vector2l toVector2() {
        return new Vector2l(this);
    }

    public Vector2l toVector2(final boolean useZ) {
        return new Vector2l(this.x, useZ ? this.z : this.y);
    }

    public VectorNl toVectorN() {
        return new VectorNl(this);
    }

    @Override
    public long[] toArray() {
        return new long[]{this.x, this.y, this.z};
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
        return new Vector3d((double) this.x, (double) this.y, (double) this.z);
    }

    @Override
    public int compareTo(final Vector3l v) {
        return Long.compare(this.lengthSquared(), v.lengthSquared());
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Vector3l)) {
            return false;
        }
        final Vector3l that = (Vector3l) other;
        if (that.x != this.x) {
            return false;
        }
        if (that.y != this.y) {
            return false;
        }
        if (that.z != this.z) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        if (!this.hashed) {
            this.hashCode = ((Long.hashCode(this.x) * 211 + Long.hashCode(this.y)) * 97 + Long.hashCode(this.z));
            this.hashed = true;
        }
        return this.hashCode;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public static Vector3l from(final long n) {
         return n == 0 ? Vector3l.ZERO : new Vector3l(n, n, n);
    }

    public static Vector3l from(final long x, final long y, final long z) {
         return x == 0 && y == 0 && z == 0 ? Vector3l.ZERO : new Vector3l(x, y, z);
    }

}