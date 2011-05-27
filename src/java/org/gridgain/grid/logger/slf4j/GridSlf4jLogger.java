// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.logger.slf4j;

import org.gridgain.grid.lang.*;
import org.gridgain.grid.logger.*;
import org.gridgain.grid.resources.*;
import org.jetbrains.annotations.*;
import org.slf4j.*;

/**
 * SLF4J-based implementation for logging. This logger should be used
 * by loaders that have prefer slf4j-based logging.
 * <p>
 * Here is an example of configuring SLF4J logger in GridGain configuration Spring file:
 * <pre name="code" class="xml">
 *      &lt;property name="gridLogger"&gt;
 *          &lt;bean class="org.gridgain.grid.logger.slf4j.GridSlf4jLogger"/&gt;
 *      &lt;/property&gt;
 * </pre>
 * <p>
 * It's recommended to use GridGain's logger injection instead of using/instantiating
 * logger in your task/job code. See {@link GridLoggerResource} annotation about logger
 * injection.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridSlf4jLogger extends GridMetadataAwareAdapter implements GridLogger {
    /** SLF4J implementation proxy. */
    private final Logger impl;

    /**
     * Creates new logger.
     */
    public GridSlf4jLogger() {
        impl = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    }

    /**
     * Creates new logger with given implementation.
     *
     * @param impl SLF4J implementation to use.
     */
    public GridSlf4jLogger(Logger impl) {
        assert impl != null;

        this.impl = impl;
    }

    /** {@inheritDoc} */
    @Override public GridSlf4jLogger getLogger(Object ctgr) {
        Logger impl = ctgr == null ? LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) :
            ctgr instanceof Class ? LoggerFactory.getLogger(((Class<?>)ctgr).getName()) :
                LoggerFactory.getLogger(ctgr.toString());

        return new GridSlf4jLogger(impl);
    }

    /** {@inheritDoc} */
    @Override public void debug(String msg) {
        if (!impl.isDebugEnabled()) {
            warning("Logging at DEBUG level without checking if DEBUG level is enabled: " + msg);
        }

        impl.debug(msg);
    }

    /** {@inheritDoc} */
    @Override public void info(String msg) {
        if (!impl.isInfoEnabled()) {
            warning("Logging at INFO level without checking if INFO level is enabled: " + msg);
        }

        impl.info(msg);
    }

    /** {@inheritDoc} */
    @Override public void warning(String msg) {
        impl.warn(msg);
    }

    /** {@inheritDoc} */
    @Override public void warning(String msg, @Nullable Throwable e) {
        impl.warn(msg, e);
    }

    /** {@inheritDoc} */
    @Override public void error(String msg) {
        impl.error(msg);
    }

    /** {@inheritDoc} */
    @Override public void error(String msg, @Nullable Throwable e) {
        impl.error(msg, e);
    }

    /** {@inheritDoc} */
    @Override public boolean isInfoEnabled() {
        return impl.isInfoEnabled();
    }

    /** {@inheritDoc} */
    @Override public boolean isDebugEnabled() {
        return impl.isDebugEnabled();
    }

    /** {@inheritDoc} */
    @Override public boolean isQuiet() {
        return !isInfoEnabled() && !isDebugEnabled();
    }
}
