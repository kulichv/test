// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.cache;

import org.jetbrains.annotations.*;

/**
 * Cache transaction state.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public enum GridCacheTxState {
    /** Transaction started. */
    ACTIVE,

    /** Transaction validating. */
    PREPARING,

    /** Transaction validation succeeded. */
    PREPARED,

    /** Transaction is marked for rollback. */
    MARKED_ROLLBACK,

    /** Transaction commit started (validating finished). */
    COMMITTING,

    /** Transaction commit succeeded. */
    COMMITTED,

    /** Transaction rollback started (validation failed). */
    ROLLING_BACK,

    /** Transaction rollback succeeded. */
    ROLLED_BACK,

    /** Transaction rollback failed or is otherwise unknown state. */
    UNKNOWN;

    /** Enumerated values. */
    private static final GridCacheTxState[] VALS = values();

    /**
     * Efficiently gets enumerated value from its ordinal.
     *
     * @param ord Ordinal value.
     * @return Enumerated value or {@code null} if ordinal out of range.
     */
    @Nullable public static GridCacheTxState fromOrdinal(int ord) {
        return ord >= 0 && ord < VALS.length ? VALS[ord] : null;
    }
}
