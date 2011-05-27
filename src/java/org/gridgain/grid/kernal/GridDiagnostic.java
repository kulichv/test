// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal;

import org.gridgain.grid.logger.*;
import org.gridgain.grid.typedef.internal.*;
import org.gridgain.grid.util.worker.*;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

/**
 * This class run some basic network diagnostic tests in the background and
 * reports errors or suspicious results.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
final class GridDiagnostic {
    /** */
    private static final int REACH_TIMEOUT = 2000;

    /**
     * Ensure singleton.
     */
    private GridDiagnostic() {
        // No-op.
    }

    /**
     * @param gridName Grid instance name. Can be {@code null}.
     * @param exec Executor service.
     * @param parentLog Parent logger.
     */
    static void runBackgroundCheck(String gridName, Executor exec, GridLogger parentLog) {
        assert exec != null;
        assert parentLog != null;

        final GridLogger log = parentLog.getLogger(GridDiagnostic.class);

        try {
            exec.execute(new GridWorker(gridName, "grid-diagnostic-1", log) {
                @Override public void body() {
                    try {
                        InetAddress localHost = U.getLocalHost();

                        if (!localHost.isReachable(REACH_TIMEOUT)) {
                            U.warn(log, "Default local host is unreachable. This may lead to delays on " +
                                "grid network operations. Check your OS network setting to correct it.",
                                "Default local host is unreachable.");
                        }
                    }
                    catch (IOException ignore) {
                        U.warn(log, "Failed to perform network diagnostics. It is usually caused by serious " +
                            "network configuration problem. Check your OS network setting to correct it.",
                            "Failed to perform network diagnostics.");
                    }
                }
            });

            exec.execute(new GridWorker(gridName, "grid-diagnostic-2", log) {
                @Override public void body() {
                    try {
                        InetAddress localHost = U.getLocalHost();

                        if (localHost.isLoopbackAddress()) {
                            U.warn(log, "Default local host is a loopback address. This can be a sign of " +
                                "potential network configuration problem.",
                                "Default local host is a loopback address.");
                        }
                    }
                    catch (IOException ignore) {
                        U.warn(log, "Failed to perform network diagnostics. It is usually caused by serious " +
                            "network configuration problem. Check your OS network setting to correct it.",
                            "Failed to perform network diagnostics.");
                    }
                }
            });

            exec.execute(new GridWorker(gridName, "grid-diagnostic-3", log) {
                @Override public void body()  {
                    String jdkStrLow = U.jdkString().toLowerCase();

                    if (jdkStrLow.contains("jrockit") && jdkStrLow.contains("1.5.")) {
                        U.warn(log, "BEA JRockit VM ver. 1.5.x has shown problems with NIO functionality in our " +
                            "tests that were not reproducible in other VMs. We recommend using Sun VM. Should you " +
                            "have further questions please contact us at support@gridgain.com",
                            "BEA JRockit VM ver. 1.5.x is not supported.");
                    }
                }
            });

            exec.execute(new GridWorker(gridName, "grid-diagnostic-4", log) {
                @Override public void body() {
                    // Sufficiently tested OS.
                    if (!U.isSufficientlyTestedOs()) {
                        U.warn(log, "This operating system has been tested less rigorously: " + U.osString() +
                            ". Our team will appreciate the feedback if you experience any problems running " +
                            "gridgain in this environment. You can always send your feedback to support@gridgain.com",
                            "This OS is not fully supported: " + U.osString());
                    }
                }
            });

            exec.execute(new GridWorker(gridName, "grid-diagnostic-5", log) {
                @Override public void body() {
                    // Fix for GG-1075.
                    if (U.allLocalMACs() == null)
                        U.warn(log, "No live network interfaces detected. If IP-multicast discovery is used - " +
                            "make sure to add 127.0.0.1 as a local address.",
                            "No live network interfaces detected.");
                }
            });

            exec.execute(new GridWorker(gridName, "grid-diagnostic-6", log) {
                @Override public void body() {
                    if (System.getProperty("com.sun.management.jmxremote") != null) {
                        String portStr = System.getProperty("com.sun.management.jmxremote.port");

                        if (portStr != null)
                            try {
                                Integer.parseInt(portStr);

                                return;
                            }
                            catch (NumberFormatException ignore) {
                            }

                        U.warn(log, "JMX remote management is enabled but JMX port is either not set or invalid. " +
                            "Check system property 'com.sun.management.jmxremote.port' to make sure it specifies " +
                            "valid TCP/IP port.", "JMX remote port is invalid - JMX management is off.");
                    }
                }
            });
        }
        catch (RejectedExecutionException e) {
            U.error(log, "Failed to start background network diagnostics check due to thread pool execution " +
                "rejection. In most cases it indicates a severe configuration problem with GridGain.",
                "Failed to start background network diagnostics.", e);
        }
    }
}
