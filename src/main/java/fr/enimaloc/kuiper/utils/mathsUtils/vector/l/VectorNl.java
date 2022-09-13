package fr.enimaloc.kuiper.utils.mathsUtils.vector.l;

import fr.enimaloc.kuiper.utils.mathsUtils.vector.d.VectorNd;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.f.VectorNf;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.i.VectorNi;
import java.io.Serializable;
import java.util.Arrays;

import static fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils.floorl;

public class VectorNl implements Vectorl, Comparable<VectorNl>, Serializable, Cloneable {

    public static VectorNl ZERO_2 = new ImmutableZeroVectorN(0, 0);
    public static VectorNl ZERO_3 = new ImmutableZeroVectorN(0, 0, 0);
    public static VectorNl ZERO_4 = new ImmutableZeroVectorN(0, 0, 0, 0);

    private static final long serialVersionUID = 1;

    private final long[] vec;

    public VectorNl(final int size) {
        if (size < 2) {
            throw new IllegalArgumentException("Minimum vector size is 2");
        }
        vec = new long[size];
    }

    public VectorNl(final Vector2l v) {
        this(v.x(), v.y());
    }

    public VectorNl(final Vector3l v) {
        this(v.x(), v.y(), v.z());
    }

    public VectorNl(final VectorNl v) {
        this(v.vec);
    }

    public VectorNl(final long... v) {
        this.vec = v.clone();
    }

    public int size() {
        return this.vec.length;
    }

    public long get(final int comp) {
        return this.vec[comp];
    }

    public void set(final int comp, final long val) {
        this.vec[comp] = val;
    }

    public void setZero() {
        Arrays.fill(vec, 0);
    }

    public VectorNl resize(final int size) {
        final VectorNl d = new VectorNl(size);
        System.arraycopy(vec, 0, d.vec, 0, Math.min(size, this.size()));
        return d;
    }

    public VectorNl add(final VectorNl v) {
        return this.add(v.vec);
    }

