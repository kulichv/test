// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.typedef;

import org.gridgain.grid.lang.*;

import java.io.*;

/**
 * Defines {@code alias} for {@link GridTuple4} by extending it. Since Java doesn't provide type aliases
 * (like Scala, for example) we resort to these types of measures. This is intended to provide for more
 * concise code in cases when readability won't be sacrificed. For more information see {@link GridTuple4}.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 * @see GridFunc
 * @see GridTuple
 */
public class T4<V1, V2, V3, V4> extends GridTuple4<V1, V2, V3, V4> {
    /**
     * Empty constructor required by {@link Externalizable}.
     */
    public T4() {
        // No-op.
    }

    /**
     * Fully initializes this tuple.
     *
     * @param val1 First value.
     * @param val2 Second value.
     * @param val3 Third value.
     * @param val4 Forth value.
     */
    public T4(V1 val1, V2 val2, V3 val3, V4 val4) {
        super(val1, val2, val3, val4);
    }
}
