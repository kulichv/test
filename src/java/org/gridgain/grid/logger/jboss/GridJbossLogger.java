// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.logger.jboss;

import org.gridgain.grid.lang.*;
import org.gridgain.grid.logger.*;
import org.gridgain.grid.resources.*;
import org.jboss.logging.*;
import org.jetbrains.annotations.*;

/**
 * Logger to use in JBoss loaders. Implementation simply delegates to
 * <a target=_new href="http://www.jboss.org/developers/guides/logging">JBoss</a> logging.
 * <p>
 * Please take a look at <a target=_new href="http://wiki.jboss.org/wiki/Logging">JBoss Wiki</a>
 * and <a target=_new href="http://docs.jboss.org/process-guide/en/html/logging.html>Logging guide</a>
 * for additional information.
 * <p>
 * It's recommended to use GridGain logger injection instead of using/instantiating
 * logger in your task/job code. See {@link GridLoggerResource} annotation about logger
 * injection.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridJbossLogger extends GridMetadataAwareAdapter implements GridLogger {
    /** Log4j implementation proxy. */
    private Logger impl;

    /**
     * Creates new logger with given implementation.
     */
    public GridJbossLogger() {
        this(Logger.getLogger("root"));
    }

    /**
     * Creates new logger with given implementation.
     *
     * @param impl Log4j implementation to use.
     */
    public GridJbossLogger(Logger impl) {
        assert impl != null;

        this.impl = impl;
    }

    /** {@inheritDoc} */
    @Override public GridJbossLogger getLogger(Object ctgr) {
        return new GridJbossLogger(Logger.getLogger(ctgr.toString()));
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
    @Override public boolean isQuiet() {
        return !isInfoEnabled() && !isDebugEnabled();
    }

    /** {@inheritDoc} */
    @Override public boolean isDebugEnabled() {
        return impl.isDebugEnabled();
    }

    /** {@inheritDoc} */
    @Override public boolean isInfoEnabled() {
        return impl.isInfoEnabled();
    }
}