    public VectorNl add(final long... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNl d = new VectorNl(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] + v[comp];
        }
        return d;
    }

    public VectorNl sub(final VectorNl v) {
        return this.sub(v.vec);
    }

    public VectorNl sub(final long... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNl d = new VectorNl(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] - v[comp];
        }
        return d;
    }

    public VectorNl mul(final double a) {
        return this.mul(floorl(a));
    }

    @Override
    public VectorNl mul(long a) {
        final int size = this.size();
        final VectorNl d = new VectorNl(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] * a;
        }
        return d;
    }

    public VectorNl mul(final VectorNl v) {
        return this.mul(v.vec);
    }

    public VectorNl mul(final long... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNl d = new VectorNl(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] * v[comp];
        }
        return d;
    }

    public VectorNl div(final double a) {
        return this.div(floorl(a));
    }

    @Override
    public VectorNl div(long a) {
        final int size = this.size();
        final VectorNl d = new VectorNl(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = vec[comp] / a;
        }
        return d;
    }

    public VectorNl div(final VectorNl v) {
        return this.div(v.vec);
    }

    public VectorNl div(final long... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNl d = new VectorNl(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] / v[comp];
        }
        return d;
    }

    public long dot(final VectorNl v) {
        return this.dot(v.vec);
    }

    public long dot(final long... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        long d = 0;
        for (int comp = 0; comp < size; comp++) {
            d += this.vec[comp] * v[comp];
        }
        return d;
    }

    public VectorNl project(final VectorNl v) {
        return this.project(v.vec);
    }

    public VectorNl project(final long... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        long lengthSquared = 0;
        for (int comp = 0; comp < size; comp++) {
            lengthSquared += v[comp] * v[comp];
        }
        if (lengthSquared == 0) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        final double a = (double) this.dot(v) / lengthSquared;
        final VectorNl d = new VectorNl(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = floorl(a * v[comp]);
        }
        return d;
    }

    public VectorNl pow(final double pow) {
        return this.pow(floorl(pow));
    }

    @Override
    public VectorNl pow(final long power) {
        final int size = this.size();
        final VectorNl d = new VectorNl(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = floorl(Math.pow(this.vec[comp], power));
        }
        return d;
    }

    @Override
    public VectorNl abs() {
        final int size = this.size();
        final VectorNl d = new VectorNl(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.abs(this.vec[comp]);
        }
        return d;
    }

    @Override
    public VectorNl negate() {
        final int size = this.size();
        final VectorNl d = new VectorNl(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = -this.vec[comp];
        }
        return d;
    }

    public VectorNl min(final VectorNl v) {
        return this.min(v.vec);
    }

    public VectorNl min(final long... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNl d = new VectorNl(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.min(this.vec[comp], v[comp]);
        }
        return d;
    }

    public VectorNl max(final VectorNl v) {
        return this.max(v.vec);
    }

    public VectorNl max(final long... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNl d = new VectorNl(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.max(this.vec[comp], v[comp]);
        }
        return d;
    }

    public long distanceSquared(final VectorNl v) {
        return distanceSquared(v.vec);
    }

    public long distanceSquared(final long... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        long d = 0;
        for (int comp = 0; comp < size; comp++) {
            final long delta = this.vec[comp] - v[comp];
            d += delta * delta;
        }
        return d;
    }

    public double distance(final VectorNl v) {
        return this.distance(v.vec);
    }

    public double distance(final long... v) {
        return (double) Math.sqrt(this.distanceSquared(v));
    }

    @Override
    public long lengthSquared() {
        final int size = this.size();
        long l = 0;
        for (int comp = 0; comp < size; comp++) {
            l += this.vec[comp] * this.vec[comp];
        }
        return l;
    }

    @Override
    public double length() {
        return (double) Math.sqrt(this.lengthSquared());
    }

    @Override
    public int minAxis() {
        int axis = 0;
        long value = this.vec[axis];
        final int size = this.size();
        for (int comp = 1; comp < size; comp++) {
            if (this.vec[comp] < value) {
                value = this.vec[comp];
                axis = comp;
            }
        }
        return axis;
    }

    @Override
    public int maxAxis() {
        int axis = 0;
        long value = vec[axis];
        final int size = this.size();
        for (int comp = 1; comp < size; comp++) {
            if (this.vec[comp] > value) {
                value = this.vec[comp];
                axis = comp;
            }
        }
        return axis;
    }

    public Vector2l toVector2() {
        return new Vector2l(this);
    }

    public Vector3l toVector3() {
        return new Vector3l(this);
    }

    @Override
    public long[] toArray() {
        return this.vec.clone();
    }

    @Override
    public VectorNi toInt() {
        final int size = this.size();
        final int[] intVec = new int[size];
        for (int comp = 0; comp < size; comp++) {
            intVec[comp] = (int) this.vec[comp];
        }
        return new VectorNi(intVec);
    }

    @Override
    public VectorNl toLong() {
        final int size = this.size();
        final long[] longVec = new long[size];
        for (int comp = 0; comp < size; comp++) {
            longVec[comp] = (long) this.vec[comp];
        }
        return new VectorNl(longVec);
    }

    @Override
    public VectorNf toFloat() {
        final int size = this.size();
        final float[] floatVec = new float[size];
        for (int comp = 0; comp < size; comp++) {
            floatVec[comp] = (float) this.vec[comp];
        }
        return new VectorNf(floatVec);
    }

    @Override
    public VectorNd toDouble() {
        final int size = this.size();
        final double[] doubleVec = new double[size];
        for (int comp = 0; comp < size; comp++) {
            doubleVec[comp] = (double) this.vec[comp];
        }
        return new VectorNd(doubleVec);
    }

    @Override
    public int compareTo(final VectorNl v) {
        return Long.compare(this.lengthSquared(), v.lengthSquared());
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof VectorNl)) {
            return false;
        }
        return Arrays.equals(this.vec, ((VectorNl) other).vec);
    }

    @Override
    public int hashCode() {
        return 67 * 5 + Arrays.hashCode(this.vec);
    }

    @Override
    public VectorNl clone() {
        return new VectorNl(this);
    }

    @Override
    public String toString() {
        return Arrays.toString(this.vec).replace('[', '(').replace(']', ')');
    }

    private static class ImmutableZeroVectorN extends VectorNl {

        private static final long serialVersionUID = 1L;

        public ImmutableZeroVectorN(final long... v) {
            super(v);
        }

        @Override
        public void set(final int comp, final long val) {
            throw new UnsupportedOperationException("You may not alter this vector");
        }

    }

}