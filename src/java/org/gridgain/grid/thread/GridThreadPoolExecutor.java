// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.thread;

import org.jetbrains.annotations.*;

import java.util.concurrent.*;

/**
 * An {@link ExecutorService} that executes submitted tasks using pooled grid threads.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridThreadPoolExecutor extends ThreadPoolExecutor {
    /** Default core pool size (value is {@code 100}). */
    public static final int DFLT_CORE_POOL_SIZE = 100;

    /**
     * Creates a new service with default initial parameters.
     * Default values are:
     * <table class="doctable">
     * <tr>
     *      <th>Name</th>
     *      <th>Default Value</th>
     * </tr>
     * <tr>
     *      <td>Core Pool Size</td>
     *      <td>{@code 100} (see {@link #DFLT_CORE_POOL_SIZE}).</td>
     * </tr>
     * <tr>
     *      <td>Maximum Pool Size</td>
     *      <td>None, is it is not used for unbounded queues.</td>
     * </tr>
     * <tr>
     *      <td>Keep alive time</td>
     *      <td>No limit (see {@link Long#MAX_VALUE}).</td>
     * </tr>
     * <tr>
     *      <td>Blocking Queue (see {@link BlockingQueue}).</td>
     *      <td>Unbounded linked blocking queue (see {@link LinkedBlockingQueue}).</td>
     * </tr>
     * </table>
     */
    public GridThreadPoolExecutor() {
        this(DFLT_CORE_POOL_SIZE, DFLT_CORE_POOL_SIZE, 0,
            new LinkedBlockingQueue<Runnable>(), new GridThreadFactory(null), null);
    }

    /**
     * Creates a new service with the given initial parameters.
     *
     * @param corePoolSize The number of threads to keep in the pool, even if they are idle.
     * @param maxPoolSize The maximum number of threads to allow in the pool.
     * @param keepAliveTime When the number of threads is greater than the core, this is the maximum time
     *      that excess idle threads will wait for new tasks before terminating.
     * @param workQueue The queue to use for holding tasks before they are executed. This queue will hold only
     *      runnable tasks submitted by the {@link #execute(Runnable)} method.
     */
    public GridThreadPoolExecutor(int corePoolSize, int maxPoolSize, long keepAliveTime,
        BlockingQueue<Runnable> workQueue) {
        this(corePoolSize, maxPoolSize, keepAliveTime, workQueue, new GridThreadFactory(null), null);
    }

    /**
     * Creates a new service with the given initial parameters.
     *
     * @param corePoolSize The number of threads to keep in the pool, even if they are idle.
     * @param maxPoolSize The maximum number of threads to allow in the pool.
     * @param keepAliveTime When the number of threads is greater than the core, this is the maximum time
     *      that excess idle threads will wait for new tasks before terminating.
     * @param workQueue The queue to use for holding tasks before they are executed. This queue will hold only the
     *      runnable tasks submitted by the {@link #execute(Runnable)} method.
     * @param handler Optional handler to use when execution is blocked because the thread bounds and queue
     *      capacities are reached. If {@code null} then {@code AbortPolicy}
     *      handler is used by default.
     */
    public GridThreadPoolExecutor(int corePoolSize, int maxPoolSize, long keepAliveTime,
        BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        this(corePoolSize, maxPoolSize, keepAliveTime, workQueue, new GridThreadFactory(null), handler);
    }

    /**
     * Creates a new service with default initial parameters.
     * Default values are:
     * <table class="doctable">
     * <tr>
     *      <th>Name</th>
     *      <th>Default Value</th>
     * </tr>
     * <tr>
     *      <td>Core Pool Size</td>
     *      <td>{@code 100} (see {@link #DFLT_CORE_POOL_SIZE}).</td>
     * </tr>
     * <tr>
     *      <td>Maximum Pool Size</td>
     *      <td>None, is it is not used for unbounded queues.</td>
     * </tr>
     * <tr>
     *      <td>Keep alive time</td>
     *      <td>No limit (see {@link Long#MAX_VALUE}).</td>
     * </tr>
     * <tr>
     *      <td>Blocking Queue (see {@link BlockingQueue}).</td>
     *      <td>Unbounded linked blocking queue (see {@link LinkedBlockingQueue}).</td>
     * </tr>
     * </table>
     *
     * @param gridName Name of the grid.
     */
    public GridThreadPoolExecutor(String gridName) {
        this(DFLT_CORE_POOL_SIZE, DFLT_CORE_POOL_SIZE, 0,
            new LinkedBlockingQueue<Runnable>(), new GridThreadFactory(gridName), null);
    }

    /**
     * Creates a new service with the given initial parameters.
     *
     * @param gridName Name of the grid
     * @param corePoolSize The number of threads to keep in the pool, even if they are idle.
     * @param maxPoolSize The maximum number of threads to allow in the pool.
     * @param keepAliveTime When the number of threads is greater than the core, this is the maximum time
     *      that excess idle threads will wait for new tasks before terminating.
     * @param workQueue The queue to use for holding tasks before they are executed. This queue will hold only
     *      runnable tasks submitted by the {@link #execute(Runnable)} method.
     */
    public GridThreadPoolExecutor(String gridName, int corePoolSize, int maxPoolSize, long keepAliveTime,
        BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue,
            new GridThreadFactory(gridName));
    }

    /**
     * Creates a new service with the given initial parameters.
     *
     * @param gridName Name of the grid.
     * @param corePoolSize The number of threads to keep in the pool, even if they are idle.
     * @param maxPoolSize The maximum number of threads to allow in the pool.
     * @param keepAliveTime When the number of threads is greater than the core, this is the maximum time
     *      that excess idle threads will wait for new tasks before terminating.
     * @param workQueue The queue to use for holding tasks before they are executed. This queue will hold only the
     *      runnable tasks submitted by the {@link #execute(Runnable)} method.
     * @param handler Optional handler to use when execution is blocked because the thread bounds and queue
     *      capacities are reached. If {@code null} then {@code AbortPolicy}
     *      handler is used by default.
     */
    public GridThreadPoolExecutor(String gridName, int corePoolSize, int maxPoolSize, long keepAliveTime,
        BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        this(corePoolSize, maxPoolSize, keepAliveTime, workQueue, new GridThreadFactory(gridName), handler);
    }

    /**
     * Creates a new service with the given initial parameters.
     *
     * @param corePoolSize The number of threads to keep in the pool, even if they are idle.
     * @param maxPoolSize The maximum number of threads to allow in the pool.
     * @param keepAliveTime When the number of threads is greater than the core, this is the maximum time
     *      that excess idle threads will wait for new tasks before terminating.
     * @param workQueue The queue to use for holding tasks before they are executed. This queue will hold only the
     *      runnable tasks submitted by the {@link #execute(Runnable)} method.
     * @param threadFactory Thread factory.
     * @param handler Optional handler to use when execution is blocked because the thread bounds and queue
     *      capacities are reached. If {@code null} then {@code AbortPolicy}
     *      handler is used by default.
     */
    public GridThreadPoolExecutor(int corePoolSize, int maxPoolSize, long keepAliveTime,
        BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, @Nullable RejectedExecutionHandler handler) {
        super(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue,
            threadFactory, handler == null ? new AbortPolicy() : handler);
    }
}
