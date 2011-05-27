// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid;

import org.gridgain.grid.events.*;
import org.gridgain.grid.lang.*;
import org.gridgain.grid.lang.utils.*;

import java.util.*;

/**
 * Grid events are used for notification about what happens within the grid. Note that by
 * design GridGain keeps all events generated on the local node locally and it provides
 * APIs for performing a distributed queries across multiple nodes:
 * <ul>
 *      <li>
 *          {@link Grid#remoteEvents(GridPredicate , long, GridPredicate[])} - querying
 *          events occurred on the nodes specified, including remote nodes.
 *      </li>
 *      <li>
 *          {@link Grid#localEvents(GridPredicate[])} - querying only local
 *          events stored on this local node.
 *      </li>
 *      <li>
 *          {@link Grid#addLocalEventListener(GridLocalEventListener, int...)} - listening
 *          to local grid events (events from remote nodes not included).
 *      </li>
 * </ul>
 * <h1 class="header">Events and Performance</h1>
 * Note that by default all events in GridGain are enabled and therefore generated and stored
 * by whatever event storage SPI is configured. GridGain can and often does generate thousands events per seconds
 * under the load and therefore it creates a significant additional load on the system. If these events are
 * not needed by the application this load is unnecessary and leads to significant performance degradation.
 * <p>
 * It is <b>highly recommended</b> to enable only those events that your application logic requires
 * by using either  {@link GridConfiguration#getExcludeEventTypes()} or
 * {@link GridConfiguration#getIncludeEventTypes()} methods in GridGain configuration. Note that certain
 * events are required for GridGain's internal operations and such events will still be generated but not stored by
 * event storage SPI if they are disabled in GridGain configuration.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 * @see GridJobEvent
 * @see GridCacheEvent
 * @see GridCachePreloadEvent
 * @see GridSwapSpaceEvent
 * @see GridCheckpointEvent
 * @see GridDeploymentEvent
 * @see GridDiscoveryEvent
 * @see GridTaskEvent
 * @see Grid#waitForEventAsync(GridPredicate, int...)
 * @see Grid#waitForEvent(long, Runnable, GridPredicate, int...) 
 */
public interface GridEvent extends GridMetadataAware, Comparable<GridEvent> {
    /**
     * Gets globally unique ID of this event.
     *
     * @return Globally unique ID of this event.
     * @see #localOrder()
     */
    public GridUuid id();

    /**
     * Gets locally unique ID that is atomically incremented for each event. Unlike
     * global {@link #id} this local ID can be used for ordering events on this node.
     * <p>
     * Note that for performance considerations GridGain doesn't order events globally.
     *
     * @return Locally unique ID that is atomically incremented for each new event.
     * @see #id()
     */
    public long localOrder();

    /**
     * Gets ID of the node where event occurred and was recorded.
     *
     * @return ID of the node where event occurred and was recorded.
     */
    public UUID nodeId();

    /**
     * Gets optional message for this event.
     *
     * @return Optional (can be {@code null}) message for this event.
     */
    public String message();

    /**
     * Gets type of this event. All system event types are defined in
     * {@link GridEventType}.
     * <p>
     * NOTE: all types in range <b>from 1 to 1000 are reserved</b> for
     * internal GridGain events and should not be used by user-defined events.
     *
     * @return Event's type.
     * @see GridEventType
     */
    public int type();

    /**
     * Gets name of this event. All events are defined in {@link GridEventType} class.
     *
     * @return Name of this event.
     */
    public String name();

    /**
     * Gets event timestamp. Timestamp is local to the node on which this
     * event was produced. Note that more than one event can be generated
     * with the same timestamp. For ordering purposes use {@link #localOrder()} instead.
     *
     * @return Event timestamp.
     */
    public long timestamp();

    /**
     * Gets a shortened version of {@code toString()} result. Suitable for humans to read.
     *
     * @return Shortened version of {@code toString()} result.
     */
    public String shortDisplay();
}
