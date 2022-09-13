package fr.enimaloc.kuiper.utils.mathsUtils.vector.i;

import fr.enimaloc.kuiper.utils.mathsUtils.vector.d.Vector3d;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.f.Vector3f;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.l.Vector3l;
import java.io.Serializable;
import java.lang.Override;
import java.util.Random;

import static fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils.floor;
import static fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils.floorl;

public final class Vector3i implements Vectori, Comparable<Vector3i>, Serializable {

    private static final long serialVersionUID = 1;

    public static final Vector3i ZERO = new Vector3i(0, 0, 0);
    public static final Vector3i UNIT_X = new Vector3i(1, 0, 0);
    public static final Vector3i UNIT_Y = new Vector3i(0, 1, 0);
    public static final Vector3i UNIT_Z = new Vector3i(0, 0, 1);
    public static final Vector3i ONE = new Vector3i(1, 1, 1);
    public static final Vector3i RIGHT = Vector3i.UNIT_X;
    public static final Vector3i UP = Vector3i.UNIT_Y;
    public static final Vector3i FORWARD = Vector3i.UNIT_Z;

    private final int x;
    private final int y;
    private final int z;

    private transient volatile boolean hashed = false;
    private transient volatile int hashCode = 0;

    public Vector3i(final Vector2i v) {
        this(v, 0);
    }

    public Vector3i(final Vector2i v, final double z) {
        this(v, floor(z));
    }

    public Vector3i(final Vector2i v, final int z) {
        this(v.x(), v.y(), z);
    }

    public Vector3i(final VectorNi v) {
        this(v.get(0), v.get(1), v.size() > 2 ? v.get(2) : 0);
    }

    public Vector3i(final double x, final double y, final double z) {
        this(floor(x), floor(y), floor(z));
    }

