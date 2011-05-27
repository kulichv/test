// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.lang;

import org.gridgain.grid.*;
import org.gridgain.grid.typedef.internal.*;
import java.util.*;

/**
 * Convenient adapter for "rich" iterable interface.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridIterableAdapter<T> implements GridIterable<T> {
    private GridIterator<T> impl;

    /**
     * Creates adapter with given iterator implementation.
     * 
     * @param impl Iterator implementation.
     */
    public GridIterableAdapter(Iterator<T> impl) {
        A.notNull(impl, "impl");

        this.impl = impl instanceof GridIterator ? (GridIterator<T>)impl : new IteratorWrapper<T>(impl);
    }

    /** {@inheritDoc} */
    @Override public GridIterator<T> iterator() {
        return impl;
    }

    /** {@inheritDoc} */
    @Override public boolean hasNext() {
        return impl.hasNext();
    }

    /** {@inheritDoc} */
    @Override public boolean hasNextX() throws GridException {
        return hasNext();
    }

    /** {@inheritDoc} */
    @Override public T next() {
        return impl.next();
    }

    /** {@inheritDoc} */
    @Override public T nextX() throws GridException {
        return next();
    }

    /** {@inheritDoc} */
    @Override public void remove() {
        impl.remove();
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridIterableAdapter.class, this);
    }

    /**
     *
     */
    private static class IteratorWrapper<T> extends GridIteratorAdapter<T> {
        /** Base iterator. */
        private Iterator<T> it;

        /**
         * Creates wrapper around the iterator.
         *
         * @param it Iterator to wrap.
         */
        IteratorWrapper(Iterator<T> it) {
            this.it = it;
        }

        /** {@inheritDoc} */
        @Override public boolean hasNext() {
            return it.hasNext();
        }

        /** {@inheritDoc} */
        @Override public T next() {
            return it.next();
        }

        /** {@inheritDoc} */
        @Override public void remove() {
            it.remove();
        }
    }
}
