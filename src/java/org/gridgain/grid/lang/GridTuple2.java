// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.lang;

import org.gridgain.grid.*;
import org.gridgain.grid.typedef.*;
import org.gridgain.grid.typedef.internal.*;
import org.gridgain.grid.util.tostring.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

/**
 * Convenience class representing mutable tuple of two values.
 * <h2 class="header">Thread Safety</h2>
 * This class doesn't provide any synchronization for multi-threaded access
 * and it is responsibility of the user of this class to provide outside
 * synchronization, if needed.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 * @see GridFunc#t2()
 * @see GridFunc#t(Object, Object)
 */
public class GridTuple2<V1, V2> extends GridMetadataAwareAdapter implements Map<V1, V2>, Map.Entry<V1, V2>,
    GridProduct, GridPeerDeployAware, Externalizable {
    /** First value. */
    @GridToStringInclude
    private V1 val1;

    /** Second value. */
    @GridToStringInclude
    private V2 val2;

    /**
     * Empty constructor required by {@link Externalizable}.
     */
    public GridTuple2() {
        // No-op.
    }

    /**
     * Fully initializes this tuple.
     *
     * @param val1 First value.
     * @param val2 Second value.
     */
    public GridTuple2(V1 val1, V2 val2) {
        this.val1 = val1;
        this.val2 = val2;
    }

    /** {@inheritDoc} */
    @Override public int arity() {
        return 2;
    }

    /** {@inheritDoc} */
    @Override public Object part(int n) {
        switch (n) {
            case 0: return get1();
            case 1: return get2();

            default:
                throw new IndexOutOfBoundsException("Invalid product index: " + n);
        }
    }

    /**
     * Swaps values.
     *
     * @return New tuple with swapped values.
     */
    public GridTuple2<V2, V1> swap() {
        return F.t(val2, val1);
    }

    /**
     * Gets first value.
     *
     * @return First value.
     */
    public V1 get1() {
        return val1;
    }

    /**
     * Gets second value.
     *
     * @return Second value.
     */
    public V2 get2() {
        return val2;
    }

    /**
     * Sets first value.
     *
     * @param val1 First value.
     */
    public void set1(V1 val1) {
        this.val1 = val1;
    }

    /**
     * Sets second value.
     *
     * @param val2 Second value.
     */
    public void set2(V2 val2) {
        this.val2 = val2;
    }

    /**
     * Sets both values in the tuple.
     *
     * @param val1 First value.
     * @param val2 Second value.
     */
    public void set(V1 val1, V2 val2) {
        set1(val1);
        set2(val2);
    }

    /** {@inheritDoc} */
    @Override public V1 getKey() {
        return val1;
    }

    /** {@inheritDoc} */
    @Override public V2 getValue() {
        return val2;
    }

    /** {@inheritDoc} */
    @Override public V2 setValue(V2 val) {
        V2 old = val2;

        val2 = val;

        return old;
    }

    /** {@inheritDoc} */
    @Override public Iterator<Object> iterator() {
        return new Iterator<Object>() {
            private int nextIdx = 1;

            @Override public boolean hasNext() {
                return nextIdx < 3;
            }

            @Override @Nullable public Object next() {
                if (!hasNext())
                    throw new NoSuchElementException();

                Object res = null;

                if (nextIdx == 1)
                    res = get1();
                else if (nextIdx == 2)
                    res = get2();

                nextIdx++;

                return res;
            }

            @Override public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /** {@inheritDoc} */
    @Override public int size() {
        return val1 == null && val2 == null ? 0 : 1;
    }

    /** {@inheritDoc} */
    @Override public boolean isEmpty() {
        return size() == 0;
    }

    /** {@inheritDoc} */
    @Override public boolean containsKey(Object key) {
        return F.eq(val1, key);
    }

    /** {@inheritDoc} */
    @Override public boolean containsValue(Object val) {
        return F.eq(val2, val);
    }

    /** {@inheritDoc} */
    @Override @Nullable public V2 get(Object key) {
        return containsKey(key) ? val2 : null;
    }

    /** {@inheritDoc} */
    @Override @Nullable
    public V2 put(V1 key, V2 val) {
        V2 old = containsKey(key) ? val2 : null;

        set(key, val);

        return old;
    }

    /** {@inheritDoc} */
    @Override @Nullable public V2 remove(Object key) {
        if (containsKey(key)) {
            V2 v2 = val2;

            val1 = null;
            val2 = null;

            return v2;
        }

        return null;
    }

    /** {@inheritDoc} */
    @Override public void putAll(Map<? extends V1, ? extends V2> m) {
        A.notNull(m, "m");
        A.ensure(m.size() <= 1, "m.size() <= 1");

        for (Map.Entry<? extends V1, ? extends V2> e : m.entrySet())
            put(e.getKey(), e.getValue());
    }

    /** {@inheritDoc} */
    @Override public void clear() {
        val1 = null;
        val2 = null;
    }

    /** {@inheritDoc} */
    @Override public Set<V1> keySet() {
        return Collections.singleton(val1);
    }

    /** {@inheritDoc} */
    @Override public Collection<V2> values() {
        return Collections.singleton(val2);
    }

    /** {@inheritDoc} */
    @Override public Set<Map.Entry<V1, V2>> entrySet() {
        return Collections.<Entry<V1, V2>>singleton(this);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException", "OverriddenMethodCallDuringObjectConstruction"})
    @Override public Object clone() {
        return super.clone();
    }

    /** {@inheritDoc} */
    @Override public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(val1);
        out.writeObject(val2);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({"unchecked"})
    @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        val1 = (V1)in.readObject();
        val2 = (V2)in.readObject();
    }

    /** {@inheritDoc} */
    @Override public Class<?> deployClass() {
        ClassLoader clsLdr = getClass().getClassLoader();

        for (Object o : this)
            if (o != null && !o.getClass().getClassLoader().equals(clsLdr))
                return o.getClass();

        return getClass();
    }

    /** {@inheritDoc} */
    @Override public ClassLoader classLoader() {
        return deployClass().getClassLoader();
    }

    /** {@inheritDoc} */
    @Override public int hashCode() {
        return val1 == null ? 0 : val1.hashCode() * 31 + (val2 == null ? 0 : val2.hashCode());
    }

    /** {@inheritDoc} */
    @Override public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof GridTuple2))
            return false;

        GridTuple2<?, ?> t = (GridTuple2<?, ?>)o;

        // Both nulls or equals.
        return F.eq(val1, t.val1) && F.eq(val2, t.val2);
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridTuple2.class, this);
    }
}
