// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.cache.jta.reflect;

import org.gridgain.grid.*;
import org.gridgain.grid.cache.jta.*;
import org.gridgain.grid.typedef.internal.*;
import javax.transaction.*;
import java.lang.reflect.*;

/**
 * Implementation of {@link GridCacheTmLookup} interface that attempts to obtain
 * JTA manager by calling static method on the class.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridCacheReflectionTmLookup implements GridCacheTmLookup {
    /** */
    private String cls;

    /** */
    private String mtd;

    /**
     * Creates uninitialized reflection TM lookup.
     */
    public GridCacheReflectionTmLookup() { /* No-op. */ }

    /**
     * Creates generic TM lookup with given class and method name.
     * 
     * @param cls Class name.
     * @param mtd Method name on that the class.
     */
    public GridCacheReflectionTmLookup(String cls, String mtd) {
        A.notNull(cls, "cls");
        A.notNull(mtd, "mtd");

        this.cls = cls;
        this.mtd = mtd;
    }

    /**
     * Gets class name to use.
     *
     * @return Class name to use.
     */
    public String getClassName() {
        return cls;
    }

    /**
     * Sets class name to use.
     *
     * @param cls Class name to use.
     */
    public void setClassName(String cls) {
        A.notNull(cls, "cls");

        this.cls = cls;
    }

    /**
     * Gets method name.
     *
     * @return Method name to use.
     */
    public String getMethodName() {
        return mtd;
    }

    /**
     * Sets method name.
     * 
     * @param mtd Method name to use.
     */
    public void setMethodName(String mtd) {
        A.notNull(mtd, "mtd");

        this.mtd = mtd;
    }

    /** {@inheritDoc} */
    @Override public TransactionManager getTm() throws GridException {
        assert cls != null;
        assert mtd != null;

        try {
            return (TransactionManager)Class.forName(cls).getMethod(mtd).invoke(null);
        }
        catch (ClassNotFoundException e) {
            throw new GridException("Failed to find class: " + cls, e);
        }
        catch (NoSuchMethodException e) {
            throw new GridException("Failed to find method: " + mtd, e);
        }
        catch (InvocationTargetException e) {
            throw new GridException("Failed to invoke method: " + mtd, e);
        }
        catch (IllegalAccessException e) {
            throw new GridException("Failed to invoke method: " + mtd, e);
        }
    }
}