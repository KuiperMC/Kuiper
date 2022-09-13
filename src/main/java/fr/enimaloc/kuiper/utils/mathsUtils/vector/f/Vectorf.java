package fr.enimaloc.kuiper.utils.mathsUtils.vector.f;

import fr.enimaloc.kuiper.utils.mathsUtils.vector.d.Vectord;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.i.Vectori;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.l.Vectorl;

public interface Vectorf {

    Vectorf mul(float a);

    Vectorf div(float a);

    Vectorf pow(float pow);

    Vectorf ceil();

    Vectorf floor();

    Vectorf round();

    Vectorf abs();

    Vectorf negate();

    float length();

    float lengthSquared();

    Vectorf normalize();

    int minAxis();

    int maxAxis();

    float[] toArray();

    Vectori toInt();

    Vectorl toLong();

    Vectorf toFloat();

    Vectord toDouble();

}