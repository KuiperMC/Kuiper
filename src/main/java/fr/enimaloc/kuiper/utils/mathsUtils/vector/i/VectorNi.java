package fr.enimaloc.kuiper.utils.mathsUtils.vector.i;

import fr.enimaloc.kuiper.utils.mathsUtils.vector.d.VectorNd;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.f.VectorNf;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.l.VectorNl;
import java.io.Serializable;
import java.util.Arrays;

import static fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils.floor;
import static fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils.floorl;

public class VectorNi implements Vectori, Comparable<VectorNi>, Serializable, Cloneable {

    public static VectorNi ZERO_2 = new ImmutableZeroVectorN(0, 0);
    public static VectorNi ZERO_3 = new ImmutableZeroVectorN(0, 0, 0);
    public static VectorNi ZERO_4 = new ImmutableZeroVectorN(0, 0, 0, 0);

    private static final long serialVersionUID = 1;

    private final int[] vec;

    public VectorNi(final int size) {
        if (size < 2) {
            throw new IllegalArgumentException("Minimum vector size is 2");
        }
        vec = new int[size];
    }

    public VectorNi(final Vector2i v) {
        this(v.x(), v.y());
    }

    public VectorNi(final Vector3i v) {
        this(v.x(), v.y(), v.z());
    }

    public VectorNi(final VectorNi v) {
        this(v.vec);
    }

    public VectorNi(final int... v) {
        this.vec = v.clone();
    }

    public int size() {
        return this.vec.length;
    }

    public int get(final int comp) {
        return this.vec[comp];
    }

    public void set(final int comp, final int val) {
        this.vec[comp] = val;
    }

    public void setZero() {
        Arrays.fill(vec, 0);
    }

    public VectorNi resize(final int size) {
        final VectorNi d = new VectorNi(size);
        System.arraycopy(vec, 0, d.vec, 0, Math.min(size, this.size()));
        return d;
    }

    public VectorNi add(final VectorNi v) {
        return this.add(v.vec);
    }

    public VectorNi add(final int... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNi d = new VectorNi(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] + v[comp];
        }
        return d;
    }

    public VectorNi sub(final VectorNi v) {
        return this.sub(v.vec);
    }

    public VectorNi sub(final int... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNi d = new VectorNi(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] - v[comp];
        }
        return d;
    }

    public VectorNi mul(final double a) {
        return this.mul(floor(a));
    }

    @Override
    public VectorNi mul(int a) {
        final int size = this.size();
        final VectorNi d = new VectorNi(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] * a;
        }
        return d;
    }

    public VectorNi mul(final VectorNi v) {
        return this.mul(v.vec);
    }

    public VectorNi mul(final int... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNi d = new VectorNi(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] * v[comp];
        }
        return d;
    }

    public VectorNi div(final double a) {
        return this.div(floor(a));
    }

    @Override
    public VectorNi div(int a) {
        final int size = this.size();
        final VectorNi d = new VectorNi(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = vec[comp] / a;
        }
        return d;
    }

    public VectorNi div(final VectorNi v) {
        return this.div(v.vec);
    }

    public VectorNi div(final int... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNi d = new VectorNi(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] / v[comp];
        }
        return d;
    }

    public int dot(final VectorNi v) {
        return this.dot(v.vec);
    }

    public int dot(final int... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        int d = 0;
        for (int comp = 0; comp < size; comp++) {
            d += this.vec[comp] * v[comp];
        }
        return d;
    }

    public VectorNi project(final VectorNi v) {
        return this.project(v.vec);
    }

    public VectorNi project(final int... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        int lengthSquared = 0;
        for (int comp = 0; comp < size; comp++) {
            lengthSquared += v[comp] * v[comp];
        }
        if (lengthSquared == 0) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        final double a = (double) this.dot(v) / lengthSquared;
        final VectorNi d = new VectorNi(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = floor(a * v[comp]);
        }
        return d;
    }

    public VectorNi pow(final double pow) {
        return this.pow(floor(pow));
    }

    @Override
    public VectorNi pow(final int power) {
        final int size = this.size();
        final VectorNi d = new VectorNi(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = floor(Math.pow(this.vec[comp], power));
        }
        return d;
    }

    @Override
    public VectorNi abs() {
        final int size = this.size();
        final VectorNi d = new VectorNi(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.abs(this.vec[comp]);
        }
        return d;
    }

    @Override
    public VectorNi negate() {
        final int size = this.size();
        final VectorNi d = new VectorNi(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = -this.vec[comp];
        }
        return d;
    }

    public VectorNi min(final VectorNi v) {
        return this.min(v.vec);
    }

    public VectorNi min(final int... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNi d = new VectorNi(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.min(this.vec[comp], v[comp]);
        }
        return d;
    }

    public VectorNi max(final VectorNi v) {
        return this.max(v.vec);
    }

    public VectorNi max(final int... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNi d = new VectorNi(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.max(this.vec[comp], v[comp]);
        }
        return d;
    }

    public int distanceSquared(final VectorNi v) {
        return distanceSquared(v.vec);
    }

    public int distanceSquared(final int... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        int d = 0;
        for (int comp = 0; comp < size; comp++) {
            final int delta = this.vec[comp] - v[comp];
            d += delta * delta;
        }
        return d;
    }

    public double distance(final VectorNi v) {
        return this.distance(v.vec);
    }

    public double distance(final int... v) {
        return (double) Math.sqrt(this.distanceSquared(v));
    }

    @Override
    public int lengthSquared() {
        final int size = this.size();
        int l = 0;
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
        int value = this.vec[axis];
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
        int value = vec[axis];
        final int size = this.size();
        for (int comp = 1; comp < size; comp++) {
            if (this.vec[comp] > value) {
                value = this.vec[comp];
                axis = comp;
            }
        }
        return axis;
    }

    public Vector2i toVector2() {
        return new Vector2i(this);
    }

    public Vector3i toVector3() {
        return new Vector3i(this);
    }

    @Override
    public int[] toArray() {
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
    public int compareTo(final VectorNi v) {
        return Integer.compare(this.lengthSquared(), v.lengthSquared());
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof VectorNi)) {
            return false;
        }
        return Arrays.equals(this.vec, ((VectorNi) other).vec);
    }

    @Override
    public int hashCode() {
        return 67 * 5 + Arrays.hashCode(this.vec);
    }

    @Override
    public VectorNi clone() {
        return new VectorNi(this);
    }

    @Override
    public String toString() {
        return Arrays.toString(this.vec).replace('[', '(').replace(']', ')');
    }

    private static class ImmutableZeroVectorN extends VectorNi {

        private static final long serialVersionUID = 1L;

        public ImmutableZeroVectorN(final int... v) {
            super(v);
        }

        @Override
        public void set(final int comp, final int val) {
            throw new UnsupportedOperationException("You may not alter this vector");
        }

    }

}