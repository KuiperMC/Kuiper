package fr.enimaloc.kuiper.utils.mathsUtils.vector.i;

import fr.enimaloc.kuiper.utils.mathsUtils.vector.d.Vectord;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.f.Vectorf;
import fr.enimaloc.kuiper.utils.mathsUtils.vector.l.Vectorl;

public interface Vectori {

    Vectori mul(int a);

    Vectori div(int a);

    Vectori pow(int pow);

    Vectori abs();

    Vectori negate();

    double length();

    int lengthSquared();

    int minAxis();

    int maxAxis();

    int[] toArray();

    Vectori toInt();

    Vectorl toLong();

    Vectorf toFloat();

    Vectord toDouble();

}
