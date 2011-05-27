// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.cache;

import org.gridgain.grid.*;
import org.gridgain.grid.cache.affinity.*;
import org.gridgain.grid.lang.*;

import java.lang.annotation.*;
import java.util.concurrent.*;

/**
 * Allows to specify cache name from grid computations. It is used to provide cache name
 * for affinity routing of grid computations, such as {@link GridJob}, {@link Runnable},
 * {@link Callable}, or {@link GridClosure}. It should be used only in conjunction with
 * {@link GridCacheAffinityMapped @GridCacheAffinityMapped} annotation, and should be attached to a method or field
 * that provides cache name for the computation. Only one annotation per class
 * is allowed. In the absence of this annotation, the default no-name cache
 * will be used for providing key-to-node affinity.
 * <p>
 * Refer to {@link GridCacheAffinityMapped @GridCacheAffinityMapped} documentation for more information
 * and examples about this annotation.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 * @see GridCacheAffinityMapped
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface GridCacheName {
    // No-op.
}
