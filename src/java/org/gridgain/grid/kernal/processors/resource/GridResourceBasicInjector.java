// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.resource;

import org.gridgain.grid.*;
import org.gridgain.grid.kernal.managers.deployment.*;
import org.gridgain.grid.typedef.internal.*;

/**
 * Simple injector which wraps only one resource object.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 * @param <T> Type of injected resource.
 */
class GridResourceBasicInjector<T> implements GridResourceInjector {
    /** Resource to inject. */
    private final T rsrc;

    /**
     * Creates injector.
     *
     * @param rsrc Resource to inject.
     */
    GridResourceBasicInjector(T rsrc) {
        this.rsrc = rsrc;
    }

    /**
     * Gets resource.
     *
     * @return Resource
     */
    public T getResource() {
        return rsrc;
    }

    /** {@inheritDoc} */
    @Override public void inject(GridResourceField field, Object target, Class<?> depCls, GridDeployment dep)
        throws GridException {
        GridResourceUtils.inject(field.getField(), target, rsrc);
    }

    /** {@inheritDoc} */
    @Override public void inject(GridResourceMethod mtd, Object target, Class<?> depCls, GridDeployment dep)
        throws GridException {
        GridResourceUtils.inject(mtd.getMethod(), target, rsrc);
    }

    /** {@inheritDoc} */
    @Override public void undeploy(GridDeployment dep) {
        /* No-op. There is no cache. */
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridResourceBasicInjector.class, this);
    }
}
