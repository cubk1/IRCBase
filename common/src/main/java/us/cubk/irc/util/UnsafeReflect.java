package us.cubk.irc.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeReflect {

    public static final Unsafe theUnsafe;

    static {
        Field f;
        try {
            f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            theUnsafe = (Unsafe) f.get(null);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

}
