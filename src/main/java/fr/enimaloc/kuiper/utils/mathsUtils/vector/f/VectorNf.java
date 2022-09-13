package fr.enimaloc.kuiper.utils.mathsUtils.vector.f;

import fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.d.VectorNd;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.i.VectorNi;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.l.VectorNl;
import java.io.Serializable;
import java.util.Arrays;

import static fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils.FLT_EPSILON;
import static fr.enimaloc.kuiper.utils.mathsUtils.vector.VectorUtils.floorl;

public class VectorNf implements Vectorf, Comparable<VectorNf>, Serializable, Cloneable {
    public static        VectorNf ZERO_2           = new ImmutableZeroVectorN(0, 0);
    public static        VectorNf ZERO_3           = new ImmutableZeroVectorN(0, 0, 0);
    public static        VectorNf ZERO_4           = new ImmutableZeroVectorN(0, 0, 0, 0);

    private static final long     serialVersionUID = 1;

    private final        float[]  vec;

    public VectorNf(final int size) {
        if (size < 2) {
            throw new IllegalArgumentException("Minimum vector size is 2");
        }
        vec = new float[size];
    }

    public VectorNf(final Vector2f v) {
        this(v.x(), v.y());
    }

    public VectorNf(final Vector3f v) {
        this(v.x(), v.y(), v.z());
    }

    public VectorNf(final VectorNf v) {
        this(v.vec);
    }

    public VectorNf(final float... v) {
        this.vec = v.clone();
    }

    public int size() {
        return this.vec.length;
    }

    public float get(final int comp) {
        return this.vec[comp];
    }

    public int floored(final int comp) {
        return VectorUtils.floor(this.get(comp));
    }

    public void set(final int comp, final double val) {
        this.set(comp, (float) val);
    }

    public void set(final int comp, final float val) {
        this.vec[comp] = val;
    }

    public void setZero() {
        Arrays.fill(this.vec, 0);
    }

    public VectorNf resize(final int size) {
        final VectorNf d = new VectorNf(size);
        System.arraycopy(vec, 0, d.vec, 0, Math.min(size, this.size()));
        return d;
    }

    public VectorNf add(final VectorNf v) {
        return this.add(v.vec);
    }

