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

import java.util.*;

/**
 * Entry set backed by cache itself.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridCacheEntrySet<K, V> extends AbstractSet<GridCacheEntry<K, V>> {
    /** Cache context. */
    private final GridCacheContext<K, V> ctx;

    /** Filter. */
    private final GridPredicate<? super GridCacheEntry<K, V>>[] filter;

    /** Base set. */
    private final Set<GridCacheEntry<K, V>> set;

    /**
     * @param ctx Cache context.
     * @param c Entry collection.
     * @param filter Filter.
     */
    public GridCacheEntrySet(GridCacheContext<K, V> ctx, Collection<? extends GridCacheEntry<K, V>> c,
        GridPredicate<? super GridCacheEntry<K, V>>[] filter) {
        set = new HashSet<GridCacheEntry<K,V>>(c.size(), 1.0f);

        assert ctx != null;
        assert filter != null;

        this.ctx = ctx;
        this.filter = filter;

        set.addAll(c);
    }

    /** {@inheritDoc} */
    @Override public Iterator<GridCacheEntry<K, V>> iterator() {
        return new GridCacheIterator<K, V, GridCacheEntry<K, V>>(set, F.<GridCacheEntry<K, V>>identity(), filter);
    }

    /** {@inheritDoc} */
    @Override public void clear() {
        ctx.cache().clearAll(F.viewReadOnly(set, F.<K>mapEntry2Key(), filter));

        set.clear();
    }

    /** {@inheritDoc} */
    @SuppressWarnings({"unchecked"})
    @Override public boolean remove(Object o) {
        if (!(o instanceof GridCacheEntryImpl)) {
            return false;
        }

        GridCacheEntry<K, V> e = (GridCacheEntry<K,V>)o;

        if (F.isAll(e, filter) && set.remove(e)) {
            try {
                e.removex();
            }
            catch (GridException ex) {
                throw new GridRuntimeException(ex);
            }

            return true;
        }

        return false;
    }

    /** {@inheritDoc} */
    @Override public int size() {
        return F.size(set, filter);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({"unchecked"})
    @Override public boolean contains(Object o) {
        if (!(o instanceof GridCacheEntryImpl)) {
            return false;
        }

        GridCacheEntry<K,V> e = (GridCacheEntry<K, V>)o;

        return F.isAll(e, filter) && set.contains(e);
    }
}
