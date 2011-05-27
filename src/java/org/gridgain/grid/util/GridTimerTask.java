// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.util;

import java.util.*;

/**
 * Utility extension for {@link TimerTask}.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public abstract class GridTimerTask extends TimerTask {
    /**
     * 
     * @throws InterruptedException Thrown in case other thread interrupted this thread.
     */
    protected abstract void safeRun() throws InterruptedException;

    /** {@inheritDoc} */
    @Override public void run() {
        try {
            safeRun();
        }
        catch (InterruptedException ignored) {
            // No-op.
        }
    }
}
