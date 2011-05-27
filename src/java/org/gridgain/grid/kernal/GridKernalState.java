// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal;

import org.jetbrains.annotations.*;

/**
 * Kernal life cycle states.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
enum GridKernalState {
    /** Kernal is started. */
    STARTED,

    /** Kernal is starting.*/
    STARTING,

    /** Kernal is stopping. */
    STOPPING,

    /** Kernal is stopped.
     * <p>
     * This is also the initial state of the kernal.
     */
    STOPPED;

    /** Enum values. */
    private static final GridKernalState[] VALS = values();

    /**
     * @param ord Byte to convert to enum.
     * @return Enum.
     */
    @Nullable public static GridKernalState fromOrdinal(int ord) {
        return ord >= 0 && ord < VALS.length ? VALS[ord] : null;
    }
}
