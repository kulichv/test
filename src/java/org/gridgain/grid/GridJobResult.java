// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid;

import java.util.*;

/**
 * Result of remote job which gets passed into {@link GridTask#result(GridJobResult, List)}
 * method.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public interface GridJobResult {
    /**
     * Gets job context. Use job context to access job unique ID or to get/set
     * jobs attributes. Context is attached to a job and travels with it wherever
     * it goes. For example, if a job gets failed-over from one node to another,
     * then its context will be failed over with it and all attributes that
     * were set on the job on the first node will be available on the new node.
     *
     * @return Job context.
     */
    public GridJobContext getJobContext();

    /**
     * Gets data returned by remote job if it didn't fail. This data is the
     * object returned from {@link GridJob#execute()} method.
     * <p>
     * Note that if task is annotated with {@link GridTaskNoResultCache} annotation,
     * then job results will not be cached and will be available only in
     * {@link GridTask#result(GridJobResult, List)} method for every individual job,
     * but not in {@link GridTask#reduce(List)} method. This feature was added to
     * avoid excessive storing of overly large results.
     *
     * @param <T> Type of the return value returning from {@link GridJob#execute()} method.
     * @return Data returned by remote job's {@link GridJob#execute()} method if it didn't fail.
     */
    public <T> T getData();

    /**
     * Gets exception produced by execution of remote job, or {@code null} if
     * remote execution finished normally and did not produce any exceptions.
     *
     * @return {@link GridException} produced by execution of remote job or {@code null} if
     *      no exception was produced.
     *      <p>
     *      Note that if remote job resulted in {@link RuntimeException}
     *      or {@link Error} then they will be wrapped into {@link GridUserUndeclaredException}
     *      returned by this method.
     *      <p>
     *      If job on remote node was rejected (cancelled while it was on waiting queue), then
     *      {@link GridExecutionRejectedException} will be returned.
     *      <p>
     *      If node on which job was computing failed, then {@link GridTopologyException} is
     *      returned.
     */
    public GridException getException();

    /**
     * Gets local instance of remote job returned by {@link GridTask#map(List, Object)} method.
     *
     * @param <T> Type of {@link GridJob} that was sent to remote node.
     * @return Local instance of remote job returned by {@link GridTask#map(List, Object)} method.
     */
    public <T extends GridJob> T getJob();

    /**
     * Gets node this job executed on.
     *
     * @return Node this job executed on.
     */
    public GridNode getNode();

    /**
     * Gets job cancellation status. Returns {@code true} if job received cancellation
     * request on remote node. Note that job, after receiving cancellation request, will still
     * need to finish and return, hence {@link #getData()} method may contain
     * execution result even if the job was canceled.
     * <p>
     * Job can receive cancellation request if the task was explicitly cancelled
     * from future (see {@link GridTaskFuture#cancel()}) or if task completed prior
     * to getting results from all remote jobs.
     *
     * @return {@code true} if job received cancellation request and {@code false} otherwise.
     */
    public boolean isCancelled();
}
