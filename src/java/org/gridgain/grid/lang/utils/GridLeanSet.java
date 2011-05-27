// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.lang.utils;

import org.gridgain.grid.typedef.*;
import org.gridgain.grid.typedef.internal.*;

import java.util.*;

/**
 * Lean set implementation. Internally this set is based on {@link GridLeanMap}
 * documentation. See {@link GridLeanMap} for more information.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridLeanSet<E> extends GridSetWrapper<E> implements Cloneable {
    /**
     * Creates a new, empty set with a default initial capacity,
     * load factor, and concurrencyLevel.
     */
    public GridLeanSet() {
        super(new GridLeanMap<E, Object>());
    }

    /**
     * Constructs lean set with initial size.
     *
     * @param size Initial size.
     */
    public GridLeanSet(int size) {
        super(new GridLeanMap<E, Object>(size));
    }

    /**
     * Creates a new set with the same elements as the given collection. The
     * collection is created with a capacity of twice the number of mappings in
     * the given map or 11 (whichever is greater), and a default load factor
     * and concurrencyLevel.
     *
     * @param c Collection to add.
     */
    public GridLeanSet(Collection<E> c) {
        super(new GridLeanMap<E, Object>(F.zip(c, VAL)));
    }

    /** {@inheritDoc} */
    @SuppressWarnings( {"unchecked", "CloneDoesntDeclareCloneNotSupportedException"})
    @Override public Object clone() {
        try {
            GridLeanSet<E> clone = (GridLeanSet<E>)super.clone();

            clone.map = (Map<E, Object>)((GridLeanMap)map).clone();

            return clone;
        }
        catch (CloneNotSupportedException ignore) {
            throw new InternalError();
        }
    }

    /** {@inheritDoc} */
    @Override public String toString() { return S.toString(GridLeanSet.class, this); }
}