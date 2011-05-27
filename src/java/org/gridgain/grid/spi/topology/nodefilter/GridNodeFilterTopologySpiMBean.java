// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.topology.nodefilter;

import org.gridgain.grid.*;
import org.gridgain.grid.lang.*;
import org.gridgain.grid.spi.*;
import org.gridgain.grid.util.mbean.*;

/**
 * Management bean for {@link GridNodeFilterTopologySpi}.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
@GridMBeanDescription("MBean that provides access to node filter based topology SPI configuration.")
public interface GridNodeFilterTopologySpiMBean  extends GridSpiManagementMBean {
    /**
     * Gets node predicate filter for nodes to be included into topology.
     *
     * @return Node predicate filter for nodes to be included into topology.
     */
    @GridMBeanDescription("Node predicate filter for nodes to be included into topology.")
    public GridPredicate2<GridNode, GridTaskSession> getFilter();
}
