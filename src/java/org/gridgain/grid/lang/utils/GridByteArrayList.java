// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.lang.utils;

import org.gridgain.grid.typedef.internal.*;
import org.gridgain.grid.util.tostring.*;
import java.io.*;
import java.nio.*;

/**
 * Re-sizable array implementation of the byte list (eliminating auto-boxing of primitive byte type).
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridByteArrayList implements Externalizable {
    /** List byte data. */
    @GridToStringExclude
    private byte[] data;

    /** List's size. */
    private int size;

    /**
     * No-op constructor that creates uninitialized list. This method is meant
     * to by used only by {@link Externalizable} interface.
     */
    public GridByteArrayList() {
        // No-op.
    }

    /**
     * Creates empty list with the specified initial capacity.
     *
     * @param capacity Initial capacity.
     */
    public GridByteArrayList(int capacity) {
        assert capacity > 0;

        data = new byte[capacity];
    }

    /**
     * Wraps existing array into byte array list.
     *
     * @param data Array to wrap.
     * @param size Size of data inside of array.
     */
    public GridByteArrayList(byte[] data, int size) {
        assert data != null;
        assert size > 0;

        this.data = data;
        this.size = size;
    }

    /**
     * Wraps existing array into byte array list.
     *
     * @param data Array to wrap.
     */
    public GridByteArrayList(byte[] data) {
        assert data != null;

        this.data = data;

        size = data.length;
    }

    /**
     * Resets byte array to empty. Note that this method simply resets the size
     * as there is no need to reset every byte in the array.
     */
    public void reset() {
        size = 0;
    }

    /**
     * Returns the underlying array. This method exists as performance
     * optimization to avoid extra copying of the arrays. Data inside
     * of this array should not be altered, only copied.
     *
     * @return Internal array.
     */
    public byte[] getInternalArray() {
        return data;
    }

    /**
     * Gets copy of internal array.
     *
     * @return Copy of internal array.
     */
    public byte[] getArray() {
        byte[] res = new byte[size];

        System.arraycopy(data, 0, res, 0, size);

        return res;
    }

    /**
     * Returns internal array if it represents the whole length,
     * otherwise returns the result of {@link #getArray()}.
     *
     * @return Array of exact data size.
     */
    public byte[] getEntireArray() {
        return size == data.length ? getInternalArray() : getArray();
    }

    /**
     * Gets initial capacity of the list.
     *
     * @return Initial capacity.
     */
    public int getCapacity() {
        return data.length;
    }

    /**
     * Sets initial capacity of the list.
     *
     * @param capacity Initial capacity.
     */
    private void setCapacity(int capacity) {
        assert capacity > 0;

        if (capacity != getCapacity()) {
            if (capacity < size) {
                size = capacity;
            }

            byte[] newData = new byte[capacity];

            System.arraycopy(data, 0, newData, 0, size);

            data = newData;
        }
    }

    /**
     * Gets number of bytes in the list.
     *
     * @return Number of bytes in the list.
     */
    public int getSize() {
        return size;
    }

    /**
     * Pre-allocates internal array for specified byte number only
     * if it currently is smaller than desired number.
     *
     * @param cnt Byte number to preallocate.
     */
    public void allocate(int cnt) {
        if (size + cnt > getCapacity()) {
            setCapacity(size + cnt);
        }
    }

    /**
     * Re-sizes internal byte array representation.
     *
     * @param cnt Number of bytes to request.
     */
    private void requestFreeSize(int cnt) {
        if (size + cnt > getCapacity()) {
            setCapacity((size + cnt) << 1);
        }
    }

    /**
     * Appends byte element to the list.
     *
     * @param b Byte value to append.
     */
    public void add(byte b) {
        requestFreeSize(1);

        data[size++] = b;
    }

    /**
     * Sets a byte at specified position.
     *
     * @param pos Specified position.
     * @param b Byte to set.
     */
    public void set(int pos, byte b) {
        assert pos >= 0;
        assert pos < size;

        data[pos] = b;
    }

    /**
     * Appends integer to the next 4 bytes of list.
     *
     * @param i Integer to append.
     */
    public void add(int i) {
        requestFreeSize(4);

        U.intToBytes(i, data, size);

        size += 4;
    }

    /**
     * Sets integer at specified position.
     *
     * @param pos Specified position.
     * @param i Integer to set.
     */
    public void set(int pos, int i) {
        assert pos >= 0;
        assert pos + 4 <= size;

        U.intToBytes(i, data, pos);
    }

    /**
     * Appends long to the next 8 bytes of list.
     *
     * @param l Long to append.
     */
    public void add(long l) {
        requestFreeSize(8);

        U.longToBytes(l, data, size);

        size += 8;
    }

    /**
     * Sets long at specified position.
     *
     * @param pos Specified position.
     * @param l Long to set.
     */
    public void set(int pos, long l) {
        assert pos >= 0;
        assert pos + 8 <= size;

        U.longToBytes(l, data, pos);
    }

    /**
     * @param bytes Byte to add.
     * @param off Offset at which to add.
     * @param len Number of bytes to add.
     */
    public void add(byte[] bytes, int off, int len) {
        requestFreeSize(len);

        System.arraycopy(bytes, off, data, size, len);

        size += len;
    }

    /**
     * Adds data from byte buffer into array.
     *
     * @param buf Buffer to read bytes from.
     * @param len Number of bytes to add.
     */
    public void add(ByteBuffer buf, int len) {
        requestFreeSize(len);

        buf.get(data, size, len);

        size += len;
    }

    /**
     * Gets the element (byte) at the specified position in the list.
     *
     * @param i Index of element to return.
     * @return The element at the specified position in the list.
     */
    public byte get(int i) {
        assert i < size;

        return data[i];
    }

    /**
     * Gets 4 bytes from byte list as an integer.
     *
     * @param i Index into the byte list.
     * @return Integer starting at index location.
     */
    public int getInt(int i) {
        assert i + 4 <= size;

        return U.bytesToInt(data, i);
    }

    /**
     * Reads all data from input stream until the end into this byte list.
     *
     * @param in Input stream to read from.
     * @throws IOException Thrown if any I/O error occurred.
     */
    public void readAll(InputStream in) throws IOException {
        assert in != null;

        int read = 0;

        while (read >= 0) {
            int free = getCapacity() - size;

            if (free == 0) {
                requestFreeSize(1);

                free = getCapacity() - size;

                assert free > 0;
            }

            read = in.read(data, size, free);

            if (read > 0) {
                size += read;
            }
        }
    }

    /** {@inheritDoc} */
    @Override public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(size);

        out.write(data, 0, size);
    }

    /** {@inheritDoc} */
    @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        size = in.readInt();

        data = new byte[size];

        in.readFully(data, 0, size);
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridByteArrayList.class, this);
    }
}
