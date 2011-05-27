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
import org.gridgain.grid.lang.*;
import org.gridgain.grid.typedef.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static org.gridgain.grid.GridClosureCallMode.*;

/**
 * Example of how to collocate computations and data in GridGain using
 * {@link GridCacheAffinityMapped} annotation as opposed to direct API calls. This
 * example will first populate cache on some node where cache is available, and then
 * will send jobs to the nodes where keys reside and print out values for those
 * keys.
 * <p>
 * Remote nodes should always be started with configuration file which includes
 * cache: {@code 'ggstart.sh examples/config/spring-cache.xml'}. Local node can
 * be started with or without cache depending on whether community or enterprise
 * edition is used respectively.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridCacheAffinityExample1 {
    /**
     * Configuration file name.
     */
    //private static final String CONFIG = "examples/config/spring-cache-none.xml"; // Enterprise Edition.
    private static final String CONFIG = "examples/config/spring-cache.xml"; // Community Edition.

    /** Name of cache specified in spring configuration. */
    private static final String NAME = "partitioned";

    /**
     * Ensure singleton.
     */
    private GridCacheAffinityExample1() {
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
                Collection<String> keys = new ArrayList<String>('Z' - 'A' + 1);

                // Create collection of capital letters of English alphabet.
                for (char c = 'A'; c <= 'Z'; c++)
                    keys.add(Character.toString(c));

                // Populate cache.
                populateCache(g, keys);

                // Result map (ordered by key for readability).
                Map<String, String> results = new TreeMap<String, String>();

                // Bring computations to the nodes where the data resides (i.e. collocation).
                for (final String key : keys) {
                    String result = g.call(
                        BALANCE,
                        new GridCallable<String>() {
                            // This annotation allows to route job to the node
                            // where the key is cached.
                            @GridCacheAffinityMapped
                            public String affinityKey() {
                                return key;
                            }

                            // Specify name of cache to use for affinity.
                            @GridCacheName
                            public String cacheName() {
                                return NAME;
                            }

                            @Nullable @Override public String call() {
                                info("Executing affinity job for key: " + key);

                                // Get cache with name 'partitioned'.
                                GridCache<String, String> cache = g.cache(NAME);

                                // If cache is not defined at this point then it means that
                                // job was not routed by affinity.
                                if (cache == null) {
                                    info("Cache not found [nodeId=" + g.localNode().id() + ", cacheName=" + NAME + ']');

                                    return "Error";
                                }

                                // Check cache without loading the value.
                                return cache.peek(key);
                            }
                        }
                    );

                    results.put(key, result);
                }

                // Print out results.
                for (Map.Entry<String, String> e : results.entrySet())
                    info("Affinity job result for key '" + e.getKey() + "': " + e.getValue());
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
