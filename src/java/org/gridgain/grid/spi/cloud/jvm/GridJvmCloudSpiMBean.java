// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.cloud.jvm;

import org.gridgain.grid.spi.*;
import org.gridgain.grid.util.mbean.*;

/**
 * Management bean that provides general administrative and configuration information
 * about JVM-based cloud SPI.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
@GridMBeanDescription("MBean that provides access to JVM-based Cloud SPI configuration.")
public interface GridJvmCloudSpiMBean extends GridSpiManagementMBean {
    /**
     * Gets ID of the cloud.
     *
     * @return Cloud ID.
     */
    @GridMBeanDescription("Cloud ID.")
    public String getCloudId();

    /**
     * Gets frequency of state check in milliseconds.
     *
     * @return State check frequency in milliseconds.
     */
    @GridMBeanDescription("State check frequency.")
    public long getStateCheckFrequency();
}
