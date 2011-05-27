// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.cache.affinity;

import org.gridgain.grid.*;
import org.gridgain.grid.cache.*;

import java.io.*;
import java.util.*;

/**
 * Cache key affinity which maps keys to nodes. This interface is utilized for
 * both, replicated and partitioned caches. Cache affinity can be configured
 * for individual caches via {@link GridCacheConfiguration#getAffinity()} method.
 * <p>
 * Whenever a key is given to cache, it is first passed to a pluggable
 * {@link GridCacheAffinityMapper} which may potentially map this key to an alternate
 * key which should be used for affinity. The key returned from
 * {@link GridCacheAffinityMapper#affinityKey(Object)} method is then passed to
 * {@link #partition(Object) partition(Object)} method to find out the partition for the key. Then
 * this partition together with all participating nodes are passed
 * to {@link #nodes(int, Collection) nodes(int, Collection)} method which returns a collection of nodes.
 * This collection of nodes is used for node affinity. In {@link GridCacheMode#REPLICATED REPLICATED}
 * cache mode the key will be cached on all returned nodes; generally, all caching nodes
 * participate in caching every key in replicated mode. In {@link GridCacheMode#PARTITIONED PARTITIONED}
 * mode, only primary and backup nodes are returned with primary node always in the
 * first position. So if there is {@code 1} backup node, then the returned collection will
 * have {@code 2} nodes in it - {@code primary} node in first position, and {@code backup}
 * node in second.
 * <p>
 * For more information about cache affinity and examples refer to {@link GridCacheAffinityMapper} and
 * {@link GridCacheAffinityMapped @GridCacheAffinityMapped} documentation.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 * @see GridCacheAffinityMapped
 * @see GridCacheAffinityMapper
 */
public interface GridCacheAffinity<K> extends Serializable {
    /**
     * Resets cache affinity to its initial state. This method will be called by
     * the system any time the affinity has been sent to remote node where
     * it has to be reinitialized. If your implementation of affinity function
     * has no initialization logic, leave this method empty.
     */
    public void reset();

    /**
     * Gets total number of partitions available. All caches should always provide
     * correct partition count which should be the same on all participating nodes.
     * Note that partitions should always be numbered from {@code 0} inclusively to
     * {@code N} exclusively without any gaps.
     *
     * @return Total partition count.
     */
    public int partitions();

    /**
     * Gets partition number for a given key starting from {@code 0}. Partitioned caches
     * should make sure that keys are about evenly distributed across all partitions
     * from {@code 0} to {@link #partitions() partition count} for best performance.
     * <p>
     * Note that for fully replicated caches it is possible to segment key sets among different
     * grid node groups. In that case each node group should return a unique partition
     * number. However, unlike partitioned cache, mappings of keys to nodes in
     * replicated caches are constant and a node cannot migrate from one partition
     * to another.
     *
     * @param key Key to get partition for.
     * @return Partition number for a given key.
     */
    public int partition(K key);

    /**
     * Gets affinity nodes for a partition. In case of replicated cache, all returned
     * nodes are updated in the same manner. In case of partitioned cache, the returned
     * list should contain only the primary and back up nodes with primary node being
     * always first.
     *
     * @param partition Partition to get nodes for.
     * @param nodes Nodes to choose from.
     * @return Affinity nodes for the given partition.
     */
    public Collection<GridRichNode> nodes(int partition, Collection<GridRichNode> nodes);
}
