package fr.enimaloc.kuiper.utils.mathsUtils.vector.d;

import fr.enimaloc.kuiper.utils.mathsUtils.vector.f.Vectorf;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.i.Vectori;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.l.Vectorl;

public interface Vectord {

    Vectord mul(double a);

    Vectord div(double a);

    Vectord pow(double pow);

    Vectord ceil();

    Vectord floor();

    Vectord round();

    Vectord abs();

    Vectord negate();

    double length();

    double lengthSquared();

    Vectord normalize();

    int minAxis();

    int maxAxis();

    double[] toArray();

    Vectori toInt();

    Vectorl toLong();

    Vectorf toFloat();

    Vectord toDouble();

}