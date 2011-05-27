// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.gridify.aop.aspectj;

import org.aspectj.lang.*;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.*;
import org.gridgain.grid.*;
import org.gridgain.grid.gridify.*;
import org.gridgain.grid.gridify.aop.*;
import org.gridgain.grid.typedef.*;
import java.lang.reflect.*;

import static org.gridgain.grid.GridFactoryState.*;

/**
 * AspectJ aspect that cross-cuts on all methods grid-enabled with
 * {@link Gridify} annotation and potentially executes them on
 * remote node.
 * <p>
 * See {@link Gridify} documentation for more information about execution of
 * {@code gridified} methods.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 * @see Gridify
 */
@Aspect
public class GridifyAspectJAspect {
    /**
     * Aspect implementation which executes grid-enabled methods on remote
     * nodes.
     *
     * @param joinPoint Join point provided by AspectJ AOP.
     * @return Method execution result.
     * @throws Throwable If execution failed.
     */
    @SuppressWarnings({"ProhibitedExceptionDeclared", "ProhibitedExceptionThrown", "CatchGenericClass", "unchecked"})
    @Around("execution(@org.gridgain.grid.gridify.Gridify * *(..)) && !cflow(call(* org.gridgain.grid.GridJob.*(..)))")
    public Object gridify(ProceedingJoinPoint joinPoint) throws Throwable {
        Method mtd = ((MethodSignature)joinPoint.getSignature()).getMethod();

        Gridify ann = mtd.getAnnotation(Gridify.class);

        assert ann != null : "Intercepted method does not have gridify annotation.";

        // Since annotations in Java don't allow 'null' as default value
        // we have accept an empty string and convert it here.
        // NOTE: there's unintended behavior when user specifies an empty
        // string as intended grid name.
        // NOTE: the 'ann.gridName() == null' check is added to mitigate
        // annotation bugs in some scripting languages (e.g. Groovy).
        String gridName = F.isEmpty(ann.gridName()) ? null : ann.gridName();

        if (G.state(gridName) != STARTED) {
            throw new GridException("Grid is not locally started: " + gridName);
        }

        // Initialize defaults.
        GridifyArgument arg = new GridifyArgumentAdapter(mtd.getDeclaringClass(), mtd.getName(),
                mtd.getParameterTypes(), joinPoint.getArgs(), joinPoint.getTarget());

        if (!ann.interceptor().equals(GridifyInterceptor.class)) {
            // Check interceptor first.
            if (!ann.interceptor().newInstance().isGridify(ann, arg)) {
                return joinPoint.proceed();
            }
        }

        if (!ann.taskClass().equals(GridifyDefaultTask.class) && ann.taskName().length() > 0) {
            throw new GridException("Gridify annotation must specify either Gridify.taskName() or " +
                "Gridify.taskClass(), but not both: " + ann);
        }

        try {
            Grid grid = G.grid(gridName);

            // If task class was specified.
            if (!ann.taskClass().equals(GridifyDefaultTask.class)) {
                return grid.execute((Class<? extends GridTask<GridifyArgument, Object>>)ann.taskClass(), arg,
                    ann.timeout()).get();
            }

            // If task name was not specified.
            if (ann.taskName().length() == 0) {
                return grid.execute(new GridifyDefaultTask(joinPoint.getSignature().getDeclaringType()), arg,
                    ann.timeout()).get();
            }

            // If task name was specified.
            return grid.execute(ann.taskName(), arg, ann.timeout()).get();
        }
        catch (Throwable e) {
            for (Class<?> ex : ((MethodSignature)joinPoint.getSignature()).getMethod().getExceptionTypes()) {
                // Descend all levels down.
                Throwable cause = e.getCause();

                while (cause != null) {
                    if (ex.isAssignableFrom(cause.getClass())) {
                        throw cause;
                    }

                    cause = cause.getCause();
                }

                if (ex.isAssignableFrom(e.getClass())) {
                    throw e;
                }
            }

            throw new GridifyRuntimeException("Undeclared exception thrown: " + e.getMessage(), e);
        }
    }
}
