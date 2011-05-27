// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.cache;

import org.gridgain.grid.lang.utils.*;
import org.gridgain.grid.logger.*;
import org.gridgain.grid.typedef.*;
import org.gridgain.grid.typedef.internal.*;
import org.gridgain.grid.util.future.*;

import java.io.*;
import java.util.*;

/**
 * Future which waits for completion of one or more transactions.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridCacheMultiTxFuture<K, V> extends GridFutureAdapter<Boolean> {
    /** Transactions to wait for. */
    private final Set<GridCacheTxEx<K, V>> txs = new GridLeanSet<GridCacheTxEx<K, V>>();

    /** */
    private Set<GridCacheTxEx<K, V>> remainingTxs;

    /** Logger. */
    private GridLogger log;

    /**
     * @param cctx Cache context.
     */
    public GridCacheMultiTxFuture(GridCacheContext<K, V> cctx) {
        super(cctx.kernalContext());

        log = cctx.logger(getClass());

        // Notify listeners in different threads.
        concurrentNotify(true);
    }

    /**
     * Empty constructor required for {@link Externalizable}.
     */
    public GridCacheMultiTxFuture() {
        // No-op.
    }

    /**
     * @return Transactions to wait for.
     */
    public Set<GridCacheTxEx<K, V>> txs() {
        return txs;
    }

    /**
     * @return Remaining transactions.
     */
    public Set<GridCacheTxEx<K, V>> remainingTxs() {
        return remainingTxs;
    }

    /**
     * @param tx Transaction to add.
     */
    public void addTx(GridCacheTxEx<K, V> tx) {
        txs.add(tx);
    }

    /**
     * Initializes this future.
     */
    public void init() {
        if (F.isEmpty(txs)) {
            remainingTxs = Collections.emptySet();

            onDone(true);
        }
        else {
            remainingTxs = new GridConcurrentHashSet<GridCacheTxEx<K, V>>(txs);

            for (GridCacheTxEx<K, V> tx : txs) {
                if (!tx.done()) {
                    tx.addFinishListener(new CI1<GridCacheTxEx<K,V>>() {
                        @Override public void apply(GridCacheTxEx<K, V> tx) {
                            remainingTxs.remove(tx);

                            checkRemaining();
                        }
                    });
                }
                else
                    remainingTxs.remove(tx);
            }

            checkRemaining();
        }
    }

    /**
     * @return {@code True} if remaining set is empty.
     */
    private boolean checkRemaining() {
        if (remainingTxs.isEmpty()) {
            if (log.isDebugEnabled())
                log.debug("Finishing multi-tx future: " + this);

            onDone(true);

            return true;
        }

        return false;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridCacheMultiTxFuture.class, this,
            "txs", F.viewReadOnly(txs, CU.<K, V>tx2xidVersion()),
            "remaining", F.viewReadOnly(remainingTxs, CU.<K, V>tx2xidVersion()),
            "super", super.toString()
        );
    }
}
