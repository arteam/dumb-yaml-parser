package util;

import java.util.Arrays;

/**
 * Date: 12.12.13
 * Time: 18:23
 *
 * @author Artem Prigoda
 */
public final class Objects {
    private Objects() {
    }

    public static boolean equals(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public static int hashCode(Object... objects) {
        return Arrays.hashCode(objects);
    }
}