package io.github.shadowchild.arcanine.util;

import io.github.shadowchild.arcanine.Arcanine;

public class ClassLoadUtil {

    public static ClassLoader getCL() {

        return Arcanine.class.getClassLoader();
    }

    public static ClassLoader getSafeCL() {

        return Thread.currentThread().getContextClassLoader();
    }

    public static <T> Class<T> loadClass(String name) throws ClassNotFoundException {

        return (Class<T>) getCL().loadClass(name);
    }
}
