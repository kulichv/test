// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.deployment.uri;

import org.gridgain.grid.logger.*;
import org.gridgain.grid.spi.*;
import org.gridgain.grid.typedef.internal.*;
import org.springframework.beans.*;
import org.springframework.beans.factory.xml.*;
import org.springframework.core.io.*;
import java.io.*;

/**
 * Workaround for {@link InputStreamResource}. Converts input stream with XML
 * to {@code GridUriDeploymentSpringDocument} with {@link ByteArrayResource}
 * instead of {@link InputStreamResource}.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
final class GridUriDeploymentSpringParser {
    /**
     * Enforces singleton.
     */
    private GridUriDeploymentSpringParser() {
        // No-op.
    }

    /**
     * Converts given input stream expecting XML inside to
     * {@link GridUriDeploymentSpringDocument}.
     * <p>
     * This is a workaround for the {@link InputStreamResource} which does
     * not work properly.
     *
     * @param in Input stream with XML.
     * @param log Logger
     * @return Grid wrapper for the input stream.
     * @throws GridSpiException Thrown if incoming input stream could not be
     *      read or parsed by {@code Spring} {@link XmlBeanFactory}.
     * @see XmlBeanFactory
     */
    static GridUriDeploymentSpringDocument parseTasksDocument(InputStream in, GridLogger log) throws
        GridSpiException {
        assert in != null;

        // Note: use ByteArrayResource instead of InputStreamResource because InputStreamResource doesn't work.
        ByteArrayOutputStream out  = new ByteArrayOutputStream();

        try {
            U.copy(in, out);

            XmlBeanFactory factory = new XmlBeanFactory(new ByteArrayResource(out.toByteArray()));

            return new GridUriDeploymentSpringDocument(factory);
        }
        catch (BeansException e) {
            throw new GridSpiException("Failed to parse spring XML file.", e);
        }
        catch (IOException e) {
            throw new GridSpiException("Failed to parse spring XML file.", e);
        }
        finally{
            U.close(out, log);
        }
    }
}
