// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.resources;

import org.gridgain.grid.*;
import org.gridgain.grid.spi.*;
import org.springframework.context.*;
import java.lang.annotation.*;

/**
 * Annotates a field or a setter method for injection of Spring {@link ApplicationContext} resource.
 * When GridGain starts using Spring configuration, the Application Context for Spring
 * Configuration is injected as this resource. Spring application context can also be explicitly
 * passed into the grid via {@link GridFactory#start(GridConfiguration, ApplicationContext)}
 * method.
 * <p>
 * Spring Application Context can be injected into instances of following classes:
 * <ul>
 * <li>{@link GridTask}</li>
 * <li>{@link GridJob}</li>
 * <li>{@link GridSpi}</li>
 * <li>{@link GridLifecycleBean}</li>
 * <li>{@link GridUserResource @GridUserResource}</li>
 * </ul>
 * <p>
 * Here is how injection would typically happen:
 * <pre name="code" class="java">
 * public class MyGridJob implements GridJob {
 *      ...
 *      &#64;GridSpringApplicationContextResource
 *      private ApplicationContext springCtx;
 *      ...
 *  }
 * </pre>
 * or
 * <pre name="code" class="java">
 * public class MyGridJob implements GridJob {
 *     ...
 *     private ApplicationContext springCtx;
 *     ...
 *     &#64;GridSpringApplicationContextResource
 *     public void setApplicationContext(MBeanServer springCtx) {
 *          this.springCtx = springCtx;
 *     }
 *     ...
 * }
 * </pre>
 * <p>
 * <img src="http://www.gridgain.com/images/spring-small.png">
 * <br>
 * For information about Spring framework visit <a href="http://www.springframework.org/">www.springframework.org</a>
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface GridSpringApplicationContextResource {
    // No-op.
}
