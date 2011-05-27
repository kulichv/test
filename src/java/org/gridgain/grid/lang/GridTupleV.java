// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.lang;

import org.gridgain.grid.*;
import org.gridgain.grid.typedef.internal.*;
import org.gridgain.grid.util.tostring.*;
import java.io.*;
import java.util.*;

/**
 * Constructs a tuple over a given array.
 * <h2 class="header">Thread Safety</h2>
 * This class doesn't provide any synchronization for multi-threaded access and it is
 * responsibility of the user of this class to provide outside synchronization, if needed.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 * @see GridFunc#tv(Object...)
 */
public class GridTupleV extends GridMetadataAwareAdapter implements GridProduct, GridPeerDeployAware, Externalizable {
    /** Tuple values. */
    @GridToStringInclude private Object[] vals;

    /**
     * Empty constructor required by {@link Externalizable}.
     */
    public GridTupleV() {
        // No-op.
    }

    /**
     * Initializes tuple with given object count.
     *
     * @param cnt Count of objects to be stored in the tuple.
     */
    public GridTupleV(int cnt) {
        A.ensure(cnt > 0, "cnt > 0");

        vals = new Object[cnt];
    }

    /**
     * Constructs tuple around passed in array.
     *
     * @param vals Values.
     */
    public GridTupleV(Object... vals) {
        this.vals = vals;
    }

    /** {@inheritDoc} */
    @Override public int arity() {
        return vals.length;
    }

    /** {@inheritDoc} */
    @Override public Object part(int n) {
        return get(n);
    }

    /**
     * Retrieves value at given index.
     *
     * @param i Index of the value to get.
     * @param <V> Value type.
     * @return Value at given index.
     */
    @SuppressWarnings({"unchecked"})
    public <V> V get(int i) {
        A.ensure(i < vals.length, "i < vals.length");

        return (V)vals[i];
    }

    /**
     * Sets value at given index.
     *
     * @param i Index to set.
     * @param v Value to set.
     * @param <V> Value type.
     */
    public <V> void set(int i, V v) {
        A.ensure(i < vals.length, "i < vals.length");

        vals[i] = v;
    }

    /**
     * Sets given values starting at {@code 0} position.
     *
     * @param v Values to set.
     */
    public void set(Object... v) {
        A.ensure(v.length <= vals.length, "v.length <= vals.length");

        if (v.length > 0) {
            System.arraycopy(v, 0, vals, 0, v.length);
        }
    }

    /**
     * Sets given values starting at provided position in the tuple.
     *
     * @param pos Position to start from.
     * @param v Values to set.
     */
    public void set(int pos, Object... v) {
        A.ensure(pos > 0, "pos > 0");
        A.ensure(v.length + pos <= vals.length, "v.length + pos <= vals.length");

        if (v.length > 0) {
            System.arraycopy(v, 0, vals, pos, v.length);
        }
    }

    /**
     * Gets internal array. Changes to this array will change this tuple.
     *
     * @return Internal array.
     */
    public Object[] getAll() {
        return vals;
    }

    /** {@inheritDoc} */
    @Override public Iterator<Object> iterator() {
        return new Iterator<Object>() {
            private int nextIdx;

            @Override public boolean hasNext() {
                return nextIdx < vals.length;
            }

            @Override public Object next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                return vals[nextIdx++];
            }

            @Override public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /** {@inheritDoc} */
    @SuppressWarnings( {"CloneDoesntDeclareCloneNotSupportedException"})
    @Override public Object clone() {
        return super.clone();
    }

    /** {@inheritDoc} */
    @Override public void writeExternal(ObjectOutput out) throws IOException {
        U.writeArray(out, vals);
    }

    /** {@inheritDoc} */
    @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        vals = U.readArray(in);
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
    @Override
    public int hashCode() {
        return Arrays.hashCode(vals);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof GridTupleV && Arrays.equals(vals, ((GridTupleV)o).vals);
    }

    /** {@inheritDoc} */
    @Override public String toString() { return S.toString(GridTupleV.class, this); }
}
