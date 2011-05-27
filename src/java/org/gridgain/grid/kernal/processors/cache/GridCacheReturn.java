// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.cache;

import org.gridgain.grid.typedef.internal.*;
import org.gridgain.grid.util.tostring.*;
import org.jetbrains.annotations.*;

/**
 * Return value for cases where both, value and success flag need to be returned.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridCacheReturn<V> {
    /** Value. */
    @GridToStringInclude
    private volatile V v;

    /** Success flag. */
    private volatile boolean success;

    /**
     * Empty constructor.
     */
    public GridCacheReturn() {
        // No-op.
    }

    /**
     * @param v Value.
     */
    public GridCacheReturn(V v) {
        this.v = v;
    }

    /**
     *
     * @param success Success flag.
     */
    public GridCacheReturn(boolean success) {
        this.success = success;
    }

    /**
     *
     * @param v Value.
     * @param success Success flag.
     */
    public GridCacheReturn(V v, boolean success) {
        this.v = v;
        this.success = success;
    }

    /**
     * @return Value.
     */
    @Nullable public V value() {
        return v;
    }

    /**
     * Checks if value is not {@code null}.
     *
     * @return {@code True} if value is not {@code null}.
     */
    public boolean hasValue() {
        return v != null;
    }

    /**
     * @param v Value.
     * @return This instance for chaining.
     */
    public GridCacheReturn<V> value(V v) {
        this.v = v;

        return this;
    }

    /**
     * @param v Value.
     * @return This instance for chaining.
     */
    public GridCacheReturn<V> valueIfNull(V v) {
        if (this.v == null)
            this.v = v;

        return this;
    }

    /**
     * @return Success flag.
     */
    public boolean success() {
        return success;
    }

    /**
     * @param v Value to set.
     * @param success Success flag to set.
     * @return This instance for chaining.
     */
    public GridCacheReturn<V> set(@Nullable V v, boolean success) {
        this.v = v;
        this.success = success;

        return this;
    }

    /**
     * @param v Value.
     * @param success Success flag.
     * @return This instance for chaining.
     */
    public GridCacheReturn<V> setIfNull(V v, boolean success) {
        if (this.v == null) {
            this.v = v;
            this.success = success;
        }

        return this;
    }

    /**
     * @param success Success flag.
     * @return This instance for chaining.
     */
    public GridCacheReturn<V> success(boolean success) {
        this.success = success;

        return this;
    }

    /** {@inheritDoc} */
    @Override public String toString() { return S.toString(GridCacheReturn.class, this); }
}
