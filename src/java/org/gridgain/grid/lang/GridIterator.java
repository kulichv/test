// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.lang;

import org.gridgain.grid.*;
import org.gridgain.grid.lang.utils.*;

import java.util.*;

/**
 * Defines "rich" iterator interface that is also acts like lambda function and iterable.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 * @see GridIterable
 */
public interface GridIterator<T> extends Iterable<T>, GridSerializableIterator<T> {
    /**
     * This method is the same as {@link #hasNext()}, but allows for failure
     * with exception. Often iterators are used to iterate through values
     * that have not or have partially been received from remote nodes,
     * and need to account for possible network failures, rather than
     * just returning {@code false} out of {@link #hasNext()} method.
     *
     * @return {@code True} if iterator contains more elements.
     * @throws GridException If no more elements can be returned due
     *      to some failure, like a network error for example.
     * @see Iterator#hasNext() 
     */
    public boolean hasNextX() throws GridException;

    /**
     * This method is the same as {@link #next()}, but allows for failure
     * with exception. Often iterators are used to iterate through values
     * that have not or have partially been received from remote nodes,
     * and need to account for possible network failures, rather than
     * throwing {@link NoSuchElementException} runtime exception.s
     *
     * @return {@code True} if iterator contains more elements.
     * @throws NoSuchElementException If there are no more elements to
     *      return.
     * @throws GridException If no more elements can be returned due
     *      to some failure, like a network error for example.
     * @see Iterator#next() 
     */
    public T nextX() throws GridException;
}
