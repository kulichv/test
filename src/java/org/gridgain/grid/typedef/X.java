// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.typedef;

import org.gridgain.grid.*;
import org.gridgain.grid.lang.utils.*;
import org.gridgain.grid.typedef.internal.*;
import org.jetbrains.annotations.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Defines global scope. Contains often used utility functions allowing to cut down on code bloat. This
 * is somewhat analogous to <code>Predef</code> in Scala. Note that this should only be used
 * when this typedef <b>does not sacrifice</b> the code readability.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public final class X {
    /** Time span dividers. */
    private static final long[] SPAN_DIVS = new long[] {1000L, 60L, 60L, 60L};

    /**
     * Ensures singleton.
     */
    private X() {
        // No-op.
    }

    /**
     * Alias for {@code System.out.println()}.
     */
    public static void println() {
        System.out.println();
    }

    /**
     * Alias for {@code System.out.println}.
     *
     * @param s1 First string to print.
     * @param rest Optional list of objects to print as well.
     */
    public static void println(@Nullable String s1, @Nullable Object... rest) {
        System.out.println(s1);

        if (rest != null && rest.length > 0)
            for (Object obj : rest)
                System.out.println(obj);
    }

    /**
     * Alias for {@code System.err.println}.
     *
     * @param s1 First string to print.
     * @param rest Optional list of objects to print as well.
     */
    public static void error(@Nullable String s1, @Nullable Object... rest) {
        System.err.println(s1);

        if (rest != null && rest.length > 0)
            for (Object obj : rest)
                System.err.println( obj);
    }

    /**
     * Alias for {@code System.out.print}.
     *
     * @param s1 First string to print.
     * @param rest Optional list of objects to print as well.
     */
    public static void print(@Nullable String s1, @Nullable Object... rest) {
        System.out.print(s1);

        if (rest != null && rest.length > 0)
            for (Object obj : rest)
                System.out.print(obj);
    }

    /**
     * Gets either system property or environment variable with given name.
     *
     * @param name Name of the system property or environment variable.
     * @return Value of the system property or environment variable. Returns
     *      {@code null} if neither can be found for given name.
     */
    @Nullable public static String getSystemOrEnv(String name) {
        assert name != null;

        String v = System.getProperty(name);

        if (v == null)
            v = System.getenv(name);

        return v;
    }

    /**
     * Creates string presentation of given time {@code span} in hh:mm:ss:msec {@code HMSM} format.
     *
     * @param span Time span.
     * @return String presentation.
     */
    public static String timeSpan2HMSM(long span) {
        long[] t = new long[4];

        long sp = span;

        for (int i = 0; i < SPAN_DIVS.length && sp > 0; sp /= SPAN_DIVS[i++])
            t[i] = sp % SPAN_DIVS[i];

        return (t[3] < 10 ? "0" + t[3] : Long.toString(t[3])) + ':' +
            (t[2] < 10 ? "0" + t[2] : Long.toString(t[2])) + ':' +
            (t[1] < 10 ? "0" + t[1] : Long.toString(t[1])) + ':' +
            (t[0] < 10 ? "0" + t[0] : Long.toString(t[0]));
    }

    /**
     * Creates string presentation of given time {@code span} in hh:mm:ss {@code HMS} format.
     *
     * @param span Time span.
     * @return String presentation.
     */
    public static String timeSpan2HMS(long span) {
        long[] t = new long[4];

        long sp = span;

        for (int i = 0; i < SPAN_DIVS.length && sp > 0; sp /= SPAN_DIVS[i++])
            t[i] = sp % SPAN_DIVS[i];

        return (t[3] < 10 ? "0" + t[3] : Long.toString(t[3])) + ':' +
            (t[2] < 10 ? "0" + t[2] : Long.toString(t[2])) + ':' +
            (t[1] < 10 ? "0" + t[1] : Long.toString(t[1]));
    }

    /**
     * Clones a passed in object. If parameter {@code deep} is set to {@code true}
     * then this method will use deep cloning algorithm based on deep reflection
     * ignoring {@link Cloneable} interface unless parameter {@code honorCloneable}
     * is set to false.
     * <p>
     * If {@code deep} is {@code false} then this method will check the object for
     * {@link Cloneable} interface and use {@link Object#clone()} to make a copy,
     * otherwise the object itself will be returned.
     *
     * @param obj Object to create a clone from.
     * @param deep {@code true} to use algorithm of deep cloning. If {@code false}
     *      then this method will always be checking whether a passed in object
     *      implements interface {@link Cloneable} and if it does then method
     *      {@link Object#clone()} will be used to clone object, if does not
     *      then the object itself will be returned.
     * @param honorCloneable Flag indicating whether {@link Cloneable} interface
     *      should be honored or not when cloning. This parameter makes sense only if
     *      parameter {@code deep} is set to {@code true}.
     * @param <T> Type of cloning object.
     * @return Copy of a passed in object.
     */
    @SuppressWarnings({"unchecked"})
    @Nullable public static <T> T cloneObject(@Nullable T obj, boolean deep, boolean honorCloneable) {
        if (obj == null)
            return null;

        try {
            return !deep ? shallowClone(obj) : (T)deepClone(new GridLeanMap<Integer, Integer>(),
                new ArrayList<Object>(), obj, honorCloneable);
        }
        catch (Throwable e) {
            throw new GridRuntimeException("Unable to clone instance of class: " + obj.getClass(), e);
        }
    }

    /**
     * @param obj Object to make a clone for.
     * @param <T> Type of cloning object.
     * @return Copy of a passed in object.
     */
    @SuppressWarnings({"unchecked"})
    @Nullable private static <T> T shallowClone(@Nullable T obj) {
        if (obj == null)
            return null;

        if (!(obj instanceof Cloneable))
            return obj;

        if (obj.getClass().isArray())
            return obj instanceof byte[] ? (T)(((byte[])obj).clone()) :
                obj instanceof short[] ? (T)(((short[])obj).clone()) :
                    obj instanceof char[] ? (T)(((char[])obj).clone()) :
                        obj instanceof int[] ? (T)(((int[])obj).clone()) :
                            obj instanceof long[] ? (T)(((long[])obj).clone()) :
                                obj instanceof float[] ? (T)(((float[])obj).clone()) :
                                    obj instanceof double[] ? (T)(((double[])obj).clone()) :
                                        obj instanceof boolean[] ? (T)(((boolean[])obj).clone()) :
                                            (T)(((Object[])obj).clone());

        try {
            Method mtd = obj.getClass().getMethod("clone");

            boolean set = false;

            if (!mtd.isAccessible())
                mtd.setAccessible(set = true);

            T clone = (T)mtd.invoke(obj);

            if (set)
                mtd.setAccessible(false);

            return clone;
        }
        catch (Exception e) {
            throw new GridRuntimeException("Unable to clone instance of class: " + obj.getClass(), e);
        }
    }

    /**
     * Recursively clones the object.
     *
     * @param identityIdxs Map of object identities to indexes in {@code clones} parameter.
     * @param clones List of already cloned objects.
     * @param obj The object to deep-clone.
     * @param honorCloneable {@code true} if method should account {@link Cloneable} interface.
     * @return Clone of the input object.
     * @throws Exception If deep-cloning fails.
     */
    @Nullable private static Object deepClone(Map<Integer, Integer> identityIdxs, List<Object> clones, @Nullable Object obj,
        boolean honorCloneable) throws Exception {
        if (obj == null)
            return null;

        if (honorCloneable && obj instanceof Cloneable)
            return shallowClone(obj);

        Integer idx = identityIdxs.get(System.identityHashCode(obj));

        Object clone = null;

        if (idx != null)
            clone = clones.get(idx);

        if (clone != null)
            return clone;

        Class cls = obj.getClass();

        if (cls.isArray()) {
            Class<?> arrType = cls.getComponentType();

            int len = Array.getLength(obj);

            clone = Array.newInstance(arrType, len);

            for (int i = 0; i < len; i++)
                Array.set(clone, i, deepClone(identityIdxs, clones, Array.get(obj, i), honorCloneable));

            clones.add(clone);

            identityIdxs.put(System.identityHashCode(obj), clones.size() - 1);

            return clone;
        }

        clone = U.forceNewInstance(cls);

        if (clone == null)
            throw new GridRuntimeException("Failed to clone object (empty constructor could not be assigned): " + obj);

        clones.add(clone);

        identityIdxs.put(System.identityHashCode(obj), clones.size() - 1);

        for (Class<?> c = cls; c != Object.class; c = c.getSuperclass())
            for (Field f : c.getDeclaredFields())
                cloneField(identityIdxs, clones, obj, clone, f, honorCloneable);

        return clone;
    }

    /**
     * @param identityIdxs Map of object identities to indexes in {@code clones} parameter.
     * @param clones List of already cloned objects.
     * @param obj Object to clone.
     * @param clone Clone.
     * @param f Field to clone.
     * @param honorCloneable {@code true} if method should account {@link Cloneable} interface.
     * @throws Exception If failed.
     */
    private static void cloneField(Map<Integer, Integer> identityIdxs, List<Object> clones, Object obj, Object clone,
        Field f, boolean honorCloneable) throws Exception {
        int modifiers = f.getModifiers();

        // Skip over static fields.
        if (Modifier.isStatic(modifiers))
            return;

        boolean set = false;

        if (!f.isAccessible()) {
            f.setAccessible(true);

            set = true;
        }

        try {
            if (f.getType().isPrimitive())
                f.set(clone, f.get(obj));
            else
                f.set(clone, deepClone(identityIdxs, clones, f.get(obj), honorCloneable));
        }
        finally {
            if (set)
                f.setAccessible(false);
        }
    }

    /**
     * Checks if passed in {@code 'Throwable'} has given class in {@code 'cause'} hierarchy.
     *
     * @param t Throwable to check (if {@code null}, {@code false} is returned).
     * @param cls Cause classes to check (if {@code null} or empty, {@code false} is returned).
     * @return {@code True} if one of the causing exception is an instance of passed in classes,
     *      {@code false} otherwise.
     */
    public static boolean hasCause(@Nullable Throwable t, @Nullable Class<? extends Throwable>... cls) {
        if (t == null || F.isEmpty(cls))
            return false;

        assert cls != null;

        for (Throwable th = t; th != null; th = th.getCause())
            for (Class<? extends Throwable> c : cls) {
                if (c.isAssignableFrom(th.getClass()))
                    return true;

                if (th.getCause() == th)
                    break;
            }

        return false;
    }

    /**
     * Gets first cause if passed in {@code 'Throwable'} has given class in {@code 'cause'} hierarchy.
     *
     * @param t Throwable to check (if {@code null}, {@code null} is returned).
     * @param cls Cause class to get cause (if {@code null}, {@code null} is returned).
     * @return First causing exception of passed in class, {@code null} otherwise.
     */
    @SuppressWarnings({"unchecked"})
    @Nullable public static <T extends Throwable> T cause(@Nullable Throwable t, @Nullable Class<T> cls) {
        if (t == null || cls == null)
            return null;

        assert cls != null;

        for (Throwable th = t; th != null; th = th.getCause()) {
            if (cls.isAssignableFrom(th.getClass()))
                return (T)th;

            if (th.getCause() == th)
                break;
        }

        return null;
    }

    /**
     * Synchronously waits for all futures in the collection.
     *
     * @param futs Futures to wait for.
     * @throws GridException If any of the futures threw exception.
     */
    public static void waitAll(@Nullable Iterable<GridFuture<?>> futs) throws GridException {
        if (F.isEmpty(futs))
            return;

        for (GridFuture fut : futs)
            fut.get();
    }
}
