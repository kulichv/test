// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.cache;

import org.gridgain.grid.*;
import org.gridgain.grid.cache.*;
import org.gridgain.grid.lang.*;
import org.gridgain.grid.typedef.*;
import org.gridgain.grid.lang.utils.*;

import java.util.*;

/**
 * Cache-backed iterator.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridCacheIterator<K, V, T> implements GridSerializableIterator<T> {
    /** Base iterator. */
    private final Iterator<? extends GridCacheEntry<K, V>> it;

    /** Transformer. */
    private final GridClosure<? super GridCacheEntry<K, V>, T> trans;

    /** Current element. */
    private GridCacheEntry<K, V> cur;

    /**
     * @param c Cache entry collection.
     * @param trans Transformer.
     * @param filter Filter.
     */
    public GridCacheIterator(Collection<? extends GridCacheEntry<K, V>> c,
        GridClosure<? super GridCacheEntry<K, V>, T> trans,
        GridPredicate<? super GridCacheEntry<K, V>>[] filter) {
        it = F.iterator0(c, false, filter);

        this.trans = trans;
    }

    /** {@inheritDoc} */
    @Override public boolean hasNext() {
        if (!it.hasNext()) {
            cur = null;

            return false;
        }

        return true;
    }

    /** {@inheritDoc} */
    @Override public T next() {
        return trans.apply(cur = it.next());
    }

    /** {@inheritDoc} */
    @Override public void remove() {
        it.remove();

        try {
            // Back remove operation by actual cache.
            cur.removex();
        }
        catch (GridException e) {
            throw new GridClosureException(e);
        }
    }
}
