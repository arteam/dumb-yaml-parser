package org.dumb.yaml.builder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Date: 12/14/13
 * Time: 11:40 PM
 * Unsafe allocator if we don't want to use constructors with params
 *
 * @author Artem Prigoda
 */
enum UnsafeAllocator {

    INSTANCE;

    private Object theUnsafe;
    private Method allocateInstance;
    private boolean unsafeAvailable;

    private UnsafeAllocator() {
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field f = unsafeClass.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            theUnsafe = f.get(null);
            allocateInstance = unsafeClass.getMethod("allocateInstance", Class.class);
            unsafeAvailable = true;
        } catch (Exception ignore) {
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> clazz) {
        if (!unsafeAvailable) return null;
        try {
            return (T) allocateInstance.invoke(theUnsafe, clazz);
        } catch (Exception e) {
            return null;
        }
    }
}