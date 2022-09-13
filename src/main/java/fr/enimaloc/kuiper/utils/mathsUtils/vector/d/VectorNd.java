package fr.enimaloc.kuiper.utils.mathsUtils.vector.d;

import fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.f.VectorNf;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.i.VectorNi;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.l.VectorNl;
import java.io.Serializable;
import java.util.Arrays;

import static fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils.DBL_EPSILON;
import static fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils.floorl;

public class VectorNd implements Vectord, Comparable<VectorNd>, Serializable, Cloneable {
    public static        VectorNd ZERO_2           = new ImmutableZeroVectorN(0, 0);
    public static        VectorNd ZERO_3           = new ImmutableZeroVectorN(0, 0, 0);
    public static        VectorNd ZERO_4           = new ImmutableZeroVectorN(0, 0, 0, 0);

    private static final long     serialVersionUID = 1;

    private final        double[] vec;

    public VectorNd(final int size) {
        if (size < 2) {
            throw new IllegalArgumentException("Minimum vector size is 2");
        }
        vec = new double[size];
    }

    public VectorNd(final Vector2d v) {
        this(v.x(), v.y());
    }

    public VectorNd(final Vector3d v) {
        this(v.x(), v.y(), v.z());
    }

    public VectorNd(final VectorNd v) {
        this(v.vec);
    }

    public VectorNd(final double... v) {
        this.vec = v.clone();
    }

    public int size() {
        return this.vec.length;
    }

    public double get(final int comp) {
        return this.vec[comp];
    }

    public int floored(final int comp) {
        return VectorUtils.floor(this.get(comp));
    }

    public void set(final int comp, final float val) {
        this.set(comp, (double) val);
    }

    public void set(final int comp, final double val) {
        this.vec[comp] = val;
    }

    public void setZero() {
        Arrays.fill(this.vec, 0);
    }

    public VectorNd resize(final int size) {
        final VectorNd d = new VectorNd(size);
        System.arraycopy(vec, 0, d.vec, 0, Math.min(size, this.size()));
        return d;
    }

    public VectorNd add(final VectorNd v) {
        return this.add(v.vec);
    }

