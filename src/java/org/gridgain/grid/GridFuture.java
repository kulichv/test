// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid;

import org.gridgain.grid.lang.*;
import org.jetbrains.annotations.*;
import java.util.concurrent.*;

/**
 * Extension for standard {@link Future} interface. It adds simplified exception handling,
 * functional programming support and ability to listen for future completion via functional
 * callback.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 * @param <R> Type of the result for the future.
 */
public interface GridFuture<R> extends GridMetadataAware, Callable<R> {
    /**
     * Synchronously waits for completion of the computation and
     * returns computation result.
     *
     * @return Computation result.
     * @throws GridInterruptedException Subclass of {@link GridException} thrown if the wait was interrupted.
     * @throws GridFutureCancelledException Subclass of {@link GridException} throws if computation was cancelled.
     * @throws GridException If computation failed.
     */
    public R get() throws GridException;

    /**
     * Synchronously waits for completion of the computation for
     * up to the timeout specified and returns computation result.
     * This method is equivalent to calling {@link #get(long, TimeUnit) get(long, TimeUnit.MILLISECONDS)}.
     *
     * @param timeout The maximum time to wait in milliseconds.
     * @return Computation result.
     * @throws GridInterruptedException Subclass of {@link GridException} thrown if the wait was interrupted.
     * @throws GridFutureTimeoutException Subclass of {@link GridException} thrown if the wait was timed out.
     * @throws GridFutureCancelledException Subclass of {@link GridException} throws if computation was cancelled.
     * @throws GridException If computation failed.
     */
    public R get(long timeout) throws GridException;

    /**
     * Synchronously waits for completion of the computation for
     * up to the timeout specified and returns computation result.
     *
     * @param timeout The maximum time to wait.
     * @param unit The time unit of the {@code timeout} argument.
     * @return Computation result.
     * @throws GridInterruptedException Subclass of {@link GridException} thrown if the wait was interrupted.
     * @throws GridFutureTimeoutException Subclass of {@link GridException} thrown if the wait was timed out.
     * @throws GridFutureCancelledException Subclass of {@link GridException} throws if computation was cancelled.
     * @throws GridException If computation failed.
     */
    public R get(long timeout, TimeUnit unit) throws GridException;

    /**
     * Cancels this future.
     *
     * @return {@code True} if future was canceled (i.e. was not finished prior to this call).
     * @throws GridException If cancellation failed.
     */
    public boolean cancel() throws GridException;

    /**
     * Checks if computation is done.
     *
     * @return {@code True} if computation is done, {@code false} otherwise.
     */
    public boolean isDone();

    /**
     * Gets predicate that evaluates to {@code true} if this future is done
     * at the moment of evaluation.
     *
     * @return Predicate that evaluates to {@code true} if this future is done
     *      at the moment of  evaluation.
     */
    public GridAbsPredicate predicate();

    /**
     * Returns {@code true} if this computation was cancelled before it completed normally.
     *
     * @return {@code True} if this computation was cancelled before it completed normally.
     */
    public boolean isCancelled();

    /**
     * Gets start time for this future.
     *
     * @return Start time for this future.
     */
    public long startTime();

    /**
     * Gets duration in milliseconds between start of the future and current time if future
     * is not finished, or between start and finish of this future.
     *
     * @return Time in milliseconds this future has taken to execute.
     */
    public long duration();

    /**
     * Flag to turn on or off synchronous listener notification. If this flag is {@code true}, then
     * upon future completion the notification may happen in the same thread that created
     * the future. This becomes especially important when adding listener to a future that
     * is already {@code done} - if this flag is {@code true}, then listener will be
     * immediately notified within the same thread.
     * <p>
     * Default value is {@code false}. To change the default, set
     * {@link GridSystemProperties#GG_FUT_SYNC_NOTIFICATION} system property to {@code true}.
     *
     * @param syncNotify Flag to turn on or off synchronous listener notification.
     */
    public void syncNotify(boolean syncNotify);

    /**
     * Gets value of synchronous listener notification flag. If this flag is {@code true}, then
     * upon future completion the notification may happen in the same thread that created
     * the future. This becomes especially important when adding listener to a future that
     * is already {@code done} - if this flag is {@code true}, then listener will be
     * immediately notified within the same thread.
     * <p>
     * Default value is {@code false}. To change the default, set
     * {@link GridSystemProperties#GG_FUT_SYNC_NOTIFICATION} system property to {@code true}.
     *
     * @return Synchronous listener notification flag.
     */
    public boolean syncNotify();

    /**
     * Flag to turn on or off concurrent listener notification. This flag comes into play only
     * when a future has more than one listener subscribed to it. If this flag is {@code true},
     * then all listeners will be notified concurrently by different threads; otherwise,
     * listeners will be notified one after another within one thread (depending on
     * {@link #syncNotify()} flag, these notifications may happen either in the same thread which
     * started the future, or in a different thread).
     * <p>
     * Default value is {@code false}. To change the default, set
     * {@link GridSystemProperties#GG_FUT_CONCURRENT_NOTIFICATION} system property to {@code true}.
     *
     * @param concurNotify Flag to turn on or off concurrent listener notification.
     */
    public void concurrentNotify(boolean concurNotify);

    /**
     * Gets value concurrent listener notification flag. This flag comes into play only
     * when a future has more than one listener subscribed to it. If this flag is {@code true},
     * then all listeners will be notified concurrently by different threads; otherwise,
     * listeners will be notified one after another within one thread (depending on
     * {@link #syncNotify()} flag, these notifications may happen either in the same thread which
     * started the future, or in a different thread).
     * <p>
     * Default value is {@code false}. To change the default, set
     * {@link GridSystemProperties#GG_FUT_CONCURRENT_NOTIFICATION} system property to {@code true}.
     *
     * @return Concurrent listener notification flag
     */
    public boolean concurrentNotify();

    /**
     * Registers listener predicate to be asynchronously notified whenever future completes.
     * If predicates returns {@code false} if will be automatically unregistered. Note that
     * for future implementations that call the listener only once this behavior is
     * irrelevant. If future implementation changes the contract and calls this listener
     * more than once - returning {@code false} allows for finer management of listener life-cycle.
     *
     * @param lsnr Listener predicate to register. If not provided - this method is no-op.
     */
    public void listenAsync(@Nullable GridInClosure<? super GridFuture<R>> lsnr);

    /**
     * Removes given listeners from the future. If no listener is passed in, then all listeners
     * will be removed.
     *
     * @param lsnr Listeners to remove.
     */
    public void stopListenAsync(@Nullable GridInClosure<? super GridFuture<R>>... lsnr);
}