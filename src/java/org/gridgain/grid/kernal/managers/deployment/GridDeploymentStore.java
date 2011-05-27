// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.managers.deployment;

import org.gridgain.grid.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * Interface for all deployment stores.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public interface GridDeploymentStore {
    /**
     * Starts store.
     *
     * @throws GridException If start failed.
     */
    public void start() throws GridException;

    /**
     * Stops store.
     */
    public void stop();

    /**
     * Kernal started callback.
     *
     * @throws GridException If callback execution failed.
     */
    public void onKernalStart() throws GridException;

    /**
     * Kernel stopping callback.
     */
    public void onKernalStop();

    /**
     * @param meta Deployment metadata.
     * @return Deployment.
     */
    @Nullable public GridDeployment getDeployment(GridDeploymentMetadata meta);

    /**
     * Gets class loader based on ID.
     *
     * @param ldrId Class loader ID.
     * @return Class loader of {@code null} if not found.
     */
    @Nullable public GridDeployment getDeployment(UUID ldrId);

    /**
     * @return All current deployments.
     */
    public Collection<GridDeployment> getDeployments();

    /**
     * Explicitly deploys class.
     *
     * @param cls Class to explicitly deploy.
     * @param clsLdr Class loader.
     * @return Grid deployment.
     * @throws GridException Id deployment failed.
     */
    public GridDeployment explicitDeploy(Class<?> cls, ClassLoader clsLdr) throws GridException;

    /**
     * @param nodeId Optional ID of node that initiated request.
     * @param rsrcName Undeploys all deployments that have given
     */
    public void explicitUndeploy(@Nullable UUID nodeId, String rsrcName);
}
