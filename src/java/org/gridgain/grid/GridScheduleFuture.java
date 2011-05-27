// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid;

import java.util.concurrent.*;

/**
 * Future for cron-based scheduled execution. Any arbitrary work can be scheduled to
 * run on the grid or on the local node via any of the {@code 'Grid.scheduleLocal(...)}
 * methods.
 * <p>
 * In addition to regular scheduling pattern in UNIX cron format with optional prefix <tt>{n1, n2}</tt>
 * where {@code n1} is delay of scheduling in seconds and {@code n2} is the number of executions. Both
 * parameters are optional.
 * <p>
 * Here's an example of scheduling a closure that broadcasts a message
 * to all nodes five times, once every minute, with initial delay in two seconds:
 * <pre name="code" class="java">
 * g.schedule(
 *     new C1() {
 *         &#64;Override public void apply() {
 *             try {
 *                 g.call(BROADCAST, F.println("Hello Node! :)");
 *             }
 *             catch (GridException e) {
 *                 throw new GridClosureException(e);
 *             }
 *         }
 *     }, "{2, 5} * * * * *" // 2 seconds delay with 5 executions only.
 * );
 * </pre>
 * <p>
 * Refer to {@link Grid#scheduleLocal(Callable, String)} and {@link Grid#scheduleLocal(Runnable, String)}
 * methods for more information.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 * @see Grid#scheduleLocal(Runnable, String)
 * @see Grid#scheduleLocal(Callable, String) 
 */
public interface GridScheduleFuture<R> extends GridFuture<R> {
    /**
     * Gets scheduled task ID.
     *
     * @return ID of scheduled task.
     */
    public String id();

    /**
     * Gets scheduling pattern.
     *
     * @return Scheduling pattern.
     */
    public String pattern();

    /**
     * Gets time in milliseconds at which this future was created.
     *
     * @return Time in milliseconds at which this future was created.
     */
    public long createTime();

    /**
     * Gets start time of last execution ({@code 0} if never started).
     *
     * @return Start time of last execution.
     */
    public long lastStartTime();

    /**
     * Gets finish time of last execution ({@code 0} if first execution has not finished).
     *
     * @return Finish time of last execution ({@code 0} if first execution has not finished).
     */
    public long lastFinishTime();

    /**
     * Gets average execution time in milliseconds since future was created.
     *
     * @return Average execution time in milliseconds since future was created.
     */
    public double averageExecutionTime();

    /**
     * Gets last interval between scheduled executions. If first execution has
     * not yet happened, then returns time passed since creation of this future.
     *
     * @return Last interval between scheduled executions.  
     */
    public long lastIdleTime();

    /**
     * Gets average idle time for this scheduled task.
     *
     * @return Average idle time for this scheduled task.
     */
    public double averageIdleTime();

    /**
     * Gets an array of the next execution times after passed {@code start} timestamp.
     *
     * @param cnt Array length.
     * @param start Start timestamp.
     * @return Array of the next execution times in milliseconds.
     * @throws GridException Thrown in case of any errors.
     */
    public long[] nextExecutionTimes(int cnt, long start) throws GridException;

    /**
     * Gets total count of executions this task has already completed.
     *
     * @return Total count of executions this task has already completed.
     */
    public int count();

    /**
     * Returns {@code true} if scheduled task is currently running.
     *  
     * @return {@code True} if scheduled task is currently running.
     */
    public boolean isRunning();

    /**
     * Gets next execution time of scheduled task.
     *
     * @return Next execution time in milliseconds.
     * @throws GridException Thrown in case of any errors.
     */
    public long nextExecutionTime() throws GridException;

    /**
     * Gets result of the last execution of scheduled task, or
     * {@code null} if task has not been executed, or has not
     * produced a result yet.
     *
     * @return Result of the last execution, or {@code null} if
     *      there isn't one yet.
     * @throws GridException If last execution resulted in exception.
     */
    public R last() throws GridException;

    /**
     * Waits for the completion of the next scheduled execution and returns its result.
     *
     * @return Result of the next execution.
     * @throws CancellationException {@inheritDoc}
     * @throws GridInterruptedException {@inheritDoc}
     * @throws GridException {@inheritDoc}
     */
    @Override public R get() throws GridException;

    /**
     * Waits for the completion of the next scheduled execution for
     * specified amount of time and returns its result. This method
     * is equivalent to {@link #get(long, TimeUnit) get(long, TimeUnit.MILLISECONDS)}.
     *
     * @param timeout {@inheritDoc}
     * @return The computed result of the next execution.
     * @throws CancellationException {@inheritDoc}
     * @throws GridInterruptedException {@inheritDoc}
     * @throws GridFutureTimeoutException {@inheritDoc}
     * @throws GridException {@inheritDoc}
     */
    @Override public R get(long timeout) throws GridException;

    /**
     * Waits for the completion of the next scheduled execution for
     * specified amount of time and returns its result.
     *
     * @param timeout {@inheritDoc}
     * @param unit {@inheritDoc}
     * @return The computed result of the next execution.
     * @throws CancellationException {@inheritDoc}
     * @throws GridInterruptedException {@inheritDoc}
     * @throws GridFutureTimeoutException {@inheritDoc}
     * @throws GridException {@inheritDoc}
     */
    @Override public R get(long timeout, TimeUnit unit) throws GridException;
}