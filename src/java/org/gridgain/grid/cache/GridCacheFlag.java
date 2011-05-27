// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.cache;

import org.gridgain.grid.lang.*;
import org.jetbrains.annotations.*;

/**
 * Cache projection flags that specify projection behaviour. This flags can be explicitly passed into
 * the following methods on {@link GridCacheProjection}:
 * <ul>
 * <li>{@link GridCacheProjection#flagsOn(GridCacheFlag...)}</li>
 * <li>{@link GridCacheProjection#flagsOff(GridCacheFlag...)}</li>
 * </ul>
 * Also, some flags, like {@link #STRICT}, {@link #LOCAL}, or {@link #READ} may be implicitly set whenever
 * creating new projections and passing entries to predicate filters. 
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public enum GridCacheFlag {
    /**
     * No null values will be allowed into projection. Essentially this means that
     * unless entry has a {@code non-null} value, it won't show up in any of the
     * projection operations.
     * <p>
     * This projection is implicitly returned for the following cache operations:
     * <ul>
     * <li>{@link GridCacheProjection#projection(Class, Class)}</li>
     * <li>{@link GridCacheProjection#projection(GridPredicate[])}</li>
     * </ul>
     */
    STRICT,

    /**
     * Only operations that don't require any communication with
     * other cache nodes are allowed. This flag is automatically set
     * on projection and all the entries that are given to predicate
     * filters.
     */
    LOCAL,

    /**
     * Only operations that don't change cached data are allowed.
     * This flag is automatically set on projection and all the entries
     * that are given to predicate filters.
     */
    READ,

    /**
     * Clone values prior to returning them to user.
     * <p>
     * Whenever values are returned from cache, they cannot be directly updated
     * as cache holds the same references internally. If it is needed to
     * update values that are returned from cache, this flag will provide
     * automatic cloning of values prior to returning so they can be directly
     * updated.
     *
     * @see GridCacheConfiguration#getCloner()
     */
    CLONE,

    /** Skips store, i.e. no read-through and no write-through behavior. */
    SKIP_STORE,

    /** Skip swap space for reads and writes. */
    SKIP_SWAP,

    /** Synchronous commit. */
    SYNC_COMMIT,

    /** Synchronous rollback. */
    SYNC_ROLLBACK,

    /**
     * Switches a cache projection to work in {@code 'invalidation'} mode.
     * Instead of updating remote entries with new values, small invalidation
     * messages will be sent to set the values to {@code null}.
     * 
     * @see GridCacheTx#isInvalidate()
     * @see GridCacheConfiguration#isInvalidate() 
     */
    INVALIDATE;

    /** */
    private static final GridCacheFlag[] VALS = values();

    /**
     * Efficiently gets enumerated value from its ordinal.
     *
     * @param ord Ordinal value.
     * @return Enumerated value or {@code null} if ordinal out of range.
     */
    @Nullable public static GridCacheFlag fromOrdinal(int ord) {
        return ord >= 0 && ord < VALS.length ? VALS[ord] : null;
    }
}
