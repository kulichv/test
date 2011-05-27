// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.marshaller.jboss;

import java.io.*;

/**
 * Wrapper for {@link InputStream}.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
class GridJbossMarshallerInputStreamWrapper extends InputStream {
    /** */
    private InputStream in;

    /**
     * Creates wrapper.
     *
     * @param in Wrapped input stream
     */
    GridJbossMarshallerInputStreamWrapper(InputStream in) {
        assert in != null;

        this.in = in;
    }

    /** {@inheritDoc} */
    @Override public int read() throws IOException {
        return in.read();
    }

    /** {@inheritDoc} */
    @Override public int read(byte[] b) throws IOException {
        return in.read(b);
    }

    /** {@inheritDoc} */
    @Override public int read(byte[] b, int off, int len) throws IOException {
        return in.read(b, off, len);
    }

    /** {@inheritDoc} */
    @Override public long skip(long n) throws IOException {
        return in.skip(n);
    }

    /** {@inheritDoc} */
    @Override public int available() throws IOException {
        return in.available();
    }

    /** {@inheritDoc} */
    @SuppressWarnings({"NonSynchronizedMethodOverridesSynchronizedMethod"})
    @Override public void mark(int readLimit) {
        in.mark(readLimit);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({"NonSynchronizedMethodOverridesSynchronizedMethod"})
    @Override public void reset() throws IOException {
        in.reset();
    }

    /** {@inheritDoc} */
    @Override public boolean markSupported() {
        return in.markSupported();
    }
}
