// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.cache.jta.jndi;

import org.gridgain.grid.*;
import org.gridgain.grid.cache.jta.*;
import org.jetbrains.annotations.*;

import javax.naming.*;
import javax.transaction.*;
import java.util.*;

/**
 * Implementation of {@link GridCacheTmLookup} interface that is using list of JNDI names to find TM.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridCacheJndiTmLookup implements GridCacheTmLookup {
    /** */
    private List<String> jndiNames;

    /**
     * Gets a list of JNDI names.
     * 
     * @return List of JNDI names that is used to find TM.
     */
    public List<String> getJndiNames() {
        return jndiNames;
    }

    /**
     * Sets a list of JNDI names used by this TM.
     * 
     * @param jndiNames List of JNDI names that is used to find TM.
     */
    public void setJndiNames(List<String> jndiNames) {
        this.jndiNames = jndiNames;
    }

    /** {@inheritDoc} */
    @Nullable @Override public TransactionManager getTm() throws GridException {
        assert jndiNames != null;
        assert !jndiNames.isEmpty();

        try {
            InitialContext ctx = new InitialContext();

            for (String s : jndiNames) {
                Object obj = ctx.lookup(s);

                if (obj != null && obj instanceof TransactionManager) {
                    return (TransactionManager) obj;
                }
            }
        }
        catch (NamingException e) {
            throw new GridException("Unable to lookup TM by: " + jndiNames, e);
        }

        return null;
    }
}