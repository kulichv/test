// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.marshaller;

import org.gridgain.grid.*;
import java.io.*;

/**
 * {@code GridMarshaller} allows to marshal or unmarshal objects in grid. It provides
 * serialization/deserialization mechanism for all instances that are sent across networks
 * or are otherwise serialized.
 * <p>
 * Gridgain provides the following {@code GridMarshaller} implementations:
 * <ul>
 * <li>{@link org.gridgain.grid.marshaller.jboss.GridJBossMarshaller} - default</li>
 * <li>{@link org.gridgain.grid.marshaller.jdk.GridJdkMarshaller}</li>
 * <li>{@link org.gridgain.grid.marshaller.xstream.GridXstreamMarshaller}</li>
 * <li>{@link org.gridgain.grid.marshaller.optimized.GridOptimizedMarshaller}</li>
 * </ul>
 * <p>
 * Below are examples of marshaller configuration, usage, and injection into tasks, jobs,
 * and SPI's.
 * <h2 class="header">Java Example</h2>
 * {@code GridMarshaller} can be explicitely configured in code.
 * <pre name="code" class="java">
 * GridJbossMarshaller marshaller = new GridJbossMarshaller();
 *
 * GridConfigurationAdapter cfg = new GridConfigurationAdapter();
 *
 * // Override marshaller.
 * cfg.setMarshaller(marshaller);
 *
 * // Starts grid.
 * G.start(cfg);
 * </pre>
 * <h2 class="header">Spring Example</h2>
 * GridMarshaller can be configured from Spring XML configuration file:
 * <pre name="code" class="xml">
 * &lt;bean id="grid.custom.cfg" class="org.gridgain.grid.GridConfigurationAdapter" singleton="true"&gt;
 *     ...
 *     &lt;property name="marshaller"&gt;
 *         &lt;bean class="org.gridgain.grid.marshaller.jboss.GridJBossMarshaller"/&gt;
 *     &lt;/property&gt;
 *     ...
 * &lt;/bean&gt;
 * </pre>
 * <p>
 * <img src="http://www.gridgain.com/images/spring-small.png">
 * <br>
 * For information about Spring framework visit <a href="http://www.springframework.org/">www.springframework.org</a>
 * <h2 class="header">Injection Example</h2>
 * GridMarshaller can be injected in users task, job or SPI as following:
 * <pre name="code" class="java">
 * public class MyGridJob implements GridJob {
 *     ...
 *     &#64;GridMarshallerResource
 *     private GridMarshaller marshaller;
 *
 *     public Serializable execute() {
 *         // Use marshaller to serialize/deserialize any object.
 *         ...
 *     }
 * }
 * </pre>
 * or
 * <pre name="code" class="java">
 * public class MyGridJob implements GridJob {
 *     ...
 *     private GridMarshaller marshaller;
 *     ...
 *     &#64;GridMarshallerResource
 *     public void setMarshaller(GridMarshaller marshaller) {
 *         this.marshaller = marshaller;
 *     }
 *     ...
 * }
 * </pre>
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public interface GridMarshaller {
    /**
     * Marshals object to the output stream. This method should not close
     * given output stream.
     *
     * @param obj Object to marshal.
     * @param out Output stream to marshal into.
     * @throws GridException If marshalling failed.
     */
    public void marshal(Object obj, OutputStream out) throws GridException;

    /**
     * Unmarshalls object from the output stream using given class loader.
     * This method should not close given input stream.
     *
     * @param <T> Type of unmarshalled object.
     * @param in Input stream.
     * @param clsLdr Class loader to use.
     * @return Unmarshalled object.
     * @throws GridException If unmarshalling failed.
     */
    public <T> T unmarshal(InputStream in, ClassLoader clsLdr) throws GridException;
}
