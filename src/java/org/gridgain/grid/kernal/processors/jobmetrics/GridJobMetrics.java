// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.jobmetrics;

import org.gridgain.grid.typedef.internal.*;

/**
 * TODO: add file description.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridJobMetrics {
    /** */
    private int maxActiveJobs;

    /** */
    private int curActiveJobs;

    /** */
    private float avgActiveJobs;

    /** */
    private int maxWaitingJobs;

    /** */
    private int curWaitingJobs;

    /** */
    private float avgWaitingJobs;

    /** */
    private int maxCancelledJobs;

    /** */
    private int curCancelledJobs;

    /** */
    private float avgCancelledJobs;

    /** */
    private int maxRejectedJobs;

    /** */
    private int curRejectedJobs;

    /** */
    private float avgRejectedJobs;

    /** */
    private int totalRejectedJobs;

    /** */
    private int totalCancelledJobs;

    /** */
    private int totalExecutedJobs;

    /** */
    private long maxJobWaitTime;

    /** */
    private long curJobWaitTime;

    /** */
    private double avgJobWaitTime;

    /** */
    private long maxJobExecTime;

    /** */
    private long curJobExecTime;

    /** */
    private double avgJobExecTime;

    /** */
    private long totalIdleTime;

    /** */
    private long curIdleTime;

    /** */
    private double cpuLoadAvg;

    /**
     * @return Maximum active jobs.
     */
    public int getMaximumActiveJobs() {
        return maxActiveJobs;
    }

    /**
     * @return Number of jobs currently executing.
     */
    public int getCurrentActiveJobs() {
        return curActiveJobs;
    }

    /**
     * @return Average number of concurrently executing active jobs.
     */
    public float getAverageActiveJobs() {
        return avgActiveJobs;
    }

    /**
     * @return Maximum number of jobs waiting concurrently in the queue.
     */
    public int getMaximumWaitingJobs() {
        return maxWaitingJobs;
    }

    /**
     * @return Current waiting jobs.
     */
    public int getCurrentWaitingJobs() {
        return curWaitingJobs;
    }

    /**
     * @return Average waiting jobs.
     */
    public float getAverageWaitingJobs() {
        return avgWaitingJobs;
    }

    /**
     * @return Maximum number of rejected jobs during a single collision resolution.
     */
    public int getMaximumRejectedJobs() {
        return maxRejectedJobs;
    }

    /**
     * @return Jobs rejected during last collision resolution on this node.
     */
    public int getCurrentRejectedJobs() {
        return curRejectedJobs;
    }

    /**
     * @return Average number of jobs rejected with every collision resolution.
     */
    public float getAverageRejectedJobs() {
        return avgRejectedJobs;
    }

    /**
     * @return Total number of jobs rejected on this node.
     */
    public int getTotalRejectedJobs() {
        return totalRejectedJobs;
    }

    /**
     * @return Maximum canceled jobs.
     */
    public int getMaximumCancelledJobs() {
        return maxCancelledJobs;
    }

    /**
     * @return Current canceled jobs.
     */
    public int getCurrentCancelledJobs() {
        return curCancelledJobs;
    }

    /**
     * @return Average canceled jobs.
     */
    public float getAverageCancelledJobs() {
        return avgCancelledJobs;
    }

    /**
     * @return Total executed jobs.
     */
    public int getTotalExecutedJobs() {
        return totalExecutedJobs;
    }

    /**
     * @return Total canceled jobs.
     */
    public int getTotalCancelledJobs() {
        return totalCancelledJobs;
    }

    /**
     * @return Maximum job wait time.
     */
    public long getMaximumJobWaitTime() {
        return maxJobWaitTime;
    }

    /**
     * @return Current job wait time.
     */
    public long getCurrentJobWaitTime() {
        return curJobWaitTime;
    }

    /**
     * @return Average job wait time.
     */
    public double getAverageJobWaitTime() {
        return avgJobWaitTime;
    }

    /**
     * @return Maximum job execute time.
     */
    public long getMaximumJobExecuteTime() {
        return maxJobExecTime;
    }

    /**
     * Gets execution time of longest job currently running.
     *
     * @return Execution time of longest job currently running.
     */
    public long getCurrentJobExecuteTime() {
        return curJobExecTime;
    }

    /**
     * Gets average job execution time.
     *
     * @return Average job execution time.
     */
    public double getAverageJobExecuteTime() {
        return avgJobExecTime;
    }

    /**
     * Gets total idle time.
     *
     * @return Total idle time.
     */
    public long getTotalIdleTime() {
        return totalIdleTime;
    }

    /**
     * Gets current idle time.
     *
     * @return Current idle time.
     */
    public long getCurrentIdleTime() {
        return curIdleTime;
    }

    /**
     * Gets CPU load average.
     *
     * @return CPU load average.
     */
    public double getAverageCpuLoad() {
        return cpuLoadAvg;
    }

    /**
     * @param maxActiveJobs The maxActiveJobs to set.
     */
    void setMaximumActiveJobs(int maxActiveJobs) {
        this.maxActiveJobs = maxActiveJobs;
    }

    /**
     * @param curActiveJobs The curActiveJobs to set.
     */
    void setCurrentActiveJobs(int curActiveJobs) {
        this.curActiveJobs = curActiveJobs;
    }

    /**
     * @param avgActiveJobs The avgActiveJobs to set.
     */
    void setAverageActiveJobs(float avgActiveJobs) {
        this.avgActiveJobs = avgActiveJobs;
    }

    /**
     * @param maxWaitingJobs The maxWaitingJobs to set.
     */
    void setMaximumWaitingJobs(int maxWaitingJobs) {
        this.maxWaitingJobs = maxWaitingJobs;
    }

    /**
     * @param curWaitingJobs The curWaitingJobs to set.
     */
    void setCurrentWaitingJobs(int curWaitingJobs) {
        this.curWaitingJobs = curWaitingJobs;
    }

    /**
     * @param avgWaitingJobs The avgWaitingJobs to set.
     */
    void setAverageWaitingJobs(float avgWaitingJobs) {
        this.avgWaitingJobs = avgWaitingJobs;
    }

    /**
     * @param maxCancelledJobs The maxCancelledJobs to set.
     */
    void setMaximumCancelledJobs(int maxCancelledJobs) {
        this.maxCancelledJobs = maxCancelledJobs;
    }

    /**
     * @param curCancelledJobs The curCancelledJobs to set.
     */
    void setCurrentCancelledJobs(int curCancelledJobs) {
        this.curCancelledJobs = curCancelledJobs;
    }

    /**
     * @param avgCancelledJobs The avgCancelledJobs to set.
     */
    void setAverageCancelledJobs(float avgCancelledJobs) {
        this.avgCancelledJobs = avgCancelledJobs;
    }

    /**
     * @param maxRejectedJobs The maxRejectedJobs to set.
     */
    void setMaximumRejectedJobs(int maxRejectedJobs) {
        this.maxRejectedJobs = maxRejectedJobs;
    }

    /**
     * @param curRejectedJobs The curRejectedJobs to set.
     */
    void setCurrentRejectedJobs(int curRejectedJobs) {
        this.curRejectedJobs = curRejectedJobs;
    }

    /**
     * @param avgRejectedJobs The avgRejectedJobs to set.
     */
    void setAverageRejectedJobs(float avgRejectedJobs) {
        this.avgRejectedJobs = avgRejectedJobs;
    }

    /**
     * @param totalRejectedJobs The totalRejectedJobs to set.
     */
    void setTotalRejectedJobs(int totalRejectedJobs) {
        this.totalRejectedJobs = totalRejectedJobs;
    }

    /**
     * @param totalCancelledJobs The totalCancelledJobs to set.
     */
    void setTotalCancelledJobs(int totalCancelledJobs) {
        this.totalCancelledJobs = totalCancelledJobs;
    }

    /**
     * @param totalExecutedJobs The totalExecutedJobs to set.
     */
    void setTotalExecutedJobs(int totalExecutedJobs) {
        this.totalExecutedJobs = totalExecutedJobs;
    }

    /**
     * @param maxJobWaitTime The maxJobWaitTime to set.
     */
    void setMaximumJobWaitTime(long maxJobWaitTime) {
        this.maxJobWaitTime = maxJobWaitTime;
    }

    /**
     * @param curJobWaitTime The curJobWaitTime to set.
     */
    void setCurrentJobWaitTime(long curJobWaitTime) {
        this.curJobWaitTime = curJobWaitTime;
    }

    /**
     * @param avgJobWaitTime The avgJobWaitTime to set.
     */
    void setAverageJobWaitTime(double avgJobWaitTime) {
        this.avgJobWaitTime = avgJobWaitTime;
    }

    /**
     * @param maxJobExecTime The maxJobExecTime to set.
     */
    void setMaxJobExecutionTime(long maxJobExecTime) {
        this.maxJobExecTime = maxJobExecTime;
    }

    /**
     * @param curJobExecTime The curJobExecTime to set.
     */
    void setCurrentJobExecutionTime(long curJobExecTime) {
        this.curJobExecTime = curJobExecTime;
    }

    /**
     * @param avgJobExecTime The avgJobExecTime to set.
     */
    void setAverageJobExecutionTime(double avgJobExecTime) {
        this.avgJobExecTime = avgJobExecTime;
    }

    /**
     * @param totalIdleTime The totalIdleTime to set.
     */
    void setTotalIdleTime(long totalIdleTime) {
        this.totalIdleTime = totalIdleTime;
    }

    /**
     * @param curIdleTime The curIdleTime to set.
     */
    void setCurrentIdleTime(long curIdleTime) {
        this.curIdleTime = curIdleTime;
    }

    /**
     * @param cpuLoadAvg CPU load average.
     */
    void setAverageCpuLoad(double cpuLoadAvg) {
        this.cpuLoadAvg = cpuLoadAvg;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridJobMetrics.class, this);
    }
}
