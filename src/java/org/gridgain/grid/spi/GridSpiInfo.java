// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi;

import java.lang.annotation.*;

/**
 * Annotation for SPI main interface implementation. Every SPI implementation should be
 * annotated with this annotations. Kernel will not load SPI implementation without this
 * annotation.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
@SuppressWarnings({"JavaDoc"})
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface GridSpiInfo {
    /**
     * SPI provider's author.
     */
    public String author();

    /**
     * Vendor's URL.
     */
    public String url();

    /**
     * Vendor's email (info or support).
     */
    public String email();

    /**
     * SPI implementation version.
     */
    public String version();
}
