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

/**
 * Convenient closure subclass that allows for thrown grid exception. This class
 * implements {@link #apply(Object, Object, Object)} method that calls
 * {@link #applyx(Object, Object, Object)} method and properly wraps {@link GridException}
 * into {@link GridClosureException} instance.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public abstract class GridClosure3X<E1, E2, E3, R> extends GridClosure3<E1, E2, E3, R> {
    /** {@inheritDoc} */
    @Override public R apply(E1 e1, E2 e2, E3 e3) {
        try {
            return applyx(e1, e2, e3);
        }
        catch (GridException e) {
            throw F.wrap(e);
        }
    }

    /**
     * Closure body that can throw {@link GridException}.
     *
     * @param e1 First bound free variable, i.e. the element the closure is called or closed on.
     * @param e2 Second bound free variable, i.e. the element the closure is called or closed on.
     * @param e3 Third bound free variable, i.e. the element the closure is called or closed on.
     * @return Optional return value.
     * @throws GridException Thrown in case of any error condition inside of the closure.
     */
    public abstract R applyx(E1 e1, E2 e2, E3 e3) throws GridException;
}
