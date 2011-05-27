// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.marshaller.jdk;

import java.io.*;

/**
 * Wrapper for {@link OutputStream}.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
class GridJdkMarshallerOutputStreamWrapper extends OutputStream {
    /** */
    private OutputStream out;

    /**
     * Creates wrapper.
     *
     * @param out Wrapped output stream
     */
    GridJdkMarshallerOutputStreamWrapper(OutputStream out) {
        assert out != null;

        this.out = out;
    }

    /** {@inheritDoc} */
    @Override public void write(int b) throws IOException {
        out.write(b);
    }

    /** {@inheritDoc} */
    @Override public void write(byte[] b) throws IOException {
        out.write(b);
    }

    /** {@inheritDoc} */
    @Override public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
    }

    /** {@inheritDoc} */
    @Override public void flush() throws IOException {
        out.flush();
    }
}
