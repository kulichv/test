// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi;

import org.gridgain.grid.logger.*;
import java.util.concurrent.*;

/**
 * This class provides implementation of {@link ThreadFactory}  factory
 * for creating grid SPI threads.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridSpiThreadFactory implements ThreadFactory {
    /** */
    private final GridLogger log;

    /** */
    private final String gridName;

    /** */
    private final String threadName;

    /**
     * @param gridName Grid name, possibly {@code null} for default grid.
     * @param threadName Name for threads created by this factory.
     * @param log Grid logger.
     */
    public GridSpiThreadFactory(String gridName, String threadName, GridLogger log) {
        assert log != null;
        assert threadName != null;

        this.gridName = gridName;
        this.threadName = threadName;
        this.log = log;
    }

    /** {@inheritDoc} */
    @Override public Thread newThread(final Runnable r) {
        return new GridSpiThread(gridName, threadName, log) {
            /** {@inheritDoc} */
            @Override protected void body() throws InterruptedException {
                r.run();
            }
        };
    }
}
