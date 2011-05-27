// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal;

import java.util.*;

/**
 * Interception listener is notified about method apply. For each intercepted method
 * apply the listener will be called twice - before and after the apply.
 * <p>
 * Method {@link #beforeCall(Class, String, Object[])} is called right before the
 * traceable method and the second apply {@link #afterCall(Class, String, Object[], Object, Throwable)}
 * is made to get invocation result and exception, if there was one.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public interface GridProxyListener extends EventListener {
    /**
     * Method is called right before the traced method.
     *
     * @param cls Callee class.
     * @param methodName Callee method name.
     * @param args Callee method parameters.
     */
    public void beforeCall(Class<?> cls, String methodName, Object[] args);

    /**
     * Method is called right after the traced method.
     *
     * @param cls Callee class.
     * @param methodName Callee method name.
     * @param args Callee method parameters.
     * @param res Call result. Might be {@code null} if apply
     *      returned {@code null} or if exception happened.
     * @param e Exception thrown by given method apply, if any. Can be {@code null}.
     */
    public void afterCall(Class<?> cls, String methodName, Object[] args, Object res, Throwable e);
}
