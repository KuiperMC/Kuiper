package fr.enimaloc.kuiper.utils.mathsUtils.vector.l;

import fr.enimaloc.kuiper.utils.mathsUtils.vector.d.Vectord;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.f.Vectorf;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.i.Vectori;

public interface Vectorl {

    Vectorl mul(long a);

    Vectorl div(long a);

    Vectorl pow(long pow);

    Vectorl abs();

    Vectorl negate();

    double length();

    long lengthSquared();

    int minAxis();

    int maxAxis();

    long[] toArray();

    Vectori toInt();

    Vectorl toLong();

    Vectorf toFloat();

    Vectord toDouble();

}