    public VectorNd add(final double... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNd d = new VectorNd(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] + v[comp];
        }
        return d;
    }

    public VectorNd sub(final VectorNd v) {
        return this.sub(v.vec);
    }

    public VectorNd sub(final double... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNd d = new VectorNd(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] - v[comp];
        }
        return d;
    }

    public VectorNd mul(final float a) {
        return this.mul((double) a);
    }

    @Override
    public VectorNd mul(final double a) {
        final int      size = this.size();
        final VectorNd d    = new VectorNd(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] * a;
        }
        return d;
    }

    public VectorNd mul(final VectorNd v) {
        return this.mul(v.vec);
    }

    public VectorNd mul(final double... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNd d = new VectorNd(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] * v[comp];
        }
        return d;
    }

    public VectorNd div(final float a) {
        return this.div((double) a);
    }

    @Override
    public VectorNd div(double a) {
        final int      size = size();
        final VectorNd d    = new VectorNd(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = vec[comp] / a;
        }
        return d;
    }

    public VectorNd div(final VectorNd v) {
        return this.div(v.vec);
    }

    public VectorNd div(final double... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNd d = new VectorNd(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = vec[comp] / v[comp];
        }
        return d;
    }

    public double dot(final VectorNd v) {
        return this.dot(v.vec);
    }

    public double dot(final double... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        double d = 0;
        for (int comp = 0; comp < size; comp++) {
            d += this.vec[comp] * v[comp];
        }
        return d;
    }

    public VectorNd project(final VectorNd v) {
        return this.project(v.vec);
    }

    public VectorNd project(final double... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        double lengthSquared = 0;
        for (int comp = 0; comp < size; comp++) {
            lengthSquared += v[comp] * v[comp];
        }
        if (Math.abs(lengthSquared) < DBL_EPSILON) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        final double   a = this.dot(v) / lengthSquared;
        final VectorNd d = new VectorNd(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = a * v[comp];
        }
        return d;
    }

    public VectorNd pow(final float pow) {
        return this.pow((double) pow);
    }

    @Override
    public VectorNd pow(final double power) {
        final int      size = this.size();
        final VectorNd d    = new VectorNd(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.pow(this.vec[comp], power);
        }
        return d;
    }

    @Override
    public VectorNd ceil() {
        final int      size = this.size();
        final VectorNd d    = new VectorNd(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.ceil(this.vec[comp]);
        }
        return d;
    }

    @Override
    public VectorNd floor() {
        final int      size = this.size();
        final VectorNd d    = new VectorNd(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = VectorUtils.floor(this.vec[comp]);
        }
        return d;
    }

    @Override
    public VectorNd round() {
        final int      size = this.size();
        final VectorNd d    = new VectorNd(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.round(this.vec[comp]);
        }
        return d;
    }

    @Override
    public VectorNd abs() {
        final int      size = this.size();
        final VectorNd d    = new VectorNd(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.abs(this.vec[comp]);
        }
        return d;
    }

    @Override
    public VectorNd negate() {
        final int      size = this.size();
        final VectorNd d    = new VectorNd(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = -this.vec[comp];
        }
        return d;
    }

    public VectorNd min(final VectorNd v) {
        return this.min(v.vec);
    }

    public VectorNd min(final double... v) {
        final int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNd d = new VectorNd(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.min(this.vec[comp], v[comp]);
        }
        return d;
    }

    public VectorNd max(final VectorNd v) {
        return this.max(v.vec);
    }

    public VectorNd max(final double... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNd d = new VectorNd(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.max(this.vec[comp], v[comp]);
        }
        return d;
    }

    public double distanceSquared(final VectorNd v) {
        return this.distanceSquared(v.vec);
    }

    public double distanceSquared(final double... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        double d = 0;
        for (int comp = 0; comp < size; comp++) {
            final double delta = this.vec[comp] - v[comp];
            d += delta * delta;
        }
        return d;
    }

    public double distance(final VectorNd v) {
        return this.distance(v.vec);
    }

    public double distance(final double... v) {
        return Math.sqrt(this.distanceSquared(v));
    }

    @Override
    public double lengthSquared() {
        final int size = this.size();
        double    l    = 0;
        for (int comp = 0; comp < size; comp++) {
            l += this.vec[comp] * this.vec[comp];
        }
        return l;
    }

    @Override
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    @Override
    public VectorNd normalize() {
        final double length = this.length();
        if (Math.abs(length) < DBL_EPSILON) {
            throw new ArithmeticException("Cannot normalize the zero vector");
        }
        final int      size = this.size();
        final VectorNd d    = new VectorNd(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] / length;
        }
        return d;
    }

    @Override
    public int minAxis() {
        int       axis  = 0;
        double    value = this.vec[axis];
        final int size  = this.size();
        for (int comp = 1; comp < size; comp++) {
            if (this.vec[comp] < value) {
                value = this.vec[comp];
                axis  = comp;
            }
        }
        return axis;
    }

    @Override
    public int maxAxis() {
        int       axis  = 0;
        double    value = this.vec[axis];
        final int size  = this.size();
        for (int comp = 1; comp < size; comp++) {
            if (this.vec[comp] > value) {
                value = this.vec[comp];
                axis  = comp;
            }
        }
        return axis;
    }

    public Vector2d toVector2() {
        return new Vector2d(this);
    }

    public Vector3d toVector3() {
        return new Vector3d(this);
    }

    @Override
    public double[] toArray() {
        return vec.clone();
    }

    @Override
    public VectorNi toInt() {
        final int   size   = this.size();
        final int[] intVec = new int[size];
        for (int comp = 0; comp < size; comp++) {
            intVec[comp] = VectorUtils.floor(this.vec[comp]);
        }
        return new VectorNi(intVec);
    }

    @Override
    public VectorNl toLong() {
        final int    size    = this.size();
        final long[] longVec = new long[size];
        for (int comp = 0; comp < size; comp++) {
            longVec[comp] = floorl(this.vec[comp]);
        }
        return new VectorNl(longVec);
    }

    @Override
    public VectorNf toFloat() {
        final int     size     = this.size();
        final float[] floatVec = new float[size];
        for (int comp = 0; comp < size; comp++) {
            floatVec[comp] = (float) this.vec[comp];
        }
        return new VectorNf(floatVec);
    }

    @Override
    public VectorNd toDouble() {
        final int      size      = this.size();
        final double[] doubleVec = new double[size];
        for (int comp = 0; comp < size; comp++) {
            doubleVec[comp] = this.vec[comp];
        }
        return new VectorNd(doubleVec);
    }

    @Override
    public int compareTo(final VectorNd v) {
        return (int) Math.signum(this.lengthSquared() - v.lengthSquared());
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof VectorNd)) {
            return false;
        }
        return Arrays.equals(this.vec, ((VectorNd) other).vec);
    }

    @Override
    public int hashCode() {
        return 67 * 5 + Arrays.hashCode(this.vec);
    }

    @Override
    public VectorNd clone() {
        return new VectorNd(this);
    }

    @Override
    public String toString() {
        return Arrays.toString(this.vec).replace('[', '(').replace(']', ')');
    }

    private static class ImmutableZeroVectorN extends VectorNd {

        private static final long serialVersionUID = 1L;

        public ImmutableZeroVectorN(final double... v) {
            super(v);
        }

        @Override
        public void set(final int comp, final double val) {
            throw new UnsupportedOperationException("You may not alter this vector");
        }
    }
}