// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.loaders.servlet;

import org.gridgain.grid.*;
import org.gridgain.grid.typedef.*;
import org.gridgain.grid.typedef.internal.*;
import org.springframework.beans.*;
import org.springframework.beans.factory.xml.*;
import org.springframework.context.support.*;
import org.springframework.core.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
import java.util.*;

/**
 * This class defines servlet-based GridGain loader. This loader can be used to start GridGain
 * inside any web container as servlet.
 * Loader must be defined in {@code web.xml} file.
 * <pre name="code" class="xml">
 * &lt;servlet&gt;
 *     &lt;servlet-name&gt;GridGain&lt;/servlet-name&gt;
 *     &lt;servlet-class&gt;org.gridgain.grid.loaders.servlet.GridServletLoader&lt;/servlet-class&gt;
 *     &lt;init-param&gt;
 *         &lt;param-name&gt;cfgFilePath&lt;/param-name&gt;
 *         &lt;param-value&gt;config/default-spring.xml&lt;/param-value&gt;
 *     &lt;/init-param&gt;
 *     &lt;load-on-startup&gt;1&lt;/load-on-startup&gt;
 * &lt;/servlet&gt;
 * </pre>
 * <p>
 * Servlet-based loader may be used in any web container like Tomcat, Jetty and etc.
 * Depending on the way this loader is deployed the GridGain instance can be accessed
 * by either all web applications or by only one. See web container class loading architecture:
 * <ul>
 * <li><a target=_blank href="http://tomcat.apache.org/tomcat-5.5-doc/class-loader-howto.html">http://tomcat.apache.org/tomcat-5.5-doc/class-loader-howto.html</a></li>
 * <li><a target=_blank href="http://docs.codehaus.org/display/JETTY/Classloading">http://docs.codehaus.org/display/JETTY/Classloading</a></li>
 * </ul>
 * <p>
 * <h2 class="header">Tomcat</h2>
 * There are two ways to start GridGain on Tomcat.
 * <ul>
 * <li>GridGain started when web container starts and GridGain instance is accessible only to all web applications.
 *      <ol>
 *      <li>Add GridGain libraries in Tomcat common loader.
 *          Add in file {@code ${TOMCAT_HOME}/conf/catalina.properties} for property {@code common.loader}
 *          the following {@code ${GRIDGAIN_HOME}/gridgain.jar,${GRIDGAIN_HOME}/libs/*.jar}
 *          (replace {@code ${GRIDGAIN_HOME}} with absolute path).
 *      </li>
 *      <li>GridGain servlet-based loader in {@code ${TOMCAT_HOME}/conf/web.xml}
 *          <pre name="code" class="xml">
 *          &lt;servlet&gt;
 *              &lt;servlet-name&gt;GridGain&lt;/servlet-name&gt;
 *              &lt;servlet-class&gt;org.gridgain.grid.loaders.servlet.GridServletLoader&lt;/servlet-class&gt;
 *              &lt;init-param&gt;
 *                  &lt;param-name&gt;cfgFilePath&lt;/param-name&gt;
 *                  &lt;param-value&gt;config/default-spring.xml&lt;/param-value&gt;
 *              &lt;/init-param&gt;
 *              &lt;load-on-startup&gt;1&lt;/load-on-startup&gt;
 *          &lt;/servlet&gt;
 *          </pre>
 *      </li>
 *      </ol>
 * </li>
 * <li>
 * GridGain started from WAR-file and GridGain instance is accessible only to that web application.
 * Difference with approach described above is that all libraries should be added in WAR file without
 * changes in Tomcat configuration files.
 * </li>
 * </ul>
 * <p>
 * <h2 class="header">Jetty</h2>
 * Below is Java code example with Jetty API:
 * <pre name="code" class="java">
 * Server service = new Server();
 *
 * service.addListener("localhost:8090");
 *
 * ServletHttpContext ctx = (ServletHttpContext)service.getContext("/");
 *
 * ServletHolder servlet = ctx.addServlet("GridGain", "/GridGainLoader",
 *      "org.gridgain.grid.loaders.servlet.GridServletLoader");
 *
 * servlet.setInitParameter("cfgFilePath", "config/default-spring.xml");
 *
 * servlet.setInitOrder(1);
 *
 * servlet.start();
 *
 * service.start();
 * </pre>
 * For more information see
 * <a target=wiki href="http://www.gridgainsystems.com:8080/wiki/display/GG15UG/Configuration+And+Startup">GridGain Configuration And Startup</a>
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridServletLoader extends HttpServlet {
    /** Grid loaded flag. */
    private static boolean loaded;

    /** Configuration file path variable name. */
    private static final String cfgFilePathParam = "cfgFilePath";

    /** */
    private Collection<String> gridNames = new ArrayList<String>();

    /** {@inheritDoc} */
    @SuppressWarnings({"unchecked"})
    @Override public void init() throws ServletException {
        // Avoid multiple servlet instances. GridGain should be loaded once.
        if (loaded)
            return;

        String cfgFile = getServletConfig().getInitParameter(cfgFilePathParam);

        if (cfgFile == null)
            throw new ServletException("Failed to read property: " + cfgFilePathParam);

        URL cfgUrl = U.resolveGridGainUrl(cfgFile);

        if (cfgUrl == null)
            throw new ServletException("Failed to find Spring configuration file (path provided should be " +
                "either absolute, relative to GRIDGAIN_HOME, or relative to META-INF folder): " + cfgFile);

        GenericApplicationContext springCtx;

        try {
            springCtx = new GenericApplicationContext();

            XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(springCtx);

            xmlReader.loadBeanDefinitions(new UrlResource(cfgUrl));

            springCtx.refresh();
        }
        catch (BeansException e) {
            throw new ServletException("Failed to instantiate Spring XML application context: " + e.getMessage(), e);
        }

        Map cfgMap;

        try {
            // Note: Spring is not generics-friendly.
            cfgMap = springCtx.getBeansOfType(GridConfiguration.class);
        }
        catch (BeansException e) {
            throw new ServletException("Failed to instantiate bean [type=" + GridConfiguration.class + ", err=" +
                e.getMessage() + ']', e);
        }

        if (cfgMap == null)
            throw new ServletException("Failed to find a single grid factory configuration in: " + cfgUrl);

        if (cfgMap.isEmpty())
            throw new ServletException("Can't find grid factory configuration in: " + cfgUrl);

        try {
            for (GridConfiguration cfg : (Collection<GridConfiguration>)cfgMap.values()) {
                assert cfg != null;

                GridConfiguration adapter = new GridConfigurationAdapter(cfg);

                Grid grid = G.start(adapter, springCtx);

                // Test if grid is not null - started properly.
                if (grid != null)
                    gridNames.add(grid.name());
            }
        }
        catch (GridException e) {
            // Stop started grids only.
            for (String name: gridNames)
                G.stop(name, true);

            throw new ServletException("Failed to start GridGain.", e);
        }

        loaded = true;
    }

    /** {@inheritDoc} */
    @Override public void destroy() {
        // Stop started grids only.
        for (String name: gridNames)
            G.stop(name, true);

        loaded = false;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridServletLoader.class, this);
    }
}
