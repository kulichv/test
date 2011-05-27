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
import org.jetbrains.annotations.*;

import java.io.*;

/**
 * Base API for all supported types of cache queries: {@link GridCacheQuery}, {@link GridCacheReduceQuery},
 *  {@link GridCacheTransformQuery}.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public interface GridCacheQueryBase<K, V> extends GridMetadataAware, Closeable {
    /** Default query page size. */
    public static final int DFLT_PAGE_SIZE = 1024;

    /**
     * Gets query id.
     *
     * @return type Query id.
     */
    public int id();

    /**
     * Sets query type.
     *
     * @param type Query type.
     */
    public void type(GridCacheQueryType type);

    /**
     * Gets query type.
     *
     * @return type Query type.
     */
    public GridCacheQueryType type();

    /**
     * Sets query clause.
     *
     * @param clause Query clause.
     */
    public void clause(String clause);

    /**
     * Gets query clause.
     *
     * @return Query clause.
     */
    @Nullable public String clause();

    /**
     * Sets Java class name of the values selected by the query.
     *
     * @param clsName Java class name of the values selected by the query.
     */
    public void className(String clsName);

    /**
     * Gets Java class name of the values selected by the query.
     *
     * @return Java class name of the values selected by the query.
     */
    @Nullable public String className();

    /**
     * Sets result page size. If not provided, {@link #DFLT_PAGE_SIZE} will be used.
     * Results are returned from queried nodes one page at a tme.
     *
     * @param  pageSize Page size.
     */
    public void pageSize(int pageSize);

    /**
     * Gets query result page size.
     *
     * @return Query page size.
     */
    public int pageSize();

    /**
     * Sets query timeout.
     *
     * @param timeout Query timeout.
     */
    public void timeout(long timeout);

    /**
     * Gets query timeout.
     *
     * @return Query timeout.
     */
    public long timeout();

    /**
     * Sets whether or not to keep all query results local. If not - only the current page
     * is kept locally. Default value is {@code true}.
     *
     * @param keepAll Keep results or not.
     */
    public void keepAll(boolean keepAll);

    /**
     * Gets query {@code keepAll} flag.
     *
     * @return Query {@code keepAll} flag.
     */
    public boolean keepAll();

    /**
     * Sets whether or not to include backup entries into query result. This flag
     * is {@code false} by default.
     *
     * @param incBackups Query {@code includeBackups} flag.
     */
    public void includeBackups(boolean incBackups);

    /**
     * Gets query {@code includeBackups} flag.
     *
     * @return Query {@code includeBackups} flag.
     */
    public boolean includeBackups();

    /**
     * Flag indicating whether values should be read-through from persistent storage
     * if not available in memory. Values may be {@code nulls} if {@code 'invalidation'}
     * mode is set on cache transactions.
     *
     * @param readThrough Flag indicating whether to read through.
     */
    public void readThrough(boolean readThrough);

    /**
     * Gets query {@code readThrough} flag.
     *
     * @return Query {@code readThrough} flag.
     */
    public boolean readThrough();

    /**
     * Optional filter factory to be used on queried nodes to create key filters prior
     * to visiting or returning key-value pairs to user. The factory is a closure that accepts
     * array of objects provided by {@link GridCacheQuery#closureArguments(Object...)} or
     * {@link GridCacheReduceQuery#closureArguments(Object...)} or
     * {@link GridCacheTransformQuery#closureArguments(Object...)} methods as a parameter
     * and returns predicate filter for keys.
     * <p>
     * If factory is set, then it will be invoked for every query execution. Only keys that
     * pass the filter will be included in query result. If state of the filter changes after
     * each query execution, then factory should return a new filter for every execution.
     *
     * @param factory Optional factory closure to create key filters.
     */
    public void remoteKeyFilter(@Nullable GridClosure<Object[], GridPredicate<? super K>> factory);

    /**
     * Optional filter factory to be used on queried nodes to create value filters prior
     * to visiting or returning key-value pairs to user. The factory is a closure that accepts
     * array of objects provided by {@link GridCacheQuery#closureArguments(Object...)} or
     * {@link GridCacheReduceQuery#closureArguments(Object...)} or
     * {@link GridCacheTransformQuery#closureArguments(Object...)} methods as a parameter
     * and returns predicate filter for values.
     * <p>
     * If factory is set, then it will be invoked for every query execution. Only values that
     * pass the filter will be included in query result. If state of the filter changes after
     * each query execution, then factory should return a new filter for every execution.
     *
     * @param factory Optional factory closure to create value filters.
     */
    public void remoteValueFilter(@Nullable GridClosure<Object[], GridPredicate<? super V>> factory);

    /**
     * Gets query metrics.
     *
     * @return Query metrics.
     */
    public GridCacheQueryMetrics metrics();
}
