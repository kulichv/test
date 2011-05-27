// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.discovery;

import org.gridgain.grid.*;

import java.lang.annotation.*;

/**
 * This annotation is for all implementations of {@link GridDiscoverySpi} that support
 * proper node ordering. This includes:
 * <ul>
 * <li>
 *  Every node gets an order number assigned to it which is provided via {@link GridNode#order()}
 *  method. There is no requirement about order value other than that nodes that join grid
 *  at later point of time have order values greater than previous nodes.
 * </li>
 * <li>
 *  All {@link GridEventType#EVT_NODE_JOINED} events come in proper order. This means that all
 *  listeners to discovery events will receive discovery notifications in proper order.
 * </li>
 * <li></li>
 * </ul>
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface GridDiscoverySpiOrderSupport {
    /**
     * Whether or not target SPI supports node startup order.
     */
    @SuppressWarnings({"JavaDoc"}) public boolean value();
}
