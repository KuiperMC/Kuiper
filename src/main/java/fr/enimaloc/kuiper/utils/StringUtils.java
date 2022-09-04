/*
 * StringUtils
 *
 * 0.0.1
 *
 * 05/09/2022
 */
package fr.enimaloc.kuiper.utils;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 *
 */
public class StringUtils {
    public static String genFrame(String s) {
        return genFrame(s,
                        '*', '*', '*', '*',
                        '*', '*', '*', '*');
    }

    public static String genFrame(String s, char corner, char horizontal, char vertical) {
        return genFrame(s,
                        corner, corner, corner, corner,
                        horizontal, horizontal, vertical, vertical);
    }

    public static String genFrame(
            String s,
            char topLeftCorner, char topRightCorner, char bottomLeftCorner, char bottomRightCorner,
            char horizontal, char vertical
    ) {
        return genFrame(s,
                        topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner,
                        horizontal, horizontal, vertical, vertical);
    }

    public static String genFrame(
            String s,
            char topLeftCorner, char topRightCorner, char bottomLeftCorner, char bottomRightCorner,
            char top, char bottom, char left, char right
    ) {
        int          max    = Arrays.stream(s.split("\n")).mapToInt(String::length).max().orElse(0);
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add(topLeftCorner + ("" + top).repeat(max + 2) + topRightCorner);
        for (String line : s.split("\n")) {
            joiner.add(left + " " + line + " ".repeat(max - line.length()) + " " + right);
        }
        joiner.add(bottomLeftCorner + ("" + bottom).repeat(max + 2) + bottomRightCorner);
        return joiner.toString();
    }
}
