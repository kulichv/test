// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.discovery;

import org.gridgain.grid.*;
import org.gridgain.grid.typedef.internal.*;

/**
 * Helper class to serialize and deserialize node metrics.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public final class GridDiscoveryMetricsHelper {
    /** Size of serialized node metrics. */
    public static final int METRICS_SIZE =
        4/*max active jobs*/ +
        4/*current active jobs*/ +
        4/*average active jobs*/ +
        4/*max waiting jobs*/ +
        4/*current waiting jobs*/ +
        4/*average waiting jobs*/ +
        4/*max cancelled jobs*/ +
        4/*current cancelled jobs*/ +
        4/*average cancelled jobs*/ +
        4/*max rejected jobs*/ +
        4/*current rejected jobs*/ +
        4/*average rejected jobs*/ +
        4/*total executed jobs*/ +
        4/*total rejected jobs*/ +
        4/*total cancelled jobs*/ +
        8/*max job wait time*/ +
        8/*current job wait time*/ +
        8/*average job wait time*/ +
        8/*max job execute time*/ +
        8/*current job execute time*/ +
        8/*average job execute time*/ +
        8/*current idle time*/ +
        8/*total idle time*/ +
        4/*available processors*/ +
        8/*current CPU load*/ +
        8/*average CPU load*/ +
        8/*heap memory init*/ +
        8/*heap memory used*/ +
        8/*heap memory committed*/ +
        8/*heap memory max*/ +
        8/*non-heap memory init*/ +
        8/*non-heap memory used*/ +
        8/*non-heap memory committed*/ +
        8/*non-heap memory max*/ +
        8/*uptime*/ +
        8/*start time*/ +
        8/*node start time*/ +
        4/*thread count*/ +
        4/*peak thread count*/ +
        8/*total started thread count*/ +
        4/*daemon thread count*/ +
        8/*file system free space*/ +
        8/*file system total space*/ +
        8/*file system usable space*/ +
        8/*last data version.*/;

    /**
     * Enforces singleton.
     */
    private GridDiscoveryMetricsHelper() {
        // No-op.
    }

    /**
     * Serializes node metrics into byte array.
     *
     * @param data Byte array.
     * @param off Offset into byte array.
     * @param metrics Node metrics to serialize.
     * @return New offset.
     */
    public static int serialize(byte[] data, int off, GridNodeMetrics metrics) {
        int start = off;

        off = U.intToBytes(metrics.getMaximumActiveJobs(), data, off);
        off = U.intToBytes(metrics.getCurrentActiveJobs(), data, off);
        off = U.floatToBytes(metrics.getAverageActiveJobs(), data, off);
        off = U.intToBytes(metrics.getMaximumWaitingJobs(), data, off);
        off = U.intToBytes(metrics.getCurrentWaitingJobs(), data, off);
        off = U.floatToBytes(metrics.getAverageWaitingJobs(), data, off);
        off = U.intToBytes(metrics.getMaximumRejectedJobs(), data, off);
        off = U.intToBytes(metrics.getCurrentRejectedJobs(), data, off);
        off = U.floatToBytes(metrics.getAverageRejectedJobs(), data, off);
        off = U.intToBytes(metrics.getMaximumCancelledJobs(), data, off);
        off = U.intToBytes(metrics.getCurrentCancelledJobs(), data, off);
        off = U.floatToBytes(metrics.getAverageCancelledJobs(), data, off);
        off = U.intToBytes(metrics.getTotalRejectedJobs(), data , off);
        off = U.intToBytes(metrics.getTotalCancelledJobs(), data , off);
        off = U.intToBytes(metrics.getTotalExecutedJobs(), data , off);
        off = U.longToBytes(metrics.getMaximumJobWaitTime(), data, off);
        off = U.longToBytes(metrics.getCurrentJobWaitTime(), data, off);
        off = U.doubleToBytes(metrics.getAverageJobWaitTime(), data, off);
        off = U.longToBytes(metrics.getMaximumJobExecuteTime(), data, off);
        off = U.longToBytes(metrics.getCurrentJobExecuteTime(), data, off);
        off = U.doubleToBytes(metrics.getAverageJobExecuteTime(), data, off);
        off = U.longToBytes(metrics.getCurrentIdleTime(), data, off);
        off = U.longToBytes(metrics.getTotalIdleTime(), data , off);
        off = U.intToBytes(metrics.getTotalCpus(), data, off);
        off = U.doubleToBytes(metrics.getCurrentCpuLoad(), data, off);
        off = U.doubleToBytes(metrics.getAverageCpuLoad(), data, off);
        off = U.longToBytes(metrics.getHeapMemoryInitialized(), data, off);
        off = U.longToBytes(metrics.getHeapMemoryUsed(), data, off);
        off = U.longToBytes(metrics.getHeapMemoryCommitted(), data, off);
        off = U.longToBytes(metrics.getHeapMemoryMaximum(), data, off);
        off = U.longToBytes(metrics.getNonHeapMemoryInitialized(), data, off);
        off = U.longToBytes(metrics.getNonHeapMemoryUsed(), data, off);
        off = U.longToBytes(metrics.getNonHeapMemoryCommitted(), data, off);
        off = U.longToBytes(metrics.getNonHeapMemoryMaximum(), data, off);
        off = U.longToBytes(metrics.getStartTime(), data, off);
        off = U.longToBytes(metrics.getNodeStartTime(), data, off);
        off = U.longToBytes(metrics.getUpTime(), data, off);
        off = U.intToBytes(metrics.getCurrentThreadCount(), data, off);
        off = U.intToBytes(metrics.getMaximumThreadCount(), data, off);
        off = U.longToBytes(metrics.getTotalStartedThreadCount(), data, off);
        off = U.intToBytes(metrics.getCurrentDaemonThreadCount(), data, off);
        off = U.longToBytes(metrics.getFileSystemFreeSpace(), data, off);
        off = U.longToBytes(metrics.getFileSystemTotalSpace(), data, off);
        off = U.longToBytes(metrics.getFileSystemUsableSpace(), data, off);
        off = U.longToBytes(metrics.getLastDataVersion(), data, off);

        assert off - start == METRICS_SIZE : "Invalid metrics size [expected=" + METRICS_SIZE + ", actual=" +
            (off - start) + ']';

        return off;
    }

    /**
     * De-serializes node metrics.
     *
     * @param data Byte array.
     * @param off Offset into byte array.
     * @return Deserialized node metrics.
     */
    public static GridNodeMetrics deserialize(byte[] data, int off) {
        int start = off;

        GridDiscoveryMetricsAdapter metrics = new GridDiscoveryMetricsAdapter();

        metrics.setLastUpdateTime(System.currentTimeMillis());

        metrics.setMaximumActiveJobs(U.bytesToInt(data, off));

        off += 4;

        metrics.setCurrentActiveJobs(U.bytesToInt(data, off));

        off += 4;

        metrics.setAverageActiveJobs(U.bytesToFloat(data, off));

        off += 4;

        metrics.setMaximumWaitingJobs(U.bytesToInt(data, off));

        off += 4;

        metrics.setCurrentWaitingJobs(U.bytesToInt(data, off));

        off += 4;

        metrics.setAverageWaitingJobs(U.bytesToFloat(data, off));

        off += 4;

        metrics.setMaximumRejectedJobs(U.bytesToInt(data, off));

        off += 4;

        metrics.setCurrentRejectedJobs(U.bytesToInt(data, off));

        off += 4;

        metrics.setAverageRejectedJobs(U.bytesToFloat(data, off));

        off += 4;

        metrics.setMaximumCancelledJobs(U.bytesToInt(data, off));

        off += 4;

        metrics.setCurrentCancelledJobs(U.bytesToInt(data, off));

        off += 4;

        metrics.setAverageCancelledJobs(U.bytesToFloat(data, off));

        off += 4;

        metrics.setTotalRejectedJobs(U.bytesToInt(data, off));

        off += 4;

        metrics.setTotalCancelledJobs(U.bytesToInt(data, off));

        off += 4;

        metrics.setTotalExecutedJobs(U.bytesToInt(data, off));

        off += 4;

        metrics.setMaximumJobWaitTime(U.bytesToLong(data, off));

        off += 8;

        metrics.setCurrentJobWaitTime(U.bytesToLong(data, off));

        off += 8;

        metrics.setAverageJobWaitTime(U.bytesToDouble(data, off));

        off += 8;

        metrics.setMaximumJobExecuteTime(U.bytesToLong(data, off));

        off += 8;

        metrics.setCurrentJobExecuteTime(U.bytesToLong(data, off));

        off += 8;

        metrics.setAverageJobExecuteTime(U.bytesToDouble(data, off));

        off += 8;

        metrics.setCurrentIdleTime(U.bytesToLong(data, off));

        off += 8;

        metrics.setTotalIdleTime(U.bytesToLong(data, off));

        off += 8;

        metrics.setAvailableProcessors(U.bytesToInt(data, off));

        off += 4;

        metrics.setCurrentCpuLoad(U.bytesToDouble(data, off));

        off += 8;

        metrics.setAverageCpuLoad(U.bytesToDouble(data, off));

        off += 8;

        metrics.setHeapMemoryInitialized(U.bytesToLong(data, off));

        off += 8;

        metrics.setHeapMemoryUsed(U.bytesToLong(data, off));

        off += 8;

        metrics.setHeapMemoryCommitted(U.bytesToLong(data, off));

        off += 8;

        metrics.setHeapMemoryMaximum(U.bytesToLong(data, off));

        off += 8;

        metrics.setNonHeapMemoryInitialized(U.bytesToLong(data, off));

        off += 8;

        metrics.setNonHeapMemoryUsed(U.bytesToLong(data, off));

        off += 8;

        metrics.setNonHeapMemoryCommitted(U.bytesToLong(data, off));

        off += 8;

        metrics.setNonHeapMemoryMaximum(U.bytesToLong(data, off));

        off += 8;

        metrics.setStartTime(U.bytesToLong(data, off));

        off += 8;

        metrics.setNodeStartTime(U.bytesToLong(data, off));

        off += 8;

        metrics.setUpTime(U.bytesToLong(data, off));

        off += 8;

        metrics.setCurrentThreadCount(U.bytesToInt(data, off));

        off += 4;

        metrics.setMaximumThreadCount(U.bytesToInt(data, off));

        off += 4;

        metrics.setTotalStartedThreadCount(U.bytesToLong(data, off));

        off += 8;

        metrics.setCurrentDaemonThreadCount(U.bytesToInt(data, off));

        off += 4;

        metrics.setFileSystemFreeSpace(U.bytesToLong(data, off));

        off += 8;

        metrics.setFileSystemTotalSpace(U.bytesToLong(data, off));

        off += 8;

        metrics.setFileSystemUsableSpace(U.bytesToLong(data, off));

        off += 8;

        metrics.setLastDataVersion(U.bytesToLong(data, off));

        off += 8;

        assert off - start == METRICS_SIZE : "Invalid metrics size [expected=" + METRICS_SIZE + ", actual=" +
            (off - start) + ']';

        return metrics;
    }
}
