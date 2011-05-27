package org.gridgain.grid.spi.checkpoint.cache;

import org.gridgain.grid.*;
import org.gridgain.grid.events.*;
import org.gridgain.grid.logger.*;
import org.gridgain.grid.resources.*;
import org.gridgain.grid.spi.*;
import org.gridgain.grid.spi.checkpoint.*;
import org.gridgain.grid.typedef.*;
import org.gridgain.grid.typedef.internal.*;
import org.jetbrains.annotations.*;

import static org.gridgain.grid.GridEventType.*;

/**
 * This class defines cache-based implementation for checkpoint SPI.
 * <h1 class="header">Configuration</h1>
 * <h2 class="header">Mandatory</h2>
 * This SPI has no mandatory configuration parameters.
 * <h2 class="header">Optional</h2>
 * This SPI has following optional configuration parameters:
 * <ul>
 * <li>Cache name (see {@link #setCacheName(String)})</li>
 * </ul>
 * <h2 class="header">Java Example</h2>
 * {@link GridCacheCheckpointSpi} can be configured as follows:
 * <pre name="code" class="java">
 * GridConfigurationAdapter cfg = new GridConfigurationAdapter();
 *
 * String cacheName = "checkpoints";
 *
 * GridCacheConfigurationAdapter cacheConfig = new GridCacheConfigurationAdapter();
 *
 * cacheConfig.setName(cacheName);
 *
 * GridCacheCheckpointSpi spi = new GridCacheCheckpointSpi();
 *
 * spi.setCacheName(cacheName);
 *
 * cfg.setCacheConfiguration(cacheConfig);
 *
 * // Override default checkpoint SPI.
 * cfg.setCheckpointSpi(cpSpi);
 *
 * // Start grid.
 * G.start(cfg);
 * </pre>
 * <h2 class="header">Spring Example</h2>
 * {@link GridCacheCheckpointSpi} can be configured from Spring XML configuration file:
 * <pre name="code" class="xml">
 * &lt;bean id="grid.custom.cfg" class="org.gridgain.grid.GridConfigurationAdapter" singleton="true"&gt;
 *     ...
 *         &lt;!-- Cache configuration. --&gt;
 *         &lt;property name=&quot;cacheConfiguration&quot;&gt;
 *             &lt;list&gt;
 *                 &lt;bean class=&quot;org.gridgain.grid.cache.GridCacheConfigurationAdapter&quot;&gt;
 *                     &lt;property name=&quot;name&quot; value=&quot;CACHE_NAME&quot;/&gt;
 *                 &lt;/bean&gt;
 *             &lt;/list&gt;
 *         &lt;/property&gt;
 *
 *         &lt;!-- SPI configuration. --&gt;
 *         &lt;property name=&quot;checkpointSpi&quot;&gt;
 *             &lt;bean class=&quot;org.gridgain.grid.spi.checkpoint.cache.GridCacheCheckpointSpi&quot;&gt;
 *                 &lt;property name=&quot;cacheName&quot; value=&quot;CACHE_NAME&quot;/&gt;
 *             &lt;/bean&gt;
 *         &lt;/property&gt;
 *     ...
 * &lt;/bean&gt;
 * </pre>
 * <p>
 * <img src="http://www.gridgain.com/images/spring-small.png">
 * <br>
 * For information about Spring framework visit <a href="http://www.springframework.org/">www.springframework.org</a>
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 * @see GridCheckpointSpi
 */
@GridSpiInfo(
    author = "GridGain Systems, Inc.",
    url = "www.gridgain.com",
    email = "support@gridgain.com",
    version = "3.0.9c.27052011")
@GridSpiMultipleInstancesSupport(true)
public class GridCacheCheckpointSpi extends GridSpiAdapter implements GridCheckpointSpi, GridCacheCheckpointSpiMBean {
    /** Default cache name (value is <tt>checkpoints</tt>). */
    public static final String DFLT_CACHE_NAME = "checkpoints";

