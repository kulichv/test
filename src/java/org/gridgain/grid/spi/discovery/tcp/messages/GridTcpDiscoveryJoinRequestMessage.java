// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.discovery.tcp.messages;

import org.gridgain.grid.spi.discovery.tcp.internal.*;
import org.gridgain.grid.typedef.internal.*;

import java.io.*;

/**
 * Initial message sent by a node that wants to enter topology.
 * Sent to random node during SPI start. Then forwarded directly to coordinator.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridTcpDiscoveryJoinRequestMessage extends GridTcpDiscoveryAbstractMessage {
    /** New node that wants to join the topology. */
    private GridTcpDiscoveryNode node;

    /** Responded flag. */
    private boolean responded;

    /**
     * Public default no-arg constructor for {@link Externalizable} interface.
     */
    public GridTcpDiscoveryJoinRequestMessage() {
        // No-op.
    }

    /**
     * Constructor.
     *
     * @param node New node that wants to join.
     */
    public GridTcpDiscoveryJoinRequestMessage(GridTcpDiscoveryNode node) {
        super(node.id());

        this.node = node;
    }

    /**
     * Gets new node that wants to join the topology.
     *
     * @return Node that wants to join the topology.
     */
    public GridTcpDiscoveryNode node() {
        return node;
    }

    /**
     * @return {@code true} flag.
     */
    public boolean responded() {
        return responded;
    }

    /**
     * @param responded Responded flag.
     */
    public void responded(boolean responded) {
        this.responded = responded;
    }

    /** {@inheritDoc} */
    @Override public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);

        out.writeObject(node);
        out.writeBoolean(responded);
    }

    /** {@inheritDoc} */
    @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);

        node = (GridTcpDiscoveryNode)in.readObject();
        responded = in.readBoolean();
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridTcpDiscoveryJoinRequestMessage.class, this, "super", super.toString());
    }
}
