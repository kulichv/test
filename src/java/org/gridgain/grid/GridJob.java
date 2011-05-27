// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid;

import org.gridgain.grid.resources.*;
import org.gridgain.grid.spi.collision.*;
import org.jetbrains.annotations.*;
import java.io.*;
import java.util.*;

/**
 * Defines executable unit for {@link GridTask}.
 * <h1 class="header">Description</h1>
 * Grid job is an executable unit of {@link GridTask}. Grid task gets split into jobs
 * when {@link GridTask#map(List, Object)} method is called. This method returns
 * all jobs for the task mapped to their corresponding grid nodes for execution. Grid
 * will then serialize this jobs and send them to requested nodes for execution.
 * When a node receives a request to execute a job, the following sequence of events
 * takes place:
 * <ol>
 * <li>
 *      Job gets put on waiting list which is passed to underlying
 *      {@link GridCollisionSpi} SPI.
 * </li>
 * <li>
 *      The Collision SPI will decide one of the following scheduling policies:
 *      <ul>
 *      <li>
 *          Job will be kept on waiting list. In this case, job will not get a
 *          chance to execute until next time the Collision SPI is called.
 *      </li>
 *      <li>
 *          Job will be moved to active list. In this case system will proceed
 *          with job execution.
 *      </li>
 *      <li>
 *          Job will be rejected. In this case the {@link GridJobResult} passed into
 *          {@link GridTask#result(GridJobResult, List)} method will contain
 *          {@link GridExecutionRejectedException} exception. If you are using any
 *          of the task adapters shipped with GridGain, then job will be failed
 *          over automatically for execution on another node.
 *      </li>
 *      </ul>
 * </li>
 * <li>
 *      For activated jobs, an instance of distributed task session (see {@link GridTaskSession})
 *      will be injected.
 * </li>
 * <li>
 *      System will execute the job by calling {@link GridJob#execute()} method.
 * </li>
 * <li>
 *      If job gets cancelled while executing then {@link GridJob#cancel()}
 *      method will be called. Note that just like with {@link Thread#interrupt()}
 *      method, grid job cancellation serves as a hint that a job should stop
 *      executing or exhibit some other user defined behavior. Generally it is
 *      up to a job to decide whether it wants to react to cancellation or
 *      ignore it. Job cancellation can happen for several reasons:
 *      <ul>
 *      <li>Collision SPI cancelled an active job.</li>
 *      <li>Parent task has completed without waiting for this job's result.</li>
 *      <li>User cancelled task by calling {@link GridTaskFuture#cancel()} method.</li>
 *      </ul>
 * </li>
 * <li>
 *      Once job execution is complete, the return value will be sent back to parent
 *      task and will be passed into {@link GridTask#result(GridJobResult, List)}
 *      method via {@link GridJobResult} instance. If job execution resulted
 *      in a checked exception, then {@link GridJobResult#getException()} method
 *      will contain that exception. If job execution threw a runtime exception
 *      or error, then it will be wrapped into {@link GridUserUndeclaredException}
 *      exception.
 * </li>
 * </ol>
 * <p>
 * <h1 class="header">Resource Injection</h1>
 * Grid job implementation can be injected using IoC (dependency injection) with
 * grid resources. Both, field and method based injection are supported.
 * The following grid resources can be injected:
 * <ul>
 * <li>{@link GridTaskSessionResource}</li>
 * <li>{@link GridJobContextResource}</li>
 * <li>{@link GridInstanceResource}</li>
 * <li>{@link GridLoggerResource}</li>
 * <li>{@link GridHomeResource}</li>
 * <li>{@link GridExecutorServiceResource}</li>
 * <li>{@link GridLocalNodeIdResource}</li>
 * <li>{@link GridMBeanServerResource}</li>
 * <li>{@link GridMarshallerResource}</li>
 * <li>{@link GridSpringApplicationContextResource}</li>
 * <li>{@link GridSpringResource}</li>
 * </ul>
 * Refer to corresponding resource documentation for more information.
 * <p>
 * <h1 class="header">GridJobAdapter</h1>
 * GridGain comes with convenience {@link GridJobAdapterEx} adapter that provides
 * default empty implementation for {@link GridJob#cancel()} method and also
 * allows user to set and get job argument, if there is one.
 * <p>
 * <h1 class="header">Distributed Session Attributes</h1>
 * Jobs can communicate with parent task and with other job siblings from the same
 * task by setting session attributes (see {@link GridTaskSession}). Other jobs
 * can wait for an attribute to be set either synchronously or asynchronously.
 * Such functionality allows jobs to synchronize their execution with other jobs
 * at any point and can be useful when other jobs within task need to be made aware
 * of certain event or state change that occurred during job execution.
 * <p>
 * Distributed task session can be injected into {@link GridJob} implementation
 * using {@link GridTaskSessionResource @GridTaskSessionResource} annotation.
 * Both, field and method based injections are supported. Refer to
 * {@link GridTaskSession} documentation for more information on session functionality.
 * <p>
 * <h1 class="header">Saving Checkpoints</h1>
 * Long running jobs may wish to save intermediate checkpoints to protect themselves
 * from failures. There are three checkpoint management methods:
 * <ul>
 * <li>{@link GridTaskSession#saveCheckpoint(String, Object, GridTaskSessionScope , long)}</li>
 * <li>{@link GridTaskSession#loadCheckpoint(String)}</li>
 * <li>{@link GridTaskSession#removeCheckpoint(String)}</li>
 * </ul>
 * Jobs that utilize checkpoint functionality should attempt to load a check
 * point at the beginning of execution. If a {@code non-null} value is returned,
 * then job can continue from where it failed last time, otherwise it would start
 * from scratch. Throughout it's execution job should periodically save its
 * intermediate state to avoid starting from scratch in case of a failure.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public interface GridJob extends Serializable {
    /**
     * This method is called when when system detects that completion of this
     * job can no longer alter the overall outcome (for example, when parent task
     * has already reduced the results). Job is also cancelled when
     * {@link GridTaskFuture#cancel()} is called.
     * <p>
     * Note that job cancellation is only a hint, and just like with
     * {@link Thread#interrupt()}  method, it is really up to the actual job
     * instance to gracefully finish execution and exit.
     */
    public void cancel();

    /**
     * Executes this job.
     *
     * @return Job execution result (possibly {@code null}). This result will be returned
     *      in {@link GridJobResult#getData()} method passed into
     *      {@link GridTask#result(GridJobResult, List)} method into task on caller node.
     * @throws GridException If job execution caused an exception. This exception will be
     *      returned in {@link GridJobResult#getException()} method passed into
     *      {@link GridTask#result(GridJobResult, List)} method into task on caller node.
     *      If execution produces a {@link RuntimeException} or {@link Error}, then
     *      it will be wrapped into {@link GridException}.
     */
    @Nullable public Object execute() throws GridException;
}