    /** Logger. */
    @GridLoggerResource
    private GridLogger log;

    /** Cache name. */
    private String cacheName = DFLT_CACHE_NAME;

    /** Listener. */
    private GridCheckpointListener lsnr;

    /** Grid event listener. */
    private GridLocalEventListener evtLsnr;

    /**
     * Sets cache name to be used by this SPI.
     * <p>
     * If cache name is not provided {@link #DFLT_CACHE_NAME} is used.
     *
     * @param cacheName Cache name.
     */
    @GridSpiConfiguration(optional = true)
    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    /** {@inheritDoc} */
    @Override public String getCacheName() {
        return cacheName;
    }

    /** {@inheritDoc} */
    @Override public void spiStart(@Nullable String gridName) throws GridSpiException {
        assertParameter(!F.isEmpty(cacheName), "!F.isEmpty(cacheName)");

        // Start SPI start stopwatch.
        startStopwatch();

        // Ack ok start.
        if (log.isDebugEnabled())
            log.debug(configInfo("cacheName", cacheName));

        registerMBean(gridName, this, GridCacheCheckpointSpiMBean.class);

        if (log.isDebugEnabled())
            log.debug(startInfo());
    }

    /** {@inheritDoc} */
    @Override public void onContextInitialized(GridSpiContext spiCtx) throws GridSpiException {
        super.onContextInitialized(spiCtx);

        getSpiContext().addLocalEventListener(evtLsnr = new GridLocalEventListener() {
            /** {@inheritDoc} */
            @Override public void onEvent(GridEvent evt) {
                assert evt != null;
                assert evt.type() == EVT_CACHE_OBJECT_REMOVED;

                GridCacheEvent e = (GridCacheEvent)evt;

                if (e.oldValue() != null) {
                    GridCheckpointListener tmp = lsnr;

                    if (tmp != null)
                        tmp.onCheckpointRemoved((String)((GridCacheEvent)evt).key());
                }
            }
        }, EVT_CACHE_OBJECT_REMOVED);
    }

    /** {@inheritDoc} */
    @Override public void spiStop() throws GridSpiException {
        unregisterMBean();

        // Ack ok stop.
        if (log.isDebugEnabled())
            log.debug(stopInfo());
    }

    /** {@inheritDoc} */
    @Override public void onContextDestroyed() {
        if (evtLsnr != null) {
            GridSpiContext ctx = getSpiContext();

            if (ctx != null)
                ctx.removeLocalEventListener(evtLsnr);
        }

        super.onContextDestroyed();
    }

    /** {@inheritDoc} */
    @Nullable @Override public byte[] loadCheckpoint(String key) throws GridSpiException {
        assert key != null;

        try {
            return getSpiContext().get(cacheName, key);
        }
        catch (GridException e) {
            throw new GridSpiException("Failed to load checkpoint data [key=" + key + ']', e);
        }
    }

    /** {@inheritDoc} */
    @Override public boolean saveCheckpoint(String key, byte[] state, long timeout, boolean override)
        throws GridSpiException {
        assert key != null;
        assert timeout >= 0;

        try {
            if (override) {
                getSpiContext().put(cacheName, key, state, timeout);

                return true;
            }
            else
                return getSpiContext().putIfAbsent(cacheName, key, state, timeout) == null;
        }
        catch (GridException e) {
            throw new GridSpiException("Failed to save checkpoint data [key=" + key +
                ", stateSize=" + state.length + ", timeout=" + timeout + ']', e);
        }
    }

    /** {@inheritDoc} */
    @Override public boolean removeCheckpoint(String key) {
        assert key != null;

        try {
            return getSpiContext().remove(cacheName, key) != null;
        }
        catch (GridException e) {
            U.error(log, "Failed to remove checkpoint data [key=" + key + ']', e);

            return false;
        }
    }

    /** {@inheritDoc} */
    @Override public void setCheckpointListener(GridCheckpointListener lsnr) {
        this.lsnr = lsnr;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridCacheCheckpointSpi.class, this);
    }
}
