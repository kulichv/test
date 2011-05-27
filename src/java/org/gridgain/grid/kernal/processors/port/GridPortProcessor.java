// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.port;

import org.gridgain.grid.*;
import org.gridgain.grid.kernal.*;
import org.gridgain.grid.kernal.processors.*;
import java.util.*;

/**
 * Registers and deregisters all ports used by SPI and Manager.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridPortProcessor extends GridProcessorAdapter {
    /** Collection of records about ports use. */
    private final Collection<GridPortRecord> recs;

    /** Listeners. */
    private final Collection<GridPortListener> lsnrs = new LinkedHashSet<GridPortListener>();

    /**
     * @param ctx Kernal context.
     */
    public GridPortProcessor(GridKernalContext ctx) {
        super(ctx);

        recs = new TreeSet<GridPortRecord>(new Comparator<GridPortRecord>() {
            @Override public int compare(GridPortRecord o1, GridPortRecord o2) {
                int p1 = o1.port();
                int p2 = o2.port();

                return p1 < p2 ? -1 : p1 == p2 ? 0 : 1;
            }
        });
    }

    /** {@inheritDoc} */
    @Override public void start() throws GridException {
        if (log.isDebugEnabled()) {
            log.debug("Started port processor.");
        }
    }

    /** {@inheritDoc} */
    @Override public void stop(boolean cancel, boolean wait) throws GridException {
        if (log.isDebugEnabled()) {
            log.debug("Stopped port processor.");
        }
    }

    /**
     * Registers port using by passed class.
     *
     * @param port Port.
     * @param proto Protocol.
     * @param cls Class.
     */
    public void registerPort(int port, GridPortProtocol proto, Class cls) {
        assert port > 0 && port < 65535;
        assert proto != null;
        assert cls != null;

        synchronized (recs) {
            recs.add(new GridPortRecord(port, proto, cls));
        }

        notifyListeners();
    }

    /**
     * Deregisters all ports used by passed class. 
     *
     * @param cls Class.
     */
    public void deregisterPorts(Class cls) {
        assert cls != null;

        synchronized (recs) {
            for (Iterator<GridPortRecord> iter = recs.iterator(); iter.hasNext();) {
                GridPortRecord pr = iter.next();

                if (pr.clazz().equals(cls)) {
                    iter.remove();
                }
            }
        }

        notifyListeners();
    }

    /**
     * Deregisters port used by passed class.
     *
     * @param port Port.
     * @param proto Protocol.
     * @param cls Class.
     */
    public void deregisterPort(int port, GridPortProtocol proto, Class cls) {
        assert port > 0 && port < 65535;
        assert proto != null;
        assert cls != null;

        synchronized (recs) {
            for (Iterator<GridPortRecord> iter = recs.iterator(); iter.hasNext();) {
                GridPortRecord pr = iter.next();

                if (pr.port() == port && pr.protocol() == proto && pr.clazz().equals(cls)) {
                    iter.remove();
                }
            }
        }

        notifyListeners();
    }

    /**
     * Returns unmodifiable collections of records.
     *
     * @return Unmodifiable collections of records
     */
    public Collection<GridPortRecord> records() {
        synchronized (recs) {
            return Collections.unmodifiableCollection(recs);
        }
    }

    /**
     * Add listener.
     *
     * @param lsnr Listener.
     */
    public void addPortListener(GridPortListener lsnr) {
        assert lsnr != null;

        synchronized (lsnrs) {
            lsnrs.add(lsnr);
        }
    }

    /**
     * Remove listener.
     *
     * @param lsnr Listener.
     */
    public void removePortListener(GridPortListener lsnr) {
        assert lsnr != null;
        
        synchronized (lsnrs) {
            lsnrs.remove(lsnr);
        }
    }

    /**
     * Notify all listeners
     */
    private void notifyListeners() {
        synchronized (lsnrs) {
            for (GridPortListener lsnr : lsnrs) {
                lsnr.onPortChange();
            }
        }
    }
}
