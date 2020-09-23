package util;

public class ArrayUtil {
    public static <T> T[] array(T... values) {
        return values;
    }
// TODO TEST
    public static <T> boolean in(T toCheck, T... values) {
        for (T value : values)
            if (value.equals(toCheck)) return true;
        return false;
    }
}
