// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.examples.messaging;

import org.gridgain.grid.*;
import org.gridgain.grid.typedef.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.zip.*;

/**
 * Demonstrates various messaging APIs. This example implements a classic
 * streaming processing problem for continues processing of large data sets
 * on the cloud or grid.
 * <p>
 * <h1 class="header">Starting Remote Nodes</h1>
 * To try this example you need to start at least one remote grid instance.
 * You can start as many as you like by executing the following script:
 * <pre class="snippet">{GRIDGAIN_HOME}/bin/ggstart.{bat|sh}</pre>
 * Once remote instances are started, you can execute this example from
 * Eclipse, IntelliJ IDEA, or NetBeans (and any other Java IDE) by simply hitting run
 * button. You will see that all nodes discover each other and
 * some of the nodes will participate in task execution (check node
 * output).
 * <p>
 * <h1 class="header">XML Configuration</h1>
 * If no specific configuration is provided, GridGain will start with
 * all defaults. For information about GridGain default configuration
 * refer to {@link GridFactory} documentation. If you would like to
 * try out different configurations you should pass a path to Spring
 * configuration file as 1st command line argument into this example.
 * The path can be relative to {@code GRIDGAIN_HOME} environment variable.
 * You should also pass the same configuration file to all other
 * grid nodes by executing startup script as follows (you will need
 * to change the actual file name):
 * <pre class="snippet">{GRIDGAIN_HOME}/bin/ggstart.{bat|sh} examples/config/specific-config-file.xml</pre>
 * <p>
 * GridGain examples come with multiple configuration files you can try.
 * All configuration files are located under {@code GRIDGAIN_HOME/examples/config}
 * folder. You are free to try any of these configurations, but whenever
 * using 3rd party configurations, such as JBoss JMS, ActiveMQ JMS, Sun MQ JMS, or GigaSpaces,
 * make sure to download these respective products and include all the necessary
 * libraries into classpath at node startup. All these libraries are already
 * specified in commented format in {@code GRIDGAIN_HOME/bin/setenv.{bat|sh}} files
 * which get executed automatically by GridGain startup scripts. You can simply
 * uncomment the necessary classpath portions as you need.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridMessagingNodeLocalExample {
    /**
     * Enforces singleton.
     */
    private GridMessagingNodeLocalExample() {
        // No-op.
    }

    /**
     * This example demonstrates a bit contrived but useful example of how to
     * combine node locals and closure for powerful distributed processing pattern.
     * <p>
     * This example runs on two or more nodes. Here's what this is going to do:
     * <ol>
     * <li>
     *      Local node will pick up random number of message that it will later send
     *      to the remote node.
     * </li>
     * <li>
     *      Local node will configure the remote node to receive these messages and calculate
     *      their CRC32 value which will be stored in remote's node local storage.
     * </li>
     * <li>
     *      Local node then will send predetermined number of message and remote node
     *      will start receiving and processing them.
     * </li>
     * <li>
     *      Local node will then wait till all messages are eventually received by the
     *      remote node and successfully processed.
     * </li>
     * <li>
     *      Local node will finally retrieve the CRC32 value from the remote node.
     * </li>
     * </ol>
     * This example illustrates classic streaming processing concept and how it can be easily
     * implemented using GridGain.
     *
     * @param args Command line arguments (none required).
     * @throws GridException Thrown in case of any errors.
     */
    public static void main(String[] args) throws GridException {
        // Typedefs:
        // ---------
        // G -> GridFactory
        // CO -> GridOutClosure
        // CA -> GridAbsClosure
        // F -> GridFunc

        try {
            final Grid g = args.length == 0 ? G.start() : G.start(args[0]);

            if (g.nodes().size() < 2) {
                System.err.println("Two or more nodes are needed.");

                return;
            }

            // Local node.
            final GridRichNode loc = g.localNode();

            // Pick random remote node.
            GridRichNode rmt = F.rand(g.remoteNodes());

            // Number of messages to process.
            final int MSG_NUM = 1 + new Random().nextInt(100);

            // Configure listener on remote node.
            rmt.run(new CA() {
                // Method 'apply' will be executed on remote node.
                @Override public void apply() {
                    final CountDownLatch latch = new CountDownLatch(1);

                    // Store latch reference in node local storage.
                    g.nodeLocal().put("latch", latch);

                    loc.listen(new GridListenActor<String>() {
                        private CRC32 crc32 = new CRC32();

                        private int cnt;

                        @Override protected void receive(UUID nodeId, String recvMsg) throws Throwable {
                            X.println("Calculating for: " + recvMsg);

                            crc32.update(recvMsg.getBytes());

                            if (++cnt == MSG_NUM) {
                                stop();

                                // Store final CRC32 value in node local storage.
                                g.nodeLocal().put("crc32", crc32.getValue());

                                // Drop the latch.
                                latch.countDown();
                            }
                            else {
                                skip();
                            }
                        }
                    });
                }
            });

            // Send all messages.
            for (int i = 0; i < MSG_NUM; i++) {
                rmt.send("Message " + i);
            }

            // Wait for all messages to be successfully processed
            // on the remote node.
            rmt.nodeLocalRun("latch", F.ciInvoke("await"));

            // Retrieve and print final CRC32 value from the remote node.
            // For example's sake we do it in a separate call (extra network trip).
            X.println("CRC32: " + rmt.nodeLocalGet("crc32"));
        }
        finally {
            G.stop(true);
        }
    }
}
