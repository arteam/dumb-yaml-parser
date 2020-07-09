package org.dumb.yaml.builder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Date: 12/14/13
 * Time: 11:40 PM
 * Unsafe allocator if we don't want/are not able to use constructors with params
 *
 * @author Artem Prigoda
 */
enum UnsafeAllocator {

    INSTANCE;

    // Guarded by unsafeAvailable
    @NotNull
    private Object theUnsafe;

    @NotNull
    private Method allocateInstance;

    private boolean unsafeAvailable;

    UnsafeAllocator() {
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
    @Nullable
    public <T> T create(@NotNull Class<T> clazz) {
        if (!unsafeAvailable) {
            return null;
        }
        try {
            return (T) allocateInstance.invoke(theUnsafe, clazz);
        } catch (Exception e) {
            return null;
        }
    }
}