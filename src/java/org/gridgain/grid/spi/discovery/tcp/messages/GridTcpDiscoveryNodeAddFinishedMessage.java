// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.discovery.tcp.messages;

import org.gridgain.grid.spi.discovery.tcp.*;
import org.gridgain.grid.typedef.internal.*;

import java.io.*;
import java.util.*;

/**
 * Sent by coordinator across the ring to finish node add process.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
@GridTcpDiscoveryEnsureDelivery
public class GridTcpDiscoveryNodeAddFinishedMessage extends GridTcpDiscoveryAbstractMessage {
    /** Added node ID. */
    private UUID nodeId;

    /**
     * Public default no-arg constructor for {@link Externalizable} interface.
     */
    public GridTcpDiscoveryNodeAddFinishedMessage() {
        // No-op.
    }

    /**
     * Constructor.
     *
     * @param creatorNodeId ID of the creator node (coordinator).
     * @param nodeId Added node ID.
     */
    public GridTcpDiscoveryNodeAddFinishedMessage(UUID creatorNodeId, UUID nodeId) {
        super(creatorNodeId);

        this.nodeId = nodeId;
    }

    /**
     * Gets ID of the node added.
     *
     * @return ID of the node added.
     */
    public UUID nodeId() {
        return nodeId;
    }

    /** {@inheritDoc} */
    @Override public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);

        U.writeUuid(out, nodeId);
    }

    /** {@inheritDoc} */
    @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);

        nodeId = U.readUuid(in);
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridTcpDiscoveryNodeAddFinishedMessage.class, this, "super", super.toString());
    }
}
