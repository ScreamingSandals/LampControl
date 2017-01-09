package cz.ceph.lampcontrol.utils;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by siOnzee on 12/16/2016.
 */

public class StringUtils {

    public static <ArrayType> String arrToString(String joiner, ArrayType[] arrayType) {
        return String.join(joiner, Arrays.stream(arrayType).map(Object::toString).toArray(String[]::new));
    }

    public static String arrToString(String joiner, Collection<?> collection) {
        return String.join(joiner, collection.stream().map(Object::toString).toArray(String[]::new));
    }
}
