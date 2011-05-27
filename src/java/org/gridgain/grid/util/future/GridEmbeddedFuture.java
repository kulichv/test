// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.util.future;

import org.gridgain.grid.*;
import org.gridgain.grid.kernal.*;
import org.gridgain.grid.lang.*;
import org.gridgain.grid.typedef.internal.*;

import java.io.*;

/**
 * Future which waits for embedded future to complete and then asynchronously executes
 * provided closure with embedded future result.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridEmbeddedFuture<A, B> extends GridFutureAdapter<A> {
    /** Embedded future to wait for. */
    private GridFuture<B> embedded;

    /**
     * Empty constructor required by {@link Externalizable}.
     */
    public GridEmbeddedFuture() {
        // No-op.
    }

    /**
     * @param ctx Context.
     * @param embedded Embedded future.
     * @param c Closure to execute upon completion of embedded future.
     */
    public GridEmbeddedFuture(GridKernalContext ctx, GridFuture<B> embedded, final GridClosure2<B, Exception, A> c) {
        super(ctx);

        assert embedded != null;
        assert c != null;

        this.embedded = embedded;

        embedded.listenAsync(new AL1() {
            @SuppressWarnings({"ErrorNotRethrown", "CatchGenericClass"})
            @Override public void applyx(GridFuture<B> embedded) {
                try {
                    onDone(c.apply(embedded.get(), null));
                }
                catch (GridClosureException e) {
                    onDone(c.apply(null, e));
                }
                catch (GridException e) {
                    onDone(c.apply(null, e));
                }
                catch (RuntimeException e) {
                    onDone(c.apply(null, e));
                }
            }
        });
    }

    /**
     * Embeds futures. Specific change order of arguments to avoid conflicts.
     *
     * @param ctx Context.
     * @param embedded Closure.
     * @param c Closure which runs upon completion of embedded closure and which returns another future.
     */
    public GridEmbeddedFuture(GridFuture<B> embedded, final GridClosure2<B, Exception, GridFuture<A>> c,
        GridKernalContext ctx) {
        super(ctx);

        assert embedded != null;
        assert c != null;

        this.embedded = embedded;

        embedded.listenAsync(new AL1() {
            @Override public void applyx(GridFuture<B> embedded) {
                try {
                    GridFuture<A> next = c.apply(embedded.get(), null);

                    if (next == null)
                        onDone();

                    next.listenAsync(new AL2() {
                        @Override public void applyx(GridFuture<A> next) {
                            try {
                                onDone(next.get());
                            }
                            catch (GridClosureException e) {
                                onDone(e.unwrap());
                            }
                            catch (GridException e) {
                                onDone(e);
                            }
                            catch (RuntimeException e) {
                                onDone(e);
                            }
                        }
                    });
                }
                catch (GridClosureException e) {
                    c.apply(null, e);

                    onDone(e.unwrap());
                }
                catch (GridException e) {
                    c.apply(null, e);

                    onDone(e);
                }
                catch (RuntimeException e) {
                    c.apply(null, e);

                    onDone(e);
                }
            }
        });
    }

    /**
     * Embeds futures.
     *
     * @param ctx Context.
     * @param embedded Future.
     * @param c1 Closure which runs upon completion of embedded future and which returns another future.
     * @param c2 Closure will runs upon completion of future returned by {@code c1} closure.
     */
    public GridEmbeddedFuture(GridKernalContext ctx, GridFuture<B> embedded, final GridClosure2<B, Exception,
        GridFuture<A>> c1, final GridClosure2<A, Exception, A> c2) {
        super(ctx);

        assert embedded != null;
        assert c1 != null;
        assert c2 != null;

        this.embedded = embedded;

        embedded.listenAsync(new AL1() {
            @Override public void applyx(GridFuture<B> embedded) {
                try {
                    GridFuture<A> next = c1.apply(embedded.get(), null);

                    if (next == null)
                        onDone();

                    next.listenAsync(new AL2() {
                        @Override public void applyx(GridFuture<A> next) {
                            try {
                                onDone(c2.apply(next.get(), null));
                            }
                            catch (GridClosureException e) {
                                c2.apply(null, e);

                                onDone(e.unwrap());
                            }
                            catch (GridException e) {
                                c2.apply(null, e);

                                onDone(e);
                            }
                            catch (RuntimeException e) {
                                c2.apply(null, e);

                                onDone(e);
                            }
                        }
                    });
                }
                catch (GridClosureException e) {
                    c1.apply(null, e);

                    onDone(e.unwrap());
                }
                catch (GridException e) {
                    c1.apply(null, e);

                    onDone(e);
                }
                catch (RuntimeException e) {
                    c1.apply(null, e);

                    onDone(e);
                }
            }
        });
    }

    /**
     * @param ctx Context.
     * @param embedded Embedded future.
     * @param c Closure to execute upon completion of embedded future.
     * @param arg Argument into closure (often default return value).
     */
    public GridEmbeddedFuture(GridKernalContext ctx, GridFuture<B> embedded, final GridClosure3<B, A, Exception, A> c,
        final A arg) {
        super(ctx);

        assert embedded != null;
        assert c != null;

        this.embedded = embedded;

        embedded.listenAsync(new AL1() {
            @SuppressWarnings({"ErrorNotRethrown", "CatchGenericClass"})
            @Override public void applyx(GridFuture<B> fut) {
                try {
                    onDone(c.apply(fut.get(), arg, null));
                }
                catch (GridClosureException e) {
                    onDone(c.apply(null, arg, e));
                }
                catch (GridException e) {
                    onDone(c.apply(null, arg, e));
                }
                catch (RuntimeException e) {
                    onDone(c.apply(null, arg, e));
                }
            }
        });
    }

    /** {@inheritDoc} */
    @Override public boolean cancel() throws GridException {
        return embedded.cancel();
    }

    /** {@inheritDoc} */
    @Override public boolean isCancelled() {
        return embedded.isCancelled();
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridEmbeddedFuture.class, this);
    }

    /** Typedef. */
    private abstract class AL1 extends AsyncListener1 {
    }

    /** Typedef. */
    private abstract class AL2 extends AsyncListener2 {
    }

    /**
     * Make sure that listener does not throw exceptions.
     */
    private abstract class AsyncListener1 extends GridInClosure<GridFuture<B>> {
        @Override public final void apply(GridFuture<B> f) {
            try {
                applyx(f);
            }
            catch (Exception e) {
                onDone(e);
            }
        }

        protected abstract void applyx(GridFuture<B> f) throws Exception;
    }

    /**
     * Make sure that listener does not throw exceptions.
     */
    private abstract class AsyncListener2 extends GridInClosure<GridFuture<A>> {
        @Override public final void apply(GridFuture<A> f) {
            try {
                applyx(f);
            }
            catch (Exception e) {
                onDone(e);
            }
        }

        protected abstract void applyx(GridFuture<A> f) throws Exception;
    }
}
