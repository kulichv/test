// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.logger;

import org.gridgain.grid.*;
import org.gridgain.grid.resources.*;
import org.gridgain.grid.util.tostring.*;
import org.jetbrains.annotations.*;

/**
 * This interface defines basic logging functionality used throughout the system. We had to
 * abstract it out so that we can use whatever logging is used by the hosting environment.
 * Currently, <a target=_new href="http://logging.apache.org/log4j/docs/">log4j</a>,
 * <a target=_new href="http://www.jboss.org/developers/guides/logging">JBoss</a>,
 * <a target=_new href="http://jakarta.apache.org/commons/logging/">JCL</a> and
 * console logging are provided as supported implementations.
 * <p>
 * GridGain logger could be configured either from code (for example log4j logger):
 * <pre name="code" class="java">
 *      GridConfiguration cfg = new GridConfigurationAdapter();
 *      ...
 *      URL xml = U.resolveGridGainUrl("modules/tests/config/log4j-test.xml");
 *      GridLogger log = new GridLog4jLogger(xml);
 *      ...
 *      cfg.setGridLogger(log);
 * </pre>
 * or in grid configuration file (see JCL logger example below):
 * <pre name="code" class="xml">
 *      ...
 *      &lt;property name="gridLogger"&gt;
 *          &lt;bean class="org.gridgain.grid.logger.jcl.GridJclLogger"&gt;
 *              &lt;constructor-arg type="org.apache.commons.logging.Log"&gt;
 *                  &lt;bean class="org.apache.commons.logging.impl.Log4JLogger"&gt;
 *                      &lt;constructor-arg type="java.lang.String" value="config/default-log4j.xml"/&gt;
 *                  &lt;/bean&gt;
 *              &lt;/constructor-arg&gt;
 *          &lt;/bean&gt;
 *      &lt;/property&gt;
 *      ...
 * </pre>
 * It's recommended to use GridGain's logger injection instead of using/instantiating
 * logger in your task/job code. See {@link GridLoggerResource} annotation about logger
 * injection.
 * <h1 class="header">Quiet Mode</h1>
 * By default GridGain 3.0 starts in quite mode suppressing {@code INFO} and {@code DEBUG}
 * log output. If system property {@code GRIDGAIN_QUIET} is set to {@code false} than GridGain
 * will operate in normal un-suppressed logging mode. Note that all output in "quiet" mode is
 * done through standard output (STDOUT). 
 * <p>
 * Note that GridGain's standard startup scripts <tt>$GRIDGAIN_HOME/bin/ggstart.{sh|bat}</tt> accept
 * {@code -q} arguments to turn on 'quiet' mode.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
@GridToStringExclude
public interface GridLogger extends GridMetadataAware {
    /**
     * Creates new logger with given category based off the current instance.
     *
     * @param ctgr Category for new logger.
     * @return New logger with given category.
     */
    public GridLogger getLogger(Object ctgr);

    /**
     * Logs out debug message.
     *
     * @param msg Debug message.
     */
    public void debug(String msg);

    /**
     * Logs out information message.
     *
     * @param msg Information message.
     */
    public void info(String msg);

    /**
     * Logs out warning message.
     *
     * @param msg Warning message.
     */
    public void warning(String msg);

    /**
     * Logs out warning message with optional exception.
     *
     * @param msg Warning message.
     * @param e Optional exception (can be {@code null}).
     */
    public void warning(String msg, @Nullable Throwable e);

    /**
     * Logs out error message.
     *
     * @param msg Error message.
     */
    public void error(String msg);

    /**
     * Logs error message with optional exception.
     *
     * @param msg Error message.
     * @param e Optional exception (can be {@code null}).
     */
    public void error(String msg, @Nullable Throwable e);

    /**
     * Tests whether {@code debug} level is enabled.
     *
     * @return {@code true} in case when {@code debug} level is enabled, {@code false} otherwise.
     */
    public boolean isDebugEnabled();

    /**
     * Tests whether {@code info} level is enabled.
     *
     * @return {@code true} in case when {@code info} level is enabled, {@code false} otherwise.
     */
    public boolean isInfoEnabled();

    /**
     * Tests whether {@code info} and {@code debug} levels are turned off.
     *
     * @return Whether {@code info} and {@code debug} levels are turned off.
     */
    public boolean isQuiet();
}
