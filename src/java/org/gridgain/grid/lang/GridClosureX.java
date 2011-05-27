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
 * implements {@link #apply(Object)} method that calls {@link #applyx(Object)} method
 * and properly wraps {@link GridException} into {@link GridClosureException} instance.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public abstract class GridClosureX<E, R> extends GridClosure<E, R> {
    /** {@inheritDoc} */
    @Override public R apply(E e) {
        try {
            return applyx(e);
        }
        catch (GridException ex) {
            throw F.wrap(ex);
        }
    }

    /**
     * Closure body that can throw {@link GridException}.
     *
     * @param e The variable the closure is called or closed on.
     * @return Optional return value.
     * @throws GridException Thrown in case of any error condition inside of the closure.
     */
    public abstract R applyx(E e) throws GridException;
}
