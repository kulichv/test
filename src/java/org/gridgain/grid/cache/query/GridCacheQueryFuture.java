// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.cache.query;

import org.gridgain.grid.*;
import org.gridgain.grid.lang.*;
import java.util.*;

/**
 * Cache query future returned by {@link GridCacheQuery#execute(GridProjection...)} or by
 * analogous methods on {@link GridCacheReduceQuery} and {@link GridCacheTransformQuery}.
 * Refer to corresponding query javadoc documentation for more information.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 * @see GridCacheQuery
 * @see GridCacheReduceQuery
 * @see GridCacheTransformQuery
 */
public interface GridCacheQueryFuture<T> extends GridFuture<Collection<T>>, GridIterable<T> {
    /**
     * Number of elements currently fetched.
     *
     * @return Number of elements currently fetched.
     */
    public int size();

    /**
     * Tests whether or not next {@link #next()} call will block.
     *
     * @return Whether or not next {@link #next()} call will block.
     */
    public boolean available();

    /**
     * Checks if all data is fetched by the query.
     *
     * @return {@code True} if all data is fetched, {@code false} otherwise.
     */
    @Override public boolean isDone();

    /**
     * Cancels this query future and stop receiving any further results for the query
     * associated with this future.
     *
     * @return {@inheritDoc}
     * @throws GridException {@inheritDoc}
     */
    @Override public boolean cancel() throws GridException;
}
