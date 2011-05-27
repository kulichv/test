// Copyright (C) GridGain Systems, Inc. Licensed under GPLv3, http://www.gnu.org/licenses/gpl.html

/*
 * ________               ______                    ______   _______
 * __  ___/_____________ ____  /______ _________    __/__ \  __  __ \
 * _____ \ _  ___/_  __ `/__  / _  __ `/__  ___/    ____/ /  _  / / /
 * ____/ / / /__  / /_/ / _  /  / /_/ / _  /        _  __/___/ /_/ /
 * /____/  \___/  \__,_/  /_/   \__,_/  /_/         /____/_(_)____/
 *
 */

package org.gridgain.scalar.examples

import org.gridgain.scalar._
import scalar._
import org.gridgain.grid.GridClosureCallMode._
import org.gridgain.grid.Grid

/**
  * This example calculates Pi number in parallel on the grid.
  *
  * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
  * @version 3.0.9c.27052011
  */
object ScalarPiCalculationExample {
    /** Number of calculations per node. */
    private val N = 10000

    /**
      * Starts examples and calculates Pi number on the grid.
      *
      * @param args Command line arguments - none required.
      */
    def main(args: Array[String]) {
        scalar { g: Grid =>
            println("Pi estimate: " +
                (g @< (SPREAD, for (i <- 0 until g.size()) yield () => calcPi(i * N), (_: Seq[Double]).sum)))
        }
    }

    /**
      * Calculates Pi starting with given number.
      *
      * @param start Start the of the `{start, start + N}` range.
      * @return Range calculation.
      */
    def calcPi(start: Int): Double =
        (start until (start + N)) map (i => 4.0 * (1 - (i % 2) * 2) /  (2 * i + 1)) sum
}