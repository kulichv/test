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

/**
 * Defines a convenient absolute, i.e. {@code no-arg} and {@code no return value} closure. This closure
 * that has {@code void} return type and no arguments (free variables).
 * <h2 class="header">Thread Safety</h2>
 * Note that this interface does not impose or assume any specific thread-safety by its
 * implementations. Each implementation can elect what type of thread-safety it provides,
 * if any.
 * <p>
 * Note that this class implements {@link GridJob} interface for convenience and can be
 * used in {@link GridTask} implementations directly, if needed, as an alternative to
 * {@link GridJobAdapterEx}.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 * @see GridFunc
 */
public abstract class GridAbsClosure extends GridLambdaAdapter implements Runnable, GridJob {
    /**
     * Absolute closure body.
     */
    public abstract void apply();

    /**
     * Delegates to {@link #apply()} method.
     * <p>
     * {@inheritDoc}
     */
    @Override public final void run() {
        apply();
    }

    /**
     * Does nothing by default. Child classes may override this method
     * to provide implementation-specific cancellation logic.
     * <p>
     * Note that this method is here only to support {@link GridJob} interface
     * and only makes sense whenever this class is used as grid job or is
     * executed via any of {@link GridProjection} methods.
     * <p>
     * {@inheritDoc}
     */
    @Override public void cancel() {
        // No-op.
    }

    /**
     * Gets closure that ignores its argument and executes the same way as this
     * absolute closure.
     *
     * @param <E> Type of ignore argument.
     * @return Closure that ignores its argument and executes the same way as this
     *      absolute closure.
     */
    public <E> GridInClosure<E> uncurry() {
        GridInClosure<E> c = new CI1<E>() {
            @Override public void apply(E e) {
                GridAbsClosure.this.apply();
            }
        };

        c.peerDeployLike(this);

        return withMeta(c);
    }

    /**
     * Gets closure that ignores its arguments and executes the same way as this
     * absolute closure.
     *
     * @param <E1> Type of 1st ignore argument.
     * @param <E2> Type of 2nd ignore argument.
     * @return Closure that ignores its arguments and executes the same way as this
     *      absolute closure.
     */
    public <E1, E2> GridInClosure2<E1, E2> uncurry2() {
        GridInClosure2<E1, E2> c = new CI2<E1, E2>() {
            @Override public void apply(E1 e1, E2 e2) {
                GridAbsClosure.this.apply();
            }
        };

        c.peerDeployLike(this);

        return withMeta(c);
    }

    /**
     * Gets closure that ignores its arguments and executes the same way as this
     * absolute closure.
     *
     * @param <E1> Type of 1st ignore argument.
     * @param <E2> Type of 2nd ignore argument.
     * @param <E3> Type of 3d ignore argument.
     * @return Closure that ignores its arguments and executes the same way as this
     *      absolute closure.
     */
    public <E1, E2, E3> GridInClosure3<E1, E2, E3> uncurry3() {
        GridInClosure3<E1, E2, E3> c = new CI3<E1, E2, E3>() {
            @Override public void apply(E1 e1, E2 e2, E3 e3) {
                GridAbsClosure.this.apply();
            }
        };

        c.peerDeployLike(this);

        return withMeta(c);
    }

    /**
     * Delegates to {@link #apply()} method.
     * <p>
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @throws GridException {@inheritDoc}
     */
    @Override public final Object execute() throws GridException {
        try {
            apply();
        }
        catch (Throwable e) {
            throw U.cast(e);
        }

        return null;
    }
}
