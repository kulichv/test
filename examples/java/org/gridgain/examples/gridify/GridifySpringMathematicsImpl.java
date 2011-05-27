// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.examples.gridify;

import org.gridgain.grid.gridify.*;
import org.gridgain.grid.typedef.*;
import java.util.*;

/**
 * Bean implementation for Spring AOP-based annotations example.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
public class GridifySpringMathematicsImpl implements GridifySpringMathematics {
    /**
     * Find maximum value in collection.
     * Method grid-enabled with {@link GridifySetToValue} annotation.
     * Note that {@link GridifySetToValue} annotation
     * is attached to {@link GridifySpringMathematics#findMaximum(Collection)} method on the
     * interface.
     *
     * @param input Input collection.
     * @return Maximum value.
     */
    @Override public Long findMaximum(Collection<Long> input) {
        X.println(">>>");
        X.println("Find maximum in: " + input);
        X.println(">>>");

        return Collections.max(input);
    }

    /**
     * Find prime numbers in collection.
     * Method grid-enabled with {@link GridifySetToSet} annotation.
     * Note that {@link GridifySetToSet} annotation
     * is attached to this method on the interface.
     *
     * @param input Input collection.
     * @return Prime numbers.
     */
    @Override public Collection<Long> findPrimes(Collection<Long> input) {
        X.println(">>>");
        X.println("Find primes in: " + input);
        X.println(">>>");

        Collection<Long> res = new ArrayList<Long>();

        for (Long val : input) {
            Long divisor = GridSimplePrimeChecker.checkPrime(val, 2, val);

            if (divisor == null)
                res.add(val);
        }

        return res;
    }
}
