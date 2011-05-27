// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid;

import java.io.*;
import java.util.*;

/**
 * Listener for asynchronous local node grid events. You can subscribe for local node grid
 * event notifications via {@link Grid#addLocalEventListener(GridLocalEventListener, int...)}.
 * <p>
 * Use {@link Grid#addLocalEventListener(GridLocalEventListener, int...)} to register
 * this listener with grid.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public interface GridLocalEventListener extends EventListener, Serializable {
    /**
     * Local event callback.
     *
     * @param evt local grid event.
     */
    public void onEvent(GridEvent evt);
}
