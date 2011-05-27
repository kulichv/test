// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.topology.attributes;

import org.gridgain.grid.spi.*;
import org.gridgain.grid.util.mbean.*;
import java.util.*;

/**
 * Management bean for {@link GridAttributesTopologySpi}.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
@GridMBeanDescription("MBean that provides access to attributes based topology SPI configuration.")
public interface GridAttributesTopologySpiMBean extends GridSpiManagementMBean {
    /**
     * Gets attribute names and values that nodes should have to be included
     * in topology.
     * <p>
     * Default value is {@code null} which means all nodes will be added.
     *
     * @return Map of node attributes.
     */
    @GridMBeanDescription("Attribute names and values that nodes should have to be included in topology.")
    public Map<String, ?> getAttributes();
}
