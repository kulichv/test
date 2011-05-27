// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.typedef.internal;

import org.gridgain.grid.kernal.*;

/**
 * Defines internal {@code typedef} for {@link GridPlainRunnable}. Since Java doesn't provide type aliases
 * (like Scala, for example) we resort to these types of measures. This is intended for internal
 * use only and meant to provide for more terse code when readability of code is not compromised.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public interface GPR extends GridPlainRunnable { /* No-op. */ }
