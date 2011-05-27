// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.schedule;

import org.gridgain.grid.typedef.internal.*;

/**
 * Scheduled task statistics.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
class GridScheduleStatistics {
    /** Create time. */
    private final long createTime = System.currentTimeMillis();

    /** Last idle time. */
    private long lastIdleTime;

    /** Total idle time. */
    private long totalIdleTime;

    /** Last execution time. */
    private long lastExecTime;

    /** Total execution time. */
    private long totalExecTime;

    /** Last start time. */
    private long lastStartTime;

    /** Last end time. */
    private long lastEndTime;

    /** Execution count. */
    private int execCnt;

    /** Running flag. */
    private boolean running;

    /**
     * @return Create time.
     */
    long getCreateTime() {
        return createTime;
    }

    /**
     * @return Last idle time.
     */
    long getLastIdleTime() {
        return lastIdleTime == 0 ? System.currentTimeMillis() - createTime : lastIdleTime;
    }

    /**
     * @return Total idle time.
     */
    long getTotalIdleTime() {
        long now = System.currentTimeMillis();

        if (totalIdleTime == 0) {
            return now - createTime;
        }

        if (running) {
            return totalIdleTime;
        }

        return totalIdleTime + (now - lastEndTime);
    }

    /**
     * @return Average idle time.
     */
    double getAverageIdleTime() {
        long now = System.currentTimeMillis();

        // If first execution has not started.
        if (totalIdleTime == 0) {
            return now - createTime;
        }

        return execCnt == 0 ? totalIdleTime : (double)totalIdleTime / execCnt;
    }

    /**
     * @return Last execution time.
     */
    long getLastExecutionTime() {
        return lastExecTime;
    }

    /**
     * @return Total execution time.
     */
    long getTotalExecutionTime() {
        return totalExecTime;
    }

    /**
     * @return Average execution time.
     */
    double getAverageExecutionTime() {
        return execCnt == 0 ? 0 : (double)totalExecTime / execCnt;
    }

    /**
     * @return Last start time.
     */
    long getLastStartTime() {
        return lastStartTime;
    }

    /**
     * @return Last end time.
     */
    long getLastEndTime() {
        return lastEndTime;
    }

    /**
     * @return Execution count.
     */
    int getExecutionCount() {
        return execCnt;
    }

    /**
     * @return {@code True} if currently running.
     */
    boolean isRunning() {
        return running;
    }

    /**
     * Start callback.
     */
    void onStart() {
        long now = System.currentTimeMillis();

        lastStartTime = now;

        long lastEndTime = this.lastEndTime == 0 ? createTime : this.lastEndTime;

        lastIdleTime = now - lastEndTime;

        totalIdleTime += lastIdleTime;

        running = true;
    }

    /**
     * End callback.
     */
    void onEnd() {
        long now = System.currentTimeMillis();

        lastEndTime = now;

        lastExecTime = now - lastStartTime;

        totalExecTime += lastExecTime;

        execCnt++;

        running = false;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridScheduleStatistics.class, this);
    }
}
