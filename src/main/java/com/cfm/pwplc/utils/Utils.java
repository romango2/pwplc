package com.cfm.pwplc.utils;

import java.net.URL;

/**
 * Utility methods for resolving absolute file paths, etc.
 */
public final class Utils {

    private Utils() {
    }

    /**
     * Resolve the given resource location to a {@code java.net.URL}.
     *
     * @param path the resource location to resolve
     * @return the corresponding URL object
     */
    public static URL getResource(final String path) {
        final ClassLoader classLoader = getDefaultClassLoader();
        final URL url = classLoader != null ? classLoader.getResource(path) : ClassLoader.getSystemResource(path);
        if (url == null) {
            throw new RuntimeException ("Resource [" + path + "] does not exists");
        }
        return url;
    }

    /**
     * Returns the default ClassLoader to use. One of: thread context ClassLoader, the ClassLoader loaded this class,
     * the System ClassLoader in this order whichever is not null first.
     * If neither of them is resolvable {@code null} is returned.
     *
     * @return the default ClassLoader or {@code null} when even the System ClassLoader isn't available
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (classLoader == null) {
            // No thread context class loader -> use class loader of this class.
            classLoader = Utils.class.getClassLoader();
            if (classLoader == null) {
                try {
                    classLoader = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return classLoader;
    }

}
