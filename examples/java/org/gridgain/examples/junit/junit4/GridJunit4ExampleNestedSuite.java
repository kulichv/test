// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.examples.junit.junit4;

import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Suite.*;

/**
 * This suite is nested within {@link GridJunit4ExampleSuite} suite. By
 * wrapping {@link TestA} and {@link TestB} into their own suite we guarantee
 * that they will always execute sequentially, however still in parallel with
 * other tests.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
@RunWith(Suite.class)
@SuiteClasses({
    TestA.class,
    TestB.class
})
public class GridJunit4ExampleNestedSuite {
    // No-op.
}
