// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.checkpoint.sharedfs;

import org.gridgain.grid.*;
import org.gridgain.grid.kernal.*;
import org.gridgain.grid.logger.*;
import org.gridgain.grid.marshaller.*;
import org.gridgain.grid.resources.*;
import org.gridgain.grid.spi.*;
import org.gridgain.grid.spi.checkpoint.*;
import org.gridgain.grid.typedef.*;
import org.gridgain.grid.typedef.internal.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

/**
 * This class defines shared file system {@link GridCheckpointSpi} implementation for
 * checkpoint SPI. All checkpoints are stored on shared storage and available for all
 * nodes in the grid. Note that every node must have access to the shared directory. The
 * reason the directory needs to be {@code shared} is because a job state
 * can be saved on one node and loaded on another (e.g. if a job gets
 * preempted on a different node after node failure). When started, this SPI tracks
 * all checkpoints saved by localhost for expiration. Note that this SPI does not
 * cache data stored in checkpoints - all the data is loaded from file system
 * on demand.
 * <p>
 * Directory paths for shared checkpoints should either be empty or contain previously
 * stored checkpoint files.
 * <p>
 * <h1 class="header">Configuration</h1>
 * <h2 class="header">Mandatory</h2>
 * This SPI has no mandatory configuration parameters.
 * <h2 class="header">Optional</h2>
 * This SPI has following optional configuration parameters:
 * <ul>
 * <li>Directory paths (see {@link #setDirectoryPaths(Collection)})</li>
 * </ul>
 * <h2 class="header">Java Example</h2>
 * {@link GridSharedFsCheckpointSpi} can be configured as follows:
 * <pre name="code" class="java">
 * GridConfigurationAdapter cfg = new GridConfigurationAdapter();
 *
 * GridSharedFsCheckpointSpi checkpointSpi = new GridSharedFsCheckpointSpi();
 *
 * // List of checkpoint directories where all files are stored.
 * Collection<String> dirPaths = new ArrayList<String>();
 *
 * dirPaths.add("/my/directory/path");
 * dirPaths.add("/other/directory/path");
 *
 * // Override default directory path.
 * checkpointSpi.setDirectoryPaths(dirPaths);
 *
 * // Override default checkpoint SPI.
 * cfg.setCheckpointSpi(checkpointSpi);
 *
 * // Starts grid.
 * G.start(cfg);
 * </pre>
 * <h2 class="header">Spring Example</h2>
 * {@link GridSharedFsCheckpointSpi} can be configured from Spring XML configuration file:
 * <pre name="code" class="xml">
 * &lt;bean id="grid.custom.cfg" class="org.gridgain.grid.GridConfigurationAdapter" singleton="true"&gt;
 *     ...
 *     &lt;property name="checkpointSpi"&gt;
 *         &lt;bean class="org.gridgain.grid.spi.checkpoint.sharedfs.GridSharedFsCheckpointSpi"&gt;
 *             &lt;!-- Change to shared directory path in your environment. --&gt;
 *             &lt;property name="directoryPaths"&gt;
 *                 &lt;list&gt;
 *                     &lt;value&gt;/my/directory/path&lt;/value&gt;
 *                     &lt;value&gt;/other/directory/path&lt;/value&gt;
 *                 &lt;/list&gt;
 *             &lt;/property&gt;
 *         &lt;/bean&gt;
 *     &lt;/property&gt;
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
public class GridSharedFsCheckpointSpi extends GridSpiAdapter implements GridCheckpointSpi,
    GridSharedFsCheckpointSpiMBean {
    /**
     * Default checkpoint directory (value is {@code work/checkpoint/sharedfs}).
     * Note that this path used relatively {@code GRIDGAIN_HOME} directory when {@code GRIDGAIN_HOME} exists.
     * For unknown {@code GRIDGAIN_HOME} used another directory {@link #DFLT_TMP_DIR}
     */
    public static final String DFLT_DIR_PATH = "work/cp/sharedfs";

    /**
     * Default directory name for SPI when {@code GRIDGAIN_HOME} not defined.
     * This directory name relative to file path in {@code java.io.tmpdir} system property value.
     */
    public static final String DFLT_TMP_DIR = ".gg.sharedfs.cp";

    /** */
    private static final String CODES = "0123456789QWERTYUIOPASDFGHJKLZXCVBNM";

    /** */
    private static final int CODES_LEN = CODES.length();

    /** Grid logger. */
    @GridLoggerResource
    private GridLogger log;

    /** Grid marshaller. */
    @GridMarshallerResource
    private GridMarshaller marsh;

    /** List of checkpoint directories where all files are stored. */
    private Queue<String> dirPaths = new LinkedList<String>();

    /** Current folder where all checkpoints are saved. */
    private String curDirPath = DFLT_DIR_PATH;

    /**
     * Either {@link #curDirPath} value if it is absolute
     * path or @{GRID_GAIN_HOME}/{@link #curDirPath} if one above was not found.
     */
    private File folder;

    /** Local host name. */
    private String host;

    /** Grid name. */
    private String gridName;

    /** Task that takes care about outdated files. */
    private GridSharedFsTimeoutTask timeoutTask;

    /** Listener. */
    private GridCheckpointListener lsnr;

    /**
     * Initializes default directory paths.
     */
    public GridSharedFsCheckpointSpi() {
        dirPaths.offer(DFLT_DIR_PATH);
    }

     /** {@inheritDoc} */
    @Override public Collection<String> getDirectoryPaths() {
        return dirPaths;
    }

    /** {@inheritDoc} */
    @Override public String getCurrentDirectoryPath() {
        return curDirPath;
    }

    /**
     * Sets path to a shared directory where checkpoints will be stored. The
     * path can either be absolute or relative to {@code GRIDGAIN_HOME} system
     * or environment variable.
     * <p>
     * If not provided, default value is {@link #DFLT_DIR_PATH}.
     *
     * @param dirPaths Absolute or GridGain installation home folder relative path where checkpoints
     * will be stored.
     */
    @GridSpiConfiguration(optional = true)
    public void setDirectoryPaths(Collection<String> dirPaths) {
        A.ensure(!F.isEmpty(dirPaths), "!F.isEmpty(dirPaths)");

        this.dirPaths.clear();
        this.dirPaths.addAll(dirPaths);
    }

    /** {@inheritDoc} */
    @Override public void spiStart(String gridName) throws GridSpiException {
        // Start SPI start stopwatch.
        startStopwatch();

        assertParameter(!F.isEmpty(dirPaths), "!F.isEmpty(dirPaths)");

        this.gridName = gridName;

        folder = getNextSharedPath();

        if (folder == null)
            throw new GridSpiException("Failed to create checkpoint directory.");

        if (!folder.isDirectory())
            throw new GridSpiException("Checkpoint directory path is not a valid directory: " + curDirPath);

        registerMBean(gridName, this, GridSharedFsCheckpointSpiMBean.class);

        // Ack parameters.
        if (log.isDebugEnabled()) {
            log.debug(configInfo("folder", folder));
            log.debug(configInfo("dirPaths", dirPaths));
        }

        try {
            host = U.getLocalHost().getHostName();
        }
        catch (IOException e) {
            throw new GridSpiException("Failed to get localhost address.", e);
        }

        // Ack ok start.
        if (log.isDebugEnabled())
            log.debug(startInfo());
    }

    /** {@inheritDoc} */
    @Override public void spiStop() throws GridSpiException {
        if (timeoutTask != null) {
            U.interrupt(timeoutTask);
            U.join(timeoutTask, log);
        }

        unregisterMBean();

        // Clean resources.
        folder = null;
        host = null;

        // Ack ok stop.
        if (log.isDebugEnabled())
            log.debug(stopInfo());
    }

    /**
     * Gets next available shared path if possible or {@code null}.
     *
     * @return File object represented shared directory.
     * @throws GridSpiException Throws if initializing has filed.
     */
    @Nullable private File getNextSharedPath() throws GridSpiException {
        if (folder != null) {
            folder = null;

            dirPaths.poll();
        }

        if (timeoutTask != null) {
            U.interrupt(timeoutTask);
            U.join(timeoutTask, log);
        }

        while (!dirPaths.isEmpty()) {
            curDirPath = dirPaths.peek();

            if (new File(curDirPath).exists())
                folder = new File(curDirPath);
            else {
                if (getGridGainHome() != null && getGridGainHome().length() > 0) {
                    // Create relative by default.
                    folder = new File(getGridGainHome(), curDirPath);

                    if (!folder.mkdirs() && !folder.exists()) {
                        // Remove failed directory.
                        dirPaths.poll();

                        // Select next shared directory if exists, otherwise throw exception.
                        if (!dirPaths.isEmpty())
                            continue;
                        else
                            throw new GridSpiException("Checkpoint directory does not exist and cannot be created : " +
                                folder);
                    }
                }
                else {
                    String tmpDirPath = System.getProperty("java.io.tmpdir");

                    if (tmpDirPath == null)
                        throw new GridSpiException("System property 'java.io.tmpdir' is invalid.");

                    folder = new File(tmpDirPath, DFLT_TMP_DIR);

                    if (!folder.mkdirs() && !folder.exists())
                        throw new GridSpiException("Failed to create checkpoint directory: " + folder);
                }

                if (log.isDebugEnabled())
                    log.debug("Created shared filesystem checkpoint directory: " + folder.getAbsolutePath());
            }

            break;
        }

        if (folder != null) {
            Map<File, GridSharedFsTimeData> files = new HashMap<File, GridSharedFsTimeData>();

            // Track expiration for only those files that are made by this node
            // to avoid file access conflicts.
            for (File file : getFiles()) {
                if (file.exists()) {
                    if (log.isDebugEnabled())
                        log.debug("Checking checkpoint file: " + file.getAbsolutePath());

                    try {
                        GridSharedFsCheckpointData data = GridSharedFsUtils.read(file, marsh, log);

                        if (data.getHost().equals(host)) {
                            files.put(file, new GridSharedFsTimeData(data.getExpireTime(), file.lastModified(),
                                data.getKey()));

                            if (log.isDebugEnabled())
                                log.debug("Registered existing checkpoint from: " + file.getAbsolutePath());
                        }
                    }
                    catch (GridException e) {
                        U.error(log, "Failed to unmarshal objects in checkpoint file (ignoring): " +
                            file.getAbsolutePath(), e);
                    }
                    catch (IOException e) {
                        U.error(log, "IO error reading checkpoint file (ignoring): " + file.getAbsolutePath(), e);
                    }
                }
            }

            timeoutTask = new GridSharedFsTimeoutTask(gridName, marsh, log);

            timeoutTask.setCheckpointListener(lsnr);

            timeoutTask.add(files);

            timeoutTask.start();
        }

        return folder;
    }

    /**
     * Returns new file name for the given key. Since fine name is based on the key,
     * the key must be unique. This method converts string key into hexadecimal-based
     * string to avoid conflicts of special characters in file names.
     *
     * @param key Unique checkpoint key.
     * @return Unique checkpoint file name.
     */
    private String getUniqueFileName(CharSequence key) {
        assert key != null;

        SB sb = new SB();

        // To be overly safe we'll limit file name size
        // to 128 characters (124 characters name + 4 character extension).
        // We also limit file name to upper case only to avoid surprising
        // behavior between Windows and Unix file systems.
        for (int i = 0; i < key.length() && i < 124; i++)
            sb.a(CODES.charAt(key.charAt(i) % CODES_LEN));

        return sb.a(".gcp").toString();
    }

    /** {@inheritDoc} */
    @Override public byte[] loadCheckpoint(String key) throws GridSpiException {
        assert key != null;

        File file = new File(folder, getUniqueFileName(key));

        if (file.exists())
            try {
                GridSharedFsCheckpointData data = GridSharedFsUtils.read(file, marsh, log);

                return data != null ?
                    data.getExpireTime() == 0 || data.getExpireTime() > System.currentTimeMillis() ?
                        data.getState() :
                        null
                    : null;
            }
            catch (GridException e) {
                throw new GridSpiException("Failed to unmarshal objects in checkpoint file: " +
                    file.getAbsolutePath(), e);
            }
            catch (IOException e) {
                throw new GridSpiException("Failed to read checkpoint file: " + file.getAbsolutePath(), e);
            }

        return null;
    }

    /** {@inheritDoc} */
    @Override public boolean saveCheckpoint(String key, byte[] state, long timeout,
        boolean override) throws GridSpiException {
        assert key != null;

        long expireTime = 0;

        if (timeout > 0) {
            expireTime = System.currentTimeMillis() + timeout;

            if (expireTime < 0)
                expireTime = Long.MAX_VALUE;
        }

        boolean saved = false;

        while (!saved) {
            File file = new File(folder, getUniqueFileName(key));

            if (file.exists()) {
                if (!override)
                    return false;

                if (log.isDebugEnabled())
                    log.debug("Overriding existing file: " + file.getAbsolutePath());
            }

            try {
                GridSharedFsUtils.write(file, new GridSharedFsCheckpointData(state, expireTime, host, key),
                    marsh, log);
            }
            catch (IOException e) {
                // Select next shared directory if exists, otherwise throw exception
                if (getNextSharedPath() != null)
                    continue;
                else
                    throw new GridSpiException("Failed to write checkpoint data into file: " +
                        file.getAbsolutePath(), e);
            }
            catch (GridException e) {
                throw new GridSpiException("Failed to marshal checkpoint data into file: " +
                    file.getAbsolutePath(), e);
            }

            if (timeout > 0)
                timeoutTask.add(file, new GridSharedFsTimeData(expireTime, file.lastModified(), key));

            saved = true;
        }

        return true;
    }

    /**
     * Returns list of files in checkpoint directory.
     * All sub-directories and their files are skipped.
     *
     * @return Array of open file descriptors.
     */
    private File[] getFiles() {
        assert folder != null;

        return folder.listFiles(new FileFilter() {
            @Override public boolean accept(File pathName) {
                return !pathName.isDirectory();
            }
        });
    }

    /** {@inheritDoc} */
    @Override public boolean removeCheckpoint(String key) {
        assert key != null;

        File file = new File(folder, getUniqueFileName(key));

        if (timeoutTask != null)
            timeoutTask.remove(file);

        boolean rmv = file.delete();

        if (rmv) {
            GridCheckpointListener lsnr = this.lsnr;

            if (lsnr != null)
                lsnr.onCheckpointRemoved(key);
        }

        return rmv;
    }

    /** {@inheritDoc} */
    @Override public void setCheckpointListener(GridCheckpointListener lsnr) {
        this.lsnr = lsnr;

        if (timeoutTask != null)
            timeoutTask.setCheckpointListener(lsnr);
    }

    /** {@inheritDoc} */
    @Override protected List<String> getConsistentAttributeNames() {
        List<String> attrs = new ArrayList<String>(2);

        attrs.add(createSpiAttributeName(GridNodeAttributes.ATTR_SPI_CLASS));
        attrs.add(createSpiAttributeName(GridNodeAttributes.ATTR_SPI_VER));

        return attrs;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridSharedFsCheckpointSpi.class, this);
    }
}
