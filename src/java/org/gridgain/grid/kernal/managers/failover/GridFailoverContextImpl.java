// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.managers.failover;

import org.gridgain.grid.*;
import org.gridgain.grid.kernal.*;
import org.gridgain.grid.kernal.managers.loadbalancer.*;
import org.gridgain.grid.spi.failover.*;
import org.gridgain.grid.typedef.internal.*;
import org.gridgain.grid.util.tostring.*;
import java.util.*;

/**
 * TODO: add file description.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridFailoverContextImpl implements GridFailoverContext {
    /** Grid task session. */
    private final GridTaskSessionImpl taskSes;

    /** Failed job result. */
    private final GridJobResult jobRes;

    /** Load balancing manager. */
    @GridToStringExclude private final GridLoadBalancerManager loadMgr;

    /**
     * Initializes failover context.
     *
     * @param taskSes Grid task session.
     * @param jobRes Failed job result.
     * @param loadMgr Load manager.
     */
    public GridFailoverContextImpl(GridTaskSessionImpl taskSes, GridJobResult jobRes,
        GridLoadBalancerManager loadMgr) {
        assert taskSes != null;
        assert jobRes != null;
        assert loadMgr != null;

        this.taskSes = taskSes;
        this.jobRes = jobRes;
        this.loadMgr = loadMgr;
    }

    /** {@inheritDoc} */
    @Override public GridTaskSession getTaskSession() {
        return taskSes;
    }

    /** {@inheritDoc} */
    @Override public GridJobResult getJobResult() {
        return jobRes;
    }

    /** {@inheritDoc} */
    @Override public GridNode getBalancedNode(List<GridNode> top) throws GridException {
        return loadMgr.getBalancedNode(taskSes, top, jobRes.getJob());
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridFailoverContextImpl.class, this);
    }
}
