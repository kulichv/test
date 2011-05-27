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
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Factory class to create class loader that loads classes and resources from
 * the GAR file or "unpacked" GAR file (GAR directory).
 * <p>
 * Class loader scans GAR file or GAR directory first and than if
 * class/resource was not found scans all JAR files.
 * It is assumed that all libraries are in the {@link #DFLT_LIBS_DIR_PATH}
 * directory.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
class GridUriDeploymentClassLoaderFactory {
    /** Libraries directory default value (value is {@code lib}). */
    public static final String DFLT_LIBS_DIR_PATH = "lib";

    /**
     * @param parent Parent class loader.
     * @param file GAR file or directory with unpacked GAR file.
     * @param log Logger.
     * @return Class Loader.
     * @throws GridSpiException In case of any error.
     */
    public static ClassLoader create(ClassLoader parent, File file, GridLogger log) throws GridSpiException {
        assert parent != null;
        assert file != null;
        assert log != null;

        assert file.isDirectory() == true;

        List<URL> urls = new ArrayList<URL>();

        URL mainUrl;

        try {
            String url = file.toURI().toURL().toString();
            if ((url.length() > 0 && url.charAt(url.length() - 1) == '/') == false) {
                mainUrl = new URL(url + '/');
            }
            else {
                mainUrl = file.toURI().toURL();
            }

            urls.add(mainUrl);

            File libDir = new File(file, DFLT_LIBS_DIR_PATH);

            if (libDir.exists()) {
                File[] files = libDir.listFiles(new FilenameFilter() {
                    @Override public boolean accept(File dir, String name) { return name.endsWith(".jar") == true; }
                });

                if (files.length > 0) {
                    for (File jarFile : files) {
                        urls.add(jarFile.toURI().toURL());
                    }
                }
            }

            return new GridUriDeploymentClassLoader(urls.toArray(new URL[urls.size()]), parent);
        }
        catch (MalformedURLException e) {
            throw new GridSpiException("Failed to create class loader for GAR file: " + file, e);
        }
    }
}