    public Vector3i(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public int z() {
        return this.z;
    }

    public Vector3i add(final Vector3i v) {
        return this.add(v.x, v.y, v.z);
    }

    public Vector3i add(final double x, final double y, final double z) {
        return this.add(floor(x), floor(y), floor(z));
    }

    public Vector3i add(final int x, final int y, final int z) {
        return new Vector3i(this.x + x, this.y + y, this.z + z);
    }

    public Vector3i sub(final Vector3i v) {
        return this.sub(v.x, v.y, v.z);
    }

    public Vector3i sub(final double x, final double y, final double z) {
        return this.sub(floor(x), floor(y), floor(z));
    }

    public Vector3i sub(final int x, final int y, final int z) {
        return new Vector3i(this.x - x, this.y - y, this.z - z);
    }

    public Vector3i mul(final double a) {
        return this.mul(floor(a));
    }

    @Override
    public Vector3i mul(final int a) {
        return this.mul(a, a, a);
    }

    public Vector3i mul(final Vector3i v) {
        return this.mul(v.x, v.y, v.z);
    }

    public Vector3i mul(final double x, final double y, final double z) {
        return this.mul(floor(x), floor(y), floor(z));
    }

    public Vector3i mul(final int x, final int y, final int z) {
        return new Vector3i(this.x * x, this.y * y, this.z * z);
    }

    public Vector3i div(final double a) {
        return this.div(floor(a));
    }

    @Override
    public Vector3i div(final int a) {
        return this.div(a, a, a);
    }

    public Vector3i div(final Vector3i v) {
        return this.div(v.x, v.y, v.z);
    }

    public Vector3i div(final double x, final double y, final double z) {
        return this.div(floor(x), floor(y), floor(z));
    }

    public Vector3i div(final int x, final int y, final int z) {
        return new Vector3i(this.x / x, this.y / y, this.z / z);
    }

    public int dot(final Vector3i v) {
        return this.dot(v.x, v.y, v.z);
    }

    public int dot(final double x, final double y, final double z) {
        return this.dot(floor(x), floor(y), floor(z));
    }

    public int dot(final int x, final int y, final int z) {
        return this.x * x + this.y * y + this.z * z;
    }

    public Vector3i project(final Vector3i v) {
        return this.project(v.x, v.y, v.z);
    }

    public Vector3i project(final double x, final double y, final double z) {
        return this.project(floor(x), floor(y), floor(z));
    }

    public Vector3i project(final int x, final int y, final int z) {
        final int lengthSquared = x * x + y * y + z * z;
        if (lengthSquared == 0) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        final double a = (double) this.dot(x, y, z) / lengthSquared;
        return new Vector3i(a * x, a * y, a * z);
    }

    public Vector3i cross(final Vector3i v) {
        return this.cross(v.x, v.y, v.z);
    }

    public Vector3i cross(final double x, final double y, final double z) {
        return this.cross(floor(x), floor(y), floor(z));
    }

    public Vector3i cross(final int x, final int y, final int z) {
        return new Vector3i(this.y * z - this.z * y, this.z * x - this.x * z, this.x * y - this.y * x);
    }

    public Vector3i pow(final double pow) {
        return this.pow(floor(pow));
    }

    @Override
    public Vector3i pow(final int power) {
        return new Vector3i(Math.pow(this.x, power), Math.pow(this.y, power), Math.pow(this.z, power));
    }

    @Override
    public Vector3i abs() {
        return new Vector3i(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }

    @Override
    public Vector3i negate() {
        return new Vector3i(-this.x, -this.y, -this.z);
    }

    public Vector3i min(final Vector3i v) {
        return this.min(v.x, v.y, v.z);
    }

    public Vector3i min(final double x, final double y, final double z) {
        return this.min(floor(x), floor(y), floor(z));
    }

    public Vector3i min(final int x, final int y, final int z) {
        return new Vector3i(Math.min(this.x, x), Math.min(this.y, y), Math.min(this.z, z));
    }

    public Vector3i max(final Vector3i v) {
        return this.max(v.x, v.y, v.z);
    }

    public Vector3i max(final double x, final double y, final double z) {
        return this.max(floor(x), floor(y), floor(z));
    }

    public Vector3i max(final int x, final int y, final int z) {
        return new Vector3i(Math.max(this.x, x), Math.max(this.y, y), Math.max(this.z, z));
    }

    public int distanceSquared(final Vector3i v) {
        return this.distanceSquared(v.x, v.y, v.z);
    }

    public int distanceSquared(final double x, final double y, final double z) {
        return this.distanceSquared(floor(x), floor(y), floor(z));
    }

    public int distanceSquared(final int x, final int y, final int z) {
        final int dx = this.x - x;
        final int dy = this.y - y;
        final int dz = this.z - z;
        return dx * dx + dy * dy + dz * dz;
    }

    public double distance(final Vector3i v) {
        return this.distance(v.x, v.y, v.z);
    }

    public double distance(final double x, final double y, final double z) {
        return this.distance(floor(x), floor(y), floor(z));
    }

    public double distance(final int x, final int y, final int z) {
        return Math.sqrt(this.distanceSquared(x, y, z));
    }

    @Override
    public int lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    @Override
    public double length() {
        return Math.sqrt(this.lengthSquared());
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

    public Vector2i toVector2() {
        return new Vector2i(this);
    }

    public Vector2i toVector2(final boolean useZ) {
        return new Vector2i(this.x, useZ ? this.z : this.y);
    }

    public VectorNi toVectorN() {
        return new VectorNi(this);
    }

    @Override
    public int[] toArray() {
        return new int[]{this.x, this.y, this.z};
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
        return new Vector3d(this.x, this.y, (double) this.z);
    }

    @Override
    public int compareTo(final Vector3i v) {
        return Integer.compare(this.lengthSquared(), v.lengthSquared());
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Vector3i)) {
            return false;
        }
        final Vector3i that = (Vector3i) other;
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
            this.hashCode = ((Integer.hashCode(this.x) * 211 + Integer.hashCode(this.y)) * 97 + Integer.hashCode(this.z));
            this.hashed = true;
        }
        return this.hashCode;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public static Vector3i from(final int n) {
         return n == 0 ? Vector3i.ZERO : new Vector3i(n, n, n);
    }

    public static Vector3i from(final int x, final int y, final int z) {
         return x == 0 && y == 0 && z == 0 ? Vector3i.ZERO : new Vector3i(x, y, z);
    }

}