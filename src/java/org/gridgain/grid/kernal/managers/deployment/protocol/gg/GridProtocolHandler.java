// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.managers.deployment.protocol.gg;

import org.gridgain.grid.kernal.managers.deployment.*;
import java.io.*;
import java.net.*;

/**
 * Custom stream protocol handler implementation.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridProtocolHandler extends URLStreamHandler {
    /** Deployment manager. */
    private static GridDeploymentManager mgr;

    /**
     * Registers deployment manager.
     * 
     * @param mgr Deployment manager.
     */
    public static void registerDeploymentManager(GridDeploymentManager mgr) {
        assert mgr != null;

        GridProtocolHandler.mgr = mgr;
    }

    /**
     * Deregisters deployment manager.
     */
    public static void deregisterDeploymentManager() {
         mgr = null;
    }

    /** {@inheritDoc} */
    @Override protected URLConnection openConnection(URL url) throws IOException {
        return new GridUrlConnection(url, mgr);
    }
}
