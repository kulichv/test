// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.deployment;

import org.gridgain.grid.*;
import org.gridgain.grid.spi.*;
import org.jetbrains.annotations.*;

/**
 * Grid deployment SPI is in charge of deploying tasks and classes from different
 * sources.
 * <p>
 * Class loaders that are in charge of loading task classes (and other classes)
 * can be deployed directly by calling {@link #register(ClassLoader, Class)}} method or
 * by SPI itself, for example by asynchronously scanning some folder for new tasks.
 * When method {@link #findResource(String)} is called by the system, SPI must return a
 * class loader associated with given class. Every time a class loader
 * gets (re)deployed or released, callbacks
 * {@link GridDeploymentListener#onUnregistered(ClassLoader)}} must be called by SPI.
 * <p>
 * If peer class loading is enabled (which is default behavior, see
 * {@link GridConfiguration#isPeerClassLoadingEnabled()}), then it is usually
 * enough to deploy class loader only on one grid node. Once a task starts executing
 * on the grid, all other nodes will automatically load all task classes from
 * the node that initiated the execution. Hot redeployment is also supported
 * with peer class loading. Every time a task changes and gets redeployed on a
 * node, all other nodes will detect it and will redeploy this task as well.
 * <strong>
 * Note that peer class loading comes into effect only if a task was
 * not locally deployed, otherwise, preference will always be given to
 * local deployment.
 * </strong>
 * <p>
 * Gridgain provides the following {@code GridDeploymentSpi} implementations:
 * <ul>
 * <li>{@link org.gridgain.grid.spi.deployment.local.GridLocalDeploymentSpi}</li>
 * <li>{@link org.gridgain.grid.spi.deployment.uri.GridUriDeploymentSpi}</li>
 * </ul>
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public interface GridDeploymentSpi extends GridSpi, GridSpiJsonConfigurable {
    /**
     * Finds class loader for the given class.
     *
     * @param rsrcName Class name or class alias to find class loader for.
     * @return Deployed class loader, or {@code null} if not deployed.
     */
    public GridDeploymentResource findResource(String rsrcName);

    /**
     * Registers a class loader with this SPI. This method exists
     * to be able to add external class loaders to deployment SPI.
     * Deployment SPI may also have its own class loaders. For example,
     * in case of GAR deployment, every GAR file is loaded and deployed
     * with a separate class loader maintained internally by the SPI.
     * <p>
     * The array of classes passed in should be checked for presence of
     * {@link GridTaskName} annotations. The classes that have this annotation
     * should be accessible by this name from {@link #findResource(String)} method.
     *
     * @param ldr Class loader to register.
     * @param rsrc Class that should be checked for aliases.
     *      Currently the only alias in the system is {@link GridTaskName} for
     *      task classes; in future, there may be others.
     * @return {@code True} if resource was registered.
     * @throws GridSpiException If registration failed.
     */
    public boolean register(ClassLoader ldr, Class<?> rsrc) throws GridSpiException;

    /**
     * Unregisters all class loaders that have a class with given name or have
     * a class with give {@link GridTaskName} value.
     *
     * @param rsrcName Either class name or {@link GridTaskName} value for a class
     *      whose class loader needs to be unregistered.
     * @return {@code True} if resource was unregistered.
     */
    public boolean unregister(String rsrcName);

    /**
     * Sets or unsets deployment event listener. Grid implementation will use this listener
     * to properly add and remove various deployments.
     *
     * @param lsnr Listener for deployment events. {@code null} to unset the listener.
     */
    public void setListener(@Nullable GridDeploymentListener lsnr);
}
