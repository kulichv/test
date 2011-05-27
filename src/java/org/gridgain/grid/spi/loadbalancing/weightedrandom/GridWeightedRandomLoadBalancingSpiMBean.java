// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.loadbalancing.weightedrandom;

import org.gridgain.grid.spi.*;
import org.gridgain.grid.util.mbean.*;

/**
 * Management MBean for {@link GridWeightedRandomLoadBalancingSpi} SPI.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
@GridMBeanDescription("MBean that provides access to weighted random load balancing SPI configuration.")
public interface GridWeightedRandomLoadBalancingSpiMBean extends GridSpiManagementMBean {
    /**
     * Checks whether node weights are considered when doing
     * random load balancing.
     *
     * @return If {@code true} then random load is distributed according
     *      to node weights.
     */
    @GridMBeanDescription("Whether node weights are considered when doing random load balancing.")
    public boolean isUseWeights();

    /**
     * Gets weight of this node.
     *
     * @return Weight of this node.
     */
    @GridMBeanDescription("Weight of this node.")
    public int getNodeWeight();
}
