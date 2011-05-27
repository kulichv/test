// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.loadbalancing.roundrobin;

import org.gridgain.grid.*;
import org.gridgain.grid.events.*;
import org.gridgain.grid.logger.*;
import org.gridgain.grid.resources.*;
import org.gridgain.grid.spi.*;
import org.gridgain.grid.spi.loadbalancing.*;
import org.gridgain.grid.typedef.internal.*;
import org.jetbrains.annotations.*;
import java.util.*;
import java.util.concurrent.*;

import static org.gridgain.grid.GridEventType.*;

/**
 * This SPI iterates through nodes in round-robin fashion and pick the next
 * sequential node. Two modes of operation are supported: per-task and global
 * (see {@link #setPerTask(boolean)} configuration).
 * <p>
 * When configured in per-task mode, implementation will pick a random starting
 * node at the beginning of every task execution and then sequentially iterate through all
 * nodes in topology starting from the picked node. This is the default configuration
 * and should fit most of the use cases as it provides a fairly well-distributed
 * split and also ensures that jobs within a single task are spread out across
 * nodes to the maximum. For cases when split size is equal to the number of nodes,
 * this mode guarantees that all nodes will participate in the split.
 * <p>
 * When configured in global mode, a single sequential queue of nodes is maintained for
 * all tasks and the next node in the queue is picked every time. In this mode (unlike in
 * {@code per-task} mode) it is possible that even if split size may be equal to the
 * number of nodes, some jobs within the same task will be assigned to the same node if
 * multiple tasks are executing concurrently.
 * <h1 class="header">Coding Example</h1>
 * If you are using {@link GridTaskSplitAdapter} then load balancing logic
 * is transparent to your code and is handled automatically by the adapter.
 * Here is an example of how your task will look:
 * <pre name="code" class="java">
 * public class MyFooBarTask extends GridTaskSplitAdapter&lt;Object, Object&gt; {
 *    &#64;Override
 *    protected Collection&lt;? extends GridJob&gt; split(int gridSize, Object arg) throws GridException {
 *        List&lt;MyFooBarJob&gt; jobs = new ArrayList&lt;MyFooBarJob&gt;(gridSize);
 *
 *        for (int i = 0; i &lt; gridSize; i++) {
 *            jobs.add(new MyFooBarJob(arg));
 *        }
 *
 *        // Node assignment via load balancer
 *        // happens automatically.
 *        return jobs;
 *    }
 *    ...
 * }
 * </pre>
 * If you need more fine-grained control over how some jobs within task get mapped to a node
 * and use affinity load balancing for some other jobs within task, then you should use
 * {@link GridTaskAdapter}. Here is an example of how your task will look. Note that in this
 * case we manually inject load balancer and use it to pick the best node. Doing it in
 * such way would allow user to map some jobs manually and for others use load balancer.
 * <pre name="code" class="java">
 * public class MyFooBarTask extends GridTaskAdapter&lt;String, String&gt; {
 *    // Inject load balancer.
 *    &#64;GridLoadBalancerResource
 *    GridLoadBalancer balancer;
 *
 *    // Map jobs to grid nodes.
 *    public Map&lt;? extends GridJob, GridNode&gt; map(List&lt;GridNode&gt; subgrid, String arg) throws GridException {
 *        Map&lt;MyFooBarJob, GridNode&gt; jobs = new HashMap&lt;MyFooBarJob, GridNode&gt;(subgrid.size());
 *
 *        // In more complex cases, you can actually do
 *        // more complicated assignments of jobs to nodes.
 *        for (int i = 0; i &lt; subgrid.size(); i++) {
 *            // Pick the next best balanced node for the job.
 *            jobs.put(new MyFooBarJob(arg), balancer.getBalancedNode())
 *        }
 *
 *        return jobs;
 *    }
 *
 *    // Aggregate results into one compound result.
 *    public String reduce(List&lt;GridJobResult&gt; results) throws GridException {
 *        // For the purpose of this example we simply
 *        // concatenate string representation of every
 *        // job result
 *        StringBuilder buf = new StringBuilder();
 *
 *        for (GridJobResult res : results) {
 *            // Append string representation of result
 *            // returned by every job.
 *            buf.append(res.getData().string());
 *        }
 *
 *        return buf.string();
 *    }
 * }
 * </pre>
 * <p>
 * <h1 class="header">Configuration</h1>
 * In order to use this load balancer, you should configure your grid instance
 * to use {@code GridRoundRobinLoadBalancingSpi} either from Spring XML file or
 * directly. The following configuration parameters are supported:
 * <h2 class="header">Mandatory</h2>
 * This SPI has no mandatory configuration parameters.
 * <h2 class="header">Optional</h2>
 * The following configuration parameters are optional:
 * <ul>
 * <li>
 *      Flag that indicates whether to use {@code per-task} or global
 *      round-robin modes described above (see {@link #setPerTask(boolean)}).
 * </li>
 * </ul>
 * Below is Java configuration example:
 * <pre name="code" class="java">
 * GridRandomLoadBalancingSpi = new GridRandomLoadBalancingSpi();
 *
 * // Configure SPI to use global round-robin mode.
 * spi.setPerTask(false);
 *
 * GridConfigurationAdapter cfg = new GridConfigurationAdapter();
 *
 * // Override default load balancing SPI.
 * cfg.setLoadBalancingSpi(spi);
 *
 * // Starts grid.
 * G.start(cfg);
 * </pre>
 * Here is how you can configure {@code GridRandomLoadBalancingSpi} using Spring XML configuration:
 * <pre name="code" class="xml">
 * &lt;property name="loadBalancingSpi"&gt;
 *     &lt;bean class="org.gridgain.grid.spi.loadBalancing.roundrobin.GridRoundRobinLoadBalancingSpi"&gt;
 *         &lt;!-- Set to global round-robin mode. --&gt;
 *         &lt;property name="perTask" value="false"/&gt;
 *     &lt;/bean&gt;
 * &lt;/property&gt;
 * </pre>
 * <p>
 * <img src="http://www.gridgain.com/images/spring-small.png">
 * <br>
 * For information about Spring framework visit <a href="http://www.springframework.org/">www.springframework.org</a>
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
@GridSpiInfo(
    author = "GridGain Systems, Inc.",
    url = "www.gridgain.com",
    email = "support@gridgain.com",
    version = "3.0.9c.27052011")
@GridSpiMultipleInstancesSupport(true)
public class GridRoundRobinLoadBalancingSpi extends GridSpiAdapter implements GridLoadBalancingSpi,
    GridRoundRobinLoadBalancingSpiMBean {
    /** Grid logger. */
    @GridLoggerResource private GridLogger log;

    /** */
    private GridRoundRobinGlobalLoadBalancer balancer;

    /** */
    private boolean isPerTask = true;

    /** */
    private final Map<UUID, GridRoundRobinPerTaskLoadBalancer> perTaskBalancers =
        new ConcurrentHashMap<UUID, GridRoundRobinPerTaskLoadBalancer>();

    /** Event listener. */
    private final GridLocalEventListener lsnr = new GridLocalEventListener() {
        @Override public void onEvent(GridEvent evt) {
            if (evt.type() == EVT_TASK_FAILED ||
                evt.type() == EVT_TASK_FINISHED) {
                perTaskBalancers.remove(((GridTaskEvent)evt).taskSessionId());
            }
            else if (evt.type() == EVT_JOB_MAPPED) {
                GridRoundRobinPerTaskLoadBalancer balancer =
                    perTaskBalancers.get(((GridJobEvent)evt).taskSessionId());

                if (balancer != null) {
                    balancer.onMapped();
                }
            }
        }
    };

    /** {@inheritDoc} */
    @Override public boolean isPerTask() {
        return isPerTask;
    }

    /**
     * Configuration parameter indicating whether a new round robin order should be
     * created for every task. If {@code true} then load balancer is guaranteed
     * to iterate through nodes sequentially for every task - so as long as number
     * of jobs is less than or equal to the number of nodes, jobs are guaranteed to
     * be assigned to unique nodes. If {@code false} then one round-robin order
     * will be maintained for all tasks, so when tasks execute concurrently, it
     * is possible for more than one job within task to be assigned to the same
     * node.
     * <p>
     * Default is {@code true}.
     *
     * @param isPerTask Configuration parameter indicating whether a new round robin order should
     *      be created for every task. Default is {@code true}.
     */
    @GridSpiConfiguration(optional = true)
    public void setPerTask(boolean isPerTask) {
        this.isPerTask = isPerTask;
    }

    /** {@inheritDoc} */
    @Override public void spiStart(@Nullable String gridName) throws GridSpiException {
        startStopwatch();

        if (log.isDebugEnabled()) {
            log.debug(configInfo("isPerTask", isPerTask));
        }

        registerMBean(gridName, this, GridRoundRobinLoadBalancingSpiMBean.class);

        balancer = new GridRoundRobinGlobalLoadBalancer(log);

        // Ack ok start.
        if (log.isDebugEnabled()) {
            log.debug(startInfo());
        }
    }

    /** {@inheritDoc} */
    @Override public void spiStop() throws GridSpiException {
        balancer = null;

        perTaskBalancers.clear();

        unregisterMBean();

        // Ack ok stop.
        if (log.isDebugEnabled()) {
            log.debug(stopInfo());
        }
    }

    /** {@inheritDoc} */
    @Override public void onContextInitialized(GridSpiContext spiCtx) throws GridSpiException {
        super.onContextInitialized(spiCtx);

        if (!isPerTask) {
            balancer.onContextInitialized(spiCtx);
        }
        else {
            getSpiContext().addLocalEventListener(lsnr, EVT_TASK_FAILED, EVT_TASK_FINISHED, EVT_JOB_MAPPED);
        }
    }

    /** {@inheritDoc} */
    @Override public void onContextDestroyed() {
        if (!isPerTask) {
            if (balancer != null) {
                balancer.onContextDestroyed();
            }
        }
        else {
            GridSpiContext spiCtx = getSpiContext();

            if (spiCtx != null) {
                spiCtx.removeLocalEventListener(lsnr);
            }
        }

        super.onContextDestroyed();
    }

    /** {@inheritDoc} */
    @Override public GridNode getBalancedNode(GridTaskSession ses, List<GridNode> top, GridJob job)
        throws GridException {
        A.notNull(ses, "ses", top, "top");

        if (isPerTask) {
            // Note that every session operates from single thread which
            // allows us to use concurrent map and avoid synchronization.
            GridRoundRobinPerTaskLoadBalancer taskBalancer = perTaskBalancers.get(ses.getId());

            if (taskBalancer == null) {
                perTaskBalancers.put(ses.getId(), taskBalancer = new GridRoundRobinPerTaskLoadBalancer());
            }

            return taskBalancer.getBalancedNode(top);
        }

        return balancer.getBalancedNode(top);
    }

    /**
     * THIS METHOD IS USED ONLY FOR TESTING.
     *
     * @param ses Task session.
     * @return Internal list of nodes.
     */
    List<UUID> getNodeIds(GridTaskSession ses) {
        if (isPerTask) {
            GridRoundRobinPerTaskLoadBalancer balancer = perTaskBalancers.get(ses.getId());

            if (balancer == null) {
                return Collections.emptyList();
            }

            List<UUID> ids = new ArrayList<UUID>();

            for (GridNode node : balancer.getNodes()) {
                ids.add(node.id());
            }

            return ids;
        }

        return balancer.getNodeIds();
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridRoundRobinLoadBalancingSpi.class, this);
    }
}
