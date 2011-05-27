// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.communication.coherence;

import com.tangosol.net.*;
import org.gridgain.grid.typedef.internal.*;
import java.io.*;
import java.util.*;

/**
 * Contains task which should be running on destination Coherence member node.
 * SPI will send that objects only to members with started invocation service
 * {@link InvocationService} with name
 * {@link GridCoherenceCommunicationSpi#setServiceName(String)}. The agents used as
 * transport to notify remote communication SPI's.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
class GridCoherenceCommunicationAgent extends AbstractInvocable {
    /** Communication message. */
    private Serializable msg;

    /** Sender UID. */
    private UUID srcNodeId;

    /**
     * Creates an agent.
     *
     * @param srcNodeId Sender UID.
     * @param msg Message being sent.
     */
    GridCoherenceCommunicationAgent(UUID srcNodeId, Serializable msg) {
        assert srcNodeId != null;

        this.srcNodeId = srcNodeId;
        this.msg = msg;
    }

    /** {@inheritDoc} */
    @Override public void run() {
       GridCoherenceCommunicationSpi spi = (GridCoherenceCommunicationSpi)getService().getUserContext();

        if (spi != null) {
            spi.onMessage(srcNodeId, msg);
        }
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridCoherenceCommunicationAgent.class, this);
    }
}
