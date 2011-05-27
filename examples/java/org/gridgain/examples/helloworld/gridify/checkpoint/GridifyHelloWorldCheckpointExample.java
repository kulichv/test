// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.examples.helloworld.gridify.checkpoint;

import org.gridgain.examples.helloworld.gridify.spring.*;
import org.gridgain.grid.*;
import org.gridgain.grid.gridify.*;
import org.gridgain.grid.gridify.aop.spring.*;
import org.gridgain.grid.typedef.*;
import java.util.*;

/**
 * Demonstrates a simple use of GridGain grid with
 * {@code Gridify} annotation and custom grid task. It also shows
 * the usage of checkpoints.
 * <p>
 * Example may take configuration file as the only parameter. By default it
 * uses sharedfs checkpoint implementation.
 * <p>
 * String "Hello World" is passed as an argument to
 * {@link #sayIt(String)} method. Since this method is annotated with
 * {@code @Gridify} annotation it is automatically grid-enabled. The
 * {@link GridifyHelloWorldCheckpointTask} task responsible for split and
 * reduce logic will do the following:
 * <ol>
 * <li>Save checkpoint with key '{@code fail}' and value '{@code true}'.</li>
 * <li>Pass the passed in string as an argument into remote job for execution.</li>
 * <li>
 *   The job will check the value of checkpoint with key '{@code fail}'. If it
 *   is {@code true}, then it will set it to {@code false} and throw
 *   exception to simulate a failure. If it is {@code false}, then
 *   it will execute the grid-enabled method.
 * </li>
 * </ol>
 * Note that when job throws an exception it will be treated as a failure
 * by {@link GridifyHelloWorldCheckpointTask#result(GridJobResult,List)} method
 * which will return {@link GridJobResultPolicy#FAILOVER} policy. This will
 * cause the job to automatically failover to another node for execution.
 * The new job will simply print out the argument passed in.
 * <p>
 * The possible outcome will look as following:
 * <pre class="snippet">
 * NODE #1 (failure occurred on this node)
 * Exception:
 * ----------
 * >>> Type: org.gridgain.grid.GridException
 * >>> Message: Example job exception.
 * >>> Documentation: http://www.gridgain.com/product.html
 * >>> Stack trace:
 * >>>     at org.gridgain.examples.helloworld.gridify.failover.GridifyHelloWorldFailoverTask$1.execute(GridifyHelloWorldFailoverTask.java:57)
 * >>>     at org.gridgain.grid.kernal.processors.job.GridJobWorker.body(GridJobWorker.java:285)
 * >>>     at org.gridgain.grid.util.runnable.GridRunnable$1.run(GridRunnable.java:125)
 * >>>     at java.util.concurrent.Executors$RunnableAdapter.apply(Executors.java:417)
 * >>>     at java.util.concurrent.FutureTask$Sync.innerRun(FutureTask.java:269)
 * >>>     at java.util.concurrent.FutureTask.run(FutureTask.java:123)
 * >>>     at org.gridgain.grid.util.runnable.GridRunnable.run(GridRunnable.java:175)
 * >>>     at java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:650)
 * >>>     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:675) *
 *
 * NODE #2 (job was failed over to this node)
 * [15:15:57,549][INFO ][GridFailoverManager] Resolving failover [jobRes=org.gridgain.grid.kernal.GridJobResultImpl ... ]
 * >>>
 * >>> Printing 'Hello World' on this node from grid-enabled method.
 * >>>
 * </pre>
 * <p>
 * <h1 class="header">Starting Remote Nodes</h1>
 * To try this example you should (but don't have to) start remote grid instances.
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
 * <p>
 * <h1 class="header">AOP Configuration</h1>
 * In order for this example to execute on the grid, any of the following
 * AOP configurations must be provided (only on the task initiating node).
 * <h2 class="header">Jboss AOP</h2>
 * The following configuration needs to be applied to enable JBoss byte code
 * weaving. Note that GridGain is not shipped with JBoss and necessary
 * libraries will have to be downloaded separately (they come standard
 * if you have JBoss installed already):
 * <ul>
 * <li>
 *      The following JVM configuration must be present:
 *      <ul>
 *      <li>{@code -javaagent:[path to jboss-aop-jdk50-4.x.x.jar]}</li>
 *      <li>{@code -Djboss.aop.class.path=[path to gridgain.jar]}</li>
 *      <li>{@code -Djboss.aop.exclude=org,com -Djboss.aop.include=org.gridgain.examples}</li>
 *      </ul>
 * </li>
 * <li>
 *      The following JARs should be in a classpath:
 *      <ul>
 *      <li>{@code javassist-3.x.x.jar}</li>
 *      <li>{@code jboss-aop-jdk50-4.x.x.jar}</li>
 *      <li>{@code jboss-aspect-library-jdk50-4.x.x.jar}</li>
 *      <li>{@code jboss-common-4.x.x.jar}</li>
 *      <li>{@code trove-1.0.2.jar}</li>
 *      </ul>
 * </li>
 * </ul>
 * <p>
 * <h2 class="header">AspectJ AOP</h2>
 * The following configuration needs to be applied to enable AspectJ byte code
 * weaving.
 * <ul>
 * <li>
 *      JVM configuration should include:
 *      {@code -javaagent:${GRIDGAIN_HOME}/libs/aspectjweaver-1.6.8.jar}
 * </li>
 * <li>
 *      Classpath should contain the {@code ${GRIDGAIN_HOME}/config/aop/aspectj} folder.
 * </li>
 * </ul>
 * <p>
 * <h2 class="header">Spring AOP</h2>
 * Spring AOP framework is based on dynamic proxy implementation and doesn't require
 * any specific runtime parameters for online weaving. All weaving is on-demand and should
 * be performed by calling method {@link GridifySpringEnhancer#enhance(Object)} for the object
 * that has method with {@link Gridify} annotation.
 * <p>
 * Note that this method of weaving is rather inconvenient and AspectJ or JBoss AOP are
 * more recommended. Spring AOP can be used in situation when code augmentation is
 * undesired and cannot be used. It also allows for very fine grained control of what gets
 * weaved.
 * <p>
 * NOTE: this example as is cannot be used with Spring AOP. To see an example of grid-enabling
 * with Spring AOP refer to {@link GridifySpringHelloWorldExample} example.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public final class GridifyHelloWorldCheckpointExample {
    /**
     * Enforces singleton.
     */
    private GridifyHelloWorldCheckpointExample() {
        // No-op.
    }

    /**
     * Method is grid-enabled with {@link Gridify} annotation and will be
     * executed on the grid. Simply prints out the argument passed in.
     *
     * @param phrase Phrase string to print.
     * @return Number of characters in the phrase.
     */
    @Gridify(taskClass = GridifyHelloWorldCheckpointTask.class, timeout = 60000)
    public static int sayIt(String phrase) {
        // Simply print out the argument.
        X.println(">>>");
        X.println(">>> Printing '" + phrase + "' on this node from grid-enabled method.");
        X.println(">>>");

        return phrase.length();
    }

    /**
     * Execute {@code HelloWorld} example grid-enabled with {@code Gridify} annotation.
     *
     * @param args Command line arguments, none required but user may
     *      set configuration file path as the only parameter. For GigaSpaces
     *      checkpoint SPI user should pass {@code "examples/config/gigaspaces.xml"}
     *      as VM configuration parameter.
     * @throws GridException If example execution failed.
     */
    public static void main(String[] args) throws GridException {
        if (args.length == 0) {
            G.start();
        }
        else {
            G.start(args[0]);
        }

        try {
            // This method will be executed on a remote grid node.
            int phraseLen = sayIt("Hello World");

            X.println(">>>");

            if (phraseLen < 0) {
                X.println(">>> Gridify \"Hello World\" finished with wrong result (do you have AOP enabled?).");
                X.println(">>> Checkpoint was not found. Make sure that Checkpoint SPI on all nodes ");
                X.println(">>> has the same configuration (the 'directoryPath' configuration parameter for");
                X.println(">>> GridSharedFsCheckpointSpi on all nodes should point to the same location).");
            }
            else {
                X.println(">>> Finished executing Gridify \"Hello World\" example with checkpoints.");
                X.println(">>> Total number of characters in the phrase is '" + phraseLen + "'.");
                X.println(">>> You should see exception stack trace from failed job on one node.");
                X.println(">>> Failed job will be failed over to another node.");
                X.println(">>> You should see print out of 'Hello World' on another node.");
                X.println(">>> Check all nodes for output (this node is also part of the grid).");
            }

            X.println(">>>");
        }
        finally {
            G.stop(true);
        }
    }
}