    public VectorNf add(final float... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNf d = new VectorNf(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] + v[comp];
        }
        return d;
    }

    public VectorNf sub(final VectorNf v) {
        return this.sub(v.vec);
    }

    public VectorNf sub(final float... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNf d = new VectorNf(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] - v[comp];
        }
        return d;
    }

    public VectorNf mul(final double a) {
        return this.mul((float) a);
    }

    @Override
    public VectorNf mul(final float a) {
        final int      size = this.size();
        final VectorNf d    = new VectorNf(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] * a;
        }
        return d;
    }

    public VectorNf mul(final VectorNf v) {
        return this.mul(v.vec);
    }

    public VectorNf mul(final float... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNf d = new VectorNf(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] * v[comp];
        }
        return d;
    }

    public VectorNf div(final double a) {
        return this.div((float) a);
    }

    @Override
    public VectorNf div(float a) {
        final int      size = size();
        final VectorNf d    = new VectorNf(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = vec[comp] / a;
        }
        return d;
    }

    public VectorNf div(final VectorNf v) {
        return this.div(v.vec);
    }

    public VectorNf div(final float... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNf d = new VectorNf(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = vec[comp] / v[comp];
        }
        return d;
    }

    public float dot(final VectorNf v) {
        return this.dot(v.vec);
    }

    public float dot(final float... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        float d = 0;
        for (int comp = 0; comp < size; comp++) {
            d += this.vec[comp] * v[comp];
        }
        return d;
    }

    public VectorNf project(final VectorNf v) {
        return this.project(v.vec);
    }

    public VectorNf project(final float... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        float lengthSquared = 0;
        for (int comp = 0; comp < size; comp++) {
            lengthSquared += v[comp] * v[comp];
        }
        if (Math.abs(lengthSquared) < FLT_EPSILON) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        final float    a = this.dot(v) / lengthSquared;
        final VectorNf d = new VectorNf(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = a * v[comp];
        }
        return d;
    }

    public VectorNf pow(final double pow) {
        return this.pow((float) pow);
    }

    @Override
    public VectorNf pow(final float power) {
        final int      size = this.size();
        final VectorNf d    = new VectorNf(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = (float) Math.pow(this.vec[comp], power);
        }
        return d;
    }

    @Override
    public VectorNf ceil() {
        final int      size = this.size();
        final VectorNf d    = new VectorNf(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = (float) Math.ceil(this.vec[comp]);
        }
        return d;
    }

    @Override
    public VectorNf floor() {
        final int      size = this.size();
        final VectorNf d    = new VectorNf(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = VectorUtils.floor(this.vec[comp]);
        }
        return d;
    }

    @Override
    public VectorNf round() {
        final int      size = this.size();
        final VectorNf d    = new VectorNf(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.round(this.vec[comp]);
        }
        return d;
    }

    @Override
    public VectorNf abs() {
        final int      size = this.size();
        final VectorNf d    = new VectorNf(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.abs(this.vec[comp]);
        }
        return d;
    }

    @Override
    public VectorNf negate() {
        final int      size = this.size();
        final VectorNf d    = new VectorNf(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = -this.vec[comp];
        }
        return d;
    }

    public VectorNf min(final VectorNf v) {
        return this.min(v.vec);
    }

    public VectorNf min(final float... v) {
        final int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNf d = new VectorNf(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.min(this.vec[comp], v[comp]);
        }
        return d;
    }

    public VectorNf max(final VectorNf v) {
        return this.max(v.vec);
    }

    public VectorNf max(final float... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        final VectorNf d = new VectorNf(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.max(this.vec[comp], v[comp]);
        }
        return d;
    }

    public float distanceSquared(final VectorNf v) {
        return this.distanceSquared(v.vec);
    }

    public float distanceSquared(final float... v) {
        final int size = this.size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        float d = 0;
        for (int comp = 0; comp < size; comp++) {
            final float delta = this.vec[comp] - v[comp];
            d += delta * delta;
        }
        return d;
    }

    public float distance(final VectorNf v) {
        return this.distance(v.vec);
    }

    public float distance(final float... v) {
        return (float) Math.sqrt(this.distanceSquared(v));
    }

    @Override
    public float lengthSquared() {
        final int size = this.size();
        float     l    = 0;
        for (int comp = 0; comp < size; comp++) {
            l += this.vec[comp] * this.vec[comp];
        }
        return l;
    }

    @Override
    public float length() {
        return (float) Math.sqrt(this.lengthSquared());
    }

    @Override
    public VectorNf normalize() {
        final float length = this.length();
        if (Math.abs(length) < FLT_EPSILON) {
            throw new ArithmeticException("Cannot normalize the zero vector");
        }
        final int      size = this.size();
        final VectorNf d    = new VectorNf(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] / length;
        }
        return d;
    }

    @Override
    public int minAxis() {
        int       axis  = 0;
        float     value = this.vec[axis];
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
        float     value = this.vec[axis];
        final int size  = this.size();
        for (int comp = 1; comp < size; comp++) {
            if (this.vec[comp] > value) {
                value = this.vec[comp];
                axis  = comp;
            }
        }
        return axis;
    }

    public Vector2f toVector2() {
        return new Vector2f(this);
    }

    public Vector3f toVector3() {
        return new Vector3f(this);
    }

    @Override
    public float[] toArray() {
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
            floatVec[comp] = this.vec[comp];
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
    public int compareTo(final VectorNf v) {
        return (int) Math.signum(this.lengthSquared() - v.lengthSquared());
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof VectorNf)) {
            return false;
        }
        return Arrays.equals(this.vec, ((VectorNf) other).vec);
    }

    @Override
    public int hashCode() {
        return 67 * 5 + Arrays.hashCode(this.vec);
    }

    @Override
    public VectorNf clone() {
        return new VectorNf(this);
    }

    @Override
    public String toString() {
        return Arrays.toString(this.vec).replace('[', '(').replace(']', ')');
    }

    private static class ImmutableZeroVectorN extends VectorNf {

        private static final long serialVersionUID = 1L;

        public ImmutableZeroVectorN(final float... v) {
            super(v);
        }

        @Override
        public void set(final int comp, final float val) {
            throw new UnsupportedOperationException("You may not alter this vector");
        }
    }
}