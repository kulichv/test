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
 * Convenience class representing mutable tuple of three values.
 * <h2 class="header">Thread Safety</h2>
 * This class doesn't provide any synchronization for multi-threaded access and it is
 * responsibility of the user of this class to provide outside synchronization, if needed.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 * @see GridFunc#t4()
 * @see GridFunc#t(Object, Object, Object, Object)
 */
public class GridTuple4<V1, V2, V3, V4> extends GridMetadataAwareAdapter implements GridProduct, GridPeerDeployAware,
    Externalizable {
    /** Value 1. */
    @GridToStringInclude
    private V1 val1;

    /** Value 2. */
    @GridToStringInclude
    private V2 val2;

    /** Value 3. */
    @GridToStringInclude
    private V3 val3;

    /** Value 4. */
    @GridToStringInclude
    private V4 val4;

    /**
     * Empty constructor required by {@link Externalizable}.
     */
    public GridTuple4() {
        // No-op.
    }

    /**
     * Fully initializes this tuple.
     *
     * @param val1 First value.
     * @param val2 Second value.
     * @param val3 Third value.
     * @param val4 Forth value.
     */
    public GridTuple4(V1 val1, V2 val2, V3 val3, V4 val4) {
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
        this.val4 = val4;
    }

    /** {@inheritDoc} */
    @Override public int arity() {
        return 4;
    }

    /** {@inheritDoc} */
    @Override public Object part(int n) {
        switch (n) {
            case 0: return get1();
            case 1: return get2();
            case 2: return get3();
            case 3: return get4();

            default:
                throw new IndexOutOfBoundsException("Invalid product index: " + n);
        }
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
     * Gets third value.
     *
     * @return Third value.
     */
    public V3 get3() {
        return val3;
    }

    /**
     * Gets forth value.
     *
     * @return Forth value.
     */
    public V4 get4() {
        return val4;
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
     * Sets third value.
     *
     * @param val3 Third value.
     */
    public void set3(V3 val3) {
        this.val3 = val3;
    }

    /**
     * Sets fourth value.
     *
     * @param val4 Fourth value.
     */
    public void set4(V4 val4) {
        this.val4 = val4;
    }

    /**
     * Sets all values.
     *
     * @param val1 First value.
     * @param val2 Second value.
     * @param val3 Third value.
     * @param val4 Fourth value.
     */
    public void set(V1 val1, V2 val2, V3 val3, V4 val4) {
        set1(val1);
        set2(val2);
        set3(val3);
        set4(val4);
    }

    /** {@inheritDoc} */
    @Override public Iterator<Object> iterator() {
        return new Iterator<Object>() {
            private int nextIdx = 1;

            @Override public boolean hasNext() {
                return nextIdx < 5;
            }

            @Nullable @Override public Object next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                Object res = null;
                if (nextIdx == 1) {
                    res = get1();
                }
                else if (nextIdx == 2) {
                    res = get2();
                }
                else if (nextIdx == 3) {
                    res = get3();
                }
                else if (nextIdx == 4) {
                    res = get4();
                }

                nextIdx++;

                return res;
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
        out.writeObject(val1);
        out.writeObject(val2);
        out.writeObject(val3);
        out.writeObject(val4);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({"unchecked"})
    @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        val1 = (V1)in.readObject();
        val2 = (V2)in.readObject();
        val3 = (V3)in.readObject();
        val4 = (V4)in.readObject();
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
    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (!(o instanceof GridTuple4)) {
            return false;
        }

        GridTuple4<?, ?, ?, ?> t = (GridTuple4<?, ?, ?, ?>)o;

        return F.eq(val1, t.val2) && F.eq(val2, t.val2) && F.eq(val3, t.val3) && F.eq(val4, t.val4);
    }

    /** {@inheritDoc} */
    @Override public int hashCode() {
        int res = val1 != null ? val1.hashCode() : 0;

        res = 13 * res + (val2 != null ? val2.hashCode() : 0);
        res = 17 * res + (val3 != null ? val3.hashCode() : 0);
        res = 19 * res + (val4 != null ? val4.hashCode() : 0);

        return res;
    }

    /** {@inheritDoc} */
    @Override public String toString() { return S.toString(GridTuple4.class, this); }
}
