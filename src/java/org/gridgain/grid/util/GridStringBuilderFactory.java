// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.util;

import org.gridgain.grid.typedef.internal.*;

/**
 * Per-thread cache of {@link StringBuilder} instances.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public final class GridStringBuilderFactory {
    /** Cache string builders per thread for better performance. */
    private static ThreadLocal<CachedBuilder> builders = new ThreadLocal<CachedBuilder>() {
        @Override protected CachedBuilder initialValue() {
            return new CachedBuilder();
        }
    };

    /**
     * Acquires a cached instance of {@link StringBuilder} if
     * current thread is not using it yet. Otherwise a new
     * instance of string builder is returned.
     *
     * @return Cached instance of {@link StringBuilder}.
     */
    public static SB acquire() {
        return builders.get().acquire();
    }

    /**
     * Releases {@link StringBuilder} back to cache.
     *
     * @param builder String builder to release.
     */
    public static void release(SB builder) {
        builders.get().release(builder);
    }

    /**
     * No-op constructor to ensure singleton.
     */
    private GridStringBuilderFactory() {
        /* No-op. */
    }

    /**
     * Cached builder.
     *
     * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
     * @version 3.0.9c.27052011
     */
    private static class CachedBuilder {
        /** Cached builder. */
        private SB builder = new SB();

        /** {@code True} if already used by a thread. */
        private boolean used;

        /**
         * @return The cached builder.
         */
        public SB acquire() {
            // If cached instance is already used, then we don't optimize.
            // Simply return a new StringBuilder in such case.
            if (used) {
                return new SB();
            }

            used = true;

            return builder;
        }

        /**
         * Releases builder for reuse.
         *
         * @param builder Builder to release.
         */
        @SuppressWarnings({"ObjectEquality"})
        public void release(SB builder) {
            if (this.builder == builder) {
                builder.setLength(0);

                used = false;
            }
        }
    }
}
