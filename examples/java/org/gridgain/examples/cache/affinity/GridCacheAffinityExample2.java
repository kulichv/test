// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.examples.cache.affinity;

import org.gridgain.grid.*;
import org.gridgain.grid.cache.*;
import org.gridgain.grid.cache.affinity.*;
import org.gridgain.grid.typedef.*;

import java.util.*;

import static org.gridgain.grid.GridClosureCallMode.*;

/**
 * This example works only on <b>Enterprise Edition.</b>
 * <p>
 * Demonstrates how to collocate computations and data in GridGain using
 * direct API calls as opposed to {@link GridCacheAffinityMapped} annotation. This
 * example will first populate cache on some nodes where cache is available, and then
 * will send jobs to the nodes where keys reside and print out values for those keys.
 * <p>
 * Note that for Enterprise Edition affinity routing is enabled for all caches. In
 * Community Edition affinity routing works only if the cache is configured locally.
 * <p>
 * Remote nodes should always be started with configuration file which includes
 * cache: {@code 'ggstart.sh examples/config/spring-cache.xml'}. Local node can
 * be started with or without cache depending on whether community or enterprise
 * edition is used respectively.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridCacheAffinityExample2 {
    /**
     * Configuration file name.
     */
    private static final String CONFIG = "examples/config/spring-cache.xml";

    /** Name of cache specified in spring configuration. */
    private static final String NAME = "partitioned";

    /**
     * Ensure singleton.
     */
    private GridCacheAffinityExample2() {
        // No-op.
    }

    /**
     * Executes cache affinity example.
     * <p>
     * Note that in case of {@code LOCAL} configuration,
     * since there is no distribution, values may come back as {@code nulls}.
     *
     * @param args Command line arguments
     * @throws Exception If failed.
     */
    public static void main(String[] args) throws Exception {
        G.in(args.length == 0 ? CONFIG : args[0], new CIX1<Grid>() {
            @Override public void applyx(final Grid g) throws GridException {
                if (!g.isEnterprise() && g.cache(NAME) == null)
                    throw new GridException("This example without having cache '" + NAME + "' started locally " +
                        "works only in Enterprise Edition.");

                Collection<String> keys = new ArrayList<String>('Z' - 'A' + 1);

                // Create collection of capital letters of English alphabet.
                for (char c = 'A'; c <= 'Z'; c++)
                    keys.add(Character.toString(c));

                // Populate cache with keys.
                populateCache(g, keys);

                // Map all keys to nodes. Note that community edition requires that
                // cache with given name is started on this node. Otherwise, use
                // enterprise edition to find out mapping on nodes that don't have
                // cache running.
                Map<UUID, Collection<String>> mappings = g.mapKeysToNodes(NAME, keys);

                // If on community edition, we have to get mappings from GridCache
                // directly as affinity mapping without have cache started
                // is not supported on community edition.
                if (mappings == null)
                    mappings = g.<String, String>cache(NAME).mapKeysToNodes(keys);

                for (Map.Entry<UUID, Collection<String>> mapping : mappings.entrySet()) {
                    UUID nodeId = mapping.getKey();

                    final Collection<String> mappedKeys = mapping.getValue();

                    GridRichNode node = g.node(nodeId);

                    if (node != null) {
                        // Bring computations to the nodes where the data resides (i.e. collocation).
                        // Note that this code does not account for node crashes, in which case you
                        // would get an exception and would have to remap the keys assigned to this node.
                        node.run(new CA() {
                            @Override public void apply() {
                                info("Executing affinity job for keys: " + mappedKeys);

                                // Get cache.
                                GridCache<String, String> cache = g.cache(NAME);

                                // If cache is not defined at this point then it means that
                                // job was not routed by affinity.
                                if (cache == null) {
                                    info("Cache not found [nodeId=" + g.localNode().id() + ", cacheName=" + NAME + ']');

                                    return;
                                }

                                // Check cache without loading the value.
                                for (String key : mappedKeys)
                                    info("Peeked at: " + cache.peek(key));
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * Populates cache with given keys. This method accounts for the case when
     * cache is not started on local node. In that case a job which populates
     * the cache will be sent to the node where cache is started.
     *
     * @param g Grid.
     * @param keys Keys to populate.
     * @throws GridException If failed.
     */
    private static void populateCache(final Grid g, Collection<String> keys) throws GridException {
        GridProjection prj = g.projectionForPredicate(F.cacheNodesForNames(NAME));

        // Give preference to local node.
        if (prj.nodes().contains(g.localNode()))
            prj = g.localNode();

        // Populate cache on some node (possibly this node) which has cache with given name started.
        // Note that CIX1 is a short type alias for GridInClosureX class. If you
        // find it too cryptic, you can use GridInClosureX class directly.
        prj.run(UNICAST, new CIX1<Collection<String>>() {
            @Override public void applyx(Collection<String> keys) throws GridException {
                info("Storing keys in cache: " + keys);

                GridCache<String, String> c = g.cache(NAME);

                for (String key : keys)
                    c.put(key, key.toLowerCase());
            }
        }, keys);
    }

    /**
     * Prints out message to standard out with given parameters.
     *
     * @param msg Message to print.
     */
    private static void info(String msg) {
        X.println(">>> " + msg);
    }
}
