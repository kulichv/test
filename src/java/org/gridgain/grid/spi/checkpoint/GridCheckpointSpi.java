// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.checkpoint;

import org.gridgain.grid.*;
import org.gridgain.grid.spi.*;
import org.gridgain.grid.spi.checkpoint.cache.*;
import org.gridgain.grid.spi.checkpoint.coherence.*;
import org.gridgain.grid.spi.checkpoint.jdbc.*;
import org.gridgain.grid.spi.checkpoint.s3.*;
import org.gridgain.grid.spi.checkpoint.sharedfs.*;
import org.jetbrains.annotations.*;

/**
 * Checkpoint SPI provides an ability to save an intermediate job state. It can
 * be useful when long running jobs need to store some intermediate state to
 * protect from system or application failures. Grid job can save intermediate
 * state in certain points of the execution (e.g., periodically) and upon start
 * check if previously saved state exists. This allows job to restart from the last
 * save checkpoint in case of preemption or other types of failover.
 * <p>
 * Note, that since a job can execute on different nodes, checkpoints need to
 * be accessible by all nodes.
 * <p>
 * To manipulate checkpoints from grid job the following public methods are available
 * on task session (that can be injected into grid job):
 * <ul>
 * <li>{@link GridTaskSession#loadCheckpoint(String)}</li>
 * <li>{@link GridTaskSession#removeCheckpoint(String)}</li>
 * <li>{@link GridTaskSession#saveCheckpoint(String, Object)}</li>
 * <li>{@link GridTaskSession#saveCheckpoint(String, Object, GridTaskSessionScope, long)}</li>
 * <li>{@link GridTaskSession#saveCheckpoint(String, Object, GridTaskSessionScope, long, boolean)}</li>
 * </ul>
 * <p>
 * GridGain provides the following {@code GridCheckpointSpi} implementations:
 * <ul>
 * <li>{@link GridSharedFsCheckpointSpi}</li>
 * <li>{@link GridS3CheckpointSpi}</li>
 * <li>{@link GridCoherenceCheckpointSpi}</li>
 * <li>{@link GridJdbcCheckpointSpi}</li>
 * <li>{@link GridCacheCheckpointSpi}</li>
 * </ul>
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
@GridSpiOptional(false)
public interface GridCheckpointSpi extends GridSpi {
    /**
     * Loads checkpoint from storage by its unique key.
     *
     * @param key Checkpoint key.
     * @return Loaded data or {@code null} if there is no data for a given
     *      key.
     * @throws GridSpiException Thrown in case of any error while loading
     *      checkpoint data. Note that in case when given {@code key} is not
     *      found this method will return {@code null}.
     */
    @Nullable public byte[] loadCheckpoint(String key) throws GridSpiException;

    /**
     * Saves checkpoint to the storage.
     *
     * @param key Checkpoint unique key.
     * @param state Saved data.
     * @param timeout Every intermediate data stored by checkpoint provider
     *      should have a timeout. Timeout allows for effective resource
     *      management by checkpoint provider by cleaning saved data that are not
     *      needed anymore. Generally, the user should choose the minimum
     *      possible timeout to avoid long-term resource acquisition by checkpoint
     *      provider. Value {@code 0} means that timeout will never expire.
     * @param override Whether or not override checkpoint if it already exists.
     * @return {@code true} if checkpoint has been actually saved, {@code false} otherwise.
     * @throws GridSpiException Thrown in case of any error while saving
     *    checkpoint data.
     */
    public boolean saveCheckpoint(String key, byte[] state, long timeout, boolean override) throws GridSpiException;

    /**
     * This method instructs the checkpoint provider to clean saved data for a
     * given {@code key}.
     *
     * @param key Key for the checkpoint to remove.
     * @return {@code true} if data has been actually removed, {@code false}
     *      otherwise.
     */
    public boolean removeCheckpoint(String key);

    /**
     * Sets the checkpoint listener.
     *
     * @param lsnr The listener to set or {@code null}.
     */
    public void setCheckpointListener(GridCheckpointListener lsnr);
}
