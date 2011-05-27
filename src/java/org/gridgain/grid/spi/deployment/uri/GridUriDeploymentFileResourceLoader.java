// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.deployment.uri;

import org.gridgain.grid.*;
import org.gridgain.grid.spi.*;
import java.io.*;

/**
 * Class loader helper that could load class from the file using certain
 * class loader.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
class GridUriDeploymentFileResourceLoader {
    /** Class loader class should be loaded by. */
    private final ClassLoader clsLdr;

    /** Initial directory. */
    private final File scanPathDir;

    /**
     * Creates new instance of loader helper.
     *
     * @param clsLdr Class loader class should be loaded by.
     * @param scanPathDir Initial directory.
     */
    GridUriDeploymentFileResourceLoader(ClassLoader clsLdr, File scanPathDir) {
        this.clsLdr = clsLdr;
        this.scanPathDir = scanPathDir;
    }

    /**
     * Creates new class from file with given file name.
     *
     * @param fileName Name of the class to be loaded. It might be either
     *      fully-qualified or just a class name.
     * @param ignoreUnknownRsrc Whether unresolved classes should be
     *      ignored or not.
     * @return Loaded class.
     * @throws GridSpiException If class could not be loaded and
     *      {@code ignoreUnknownRsrc} parameter is {@code true}.
     */
    @SuppressWarnings("unchecked")
    Class<? extends GridTask<?, ?>> createResource(String fileName, boolean ignoreUnknownRsrc) throws GridSpiException {
        if (scanPathDir.isDirectory()) {
            fileName = fileName.substring(scanPathDir.getAbsolutePath().length() + 1);
        }

        if (fileName.endsWith(".class")) {
            String str = fileName;

            // Replace separators.
            str = str.replaceAll("\\/|\\\\", ".");

            // Strip off '.class' extention.
            str = str.substring(0, str.indexOf(".class"));

            try {
                return (Class<? extends GridTask<?, ?>>)clsLdr.loadClass(str);
            }
            catch (ClassNotFoundException e) {
                if (ignoreUnknownRsrc) {
                    // No-op.
                }
                else {
                    throw new GridSpiException("Failed to load class: " + str, e);
                }
            }
        }

        // Not a class resource.
        return null;
    }
}
