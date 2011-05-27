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
import java.math._
import org.gridgain.grid.typedef.{CI1, CX1}
import org.gridgain.grid._
import GridClosureCallMode._
import resources._
import org.jetbrains.annotations.Nullable

/**
 * This example recursively calculates `Fibonacci` numbers on the grid. This is
 * a powerful design pattern which allows for creation of fully distributively recursive
 * (a.k.a. nested) tasks or closures with continuations. This example also shows
 * usage of `continuations`, which allows us to wait for results from remote nodes
 * without blocking threads.
 * <p>
 * Note that because this example utilizes local node storage via `GridNodeLocal`,
 * it gets faster if you execute it multiple times, as the more you execute it,
 * the more values it will be cached on remote nodes.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
object ScalarContinuationExample {
    def main(args: Array[String]) {
        scalar {
            // Calculate fibonacci for N.
            val N: Long = 200

            val thisNodeId = grid$.localNode.id

            val start = System.currentTimeMillis

            // Predicate that excludes this node.
            val p = (n: GridRichNode) => grid$.remoteNodes().isEmpty || n.id != thisNodeId

            val fib = grid$.call(UNICAST, new CX1[Long, BigInteger]() {
                // These fields must be *transient* so they do not get
                // serialized and sent to remote nodes.
                // However, these fields will be preserved locally while
                // this closure is being "held", i.e. while it is suspended
                // and is waiting to be continued.
                @transient private var fut1, fut2: GridFuture[BigInteger] = null

                // Auto-inject job context.
                @GridJobContextResource
                private var jobCtx: GridJobContext = null

                @Nullable override def applyx(num: Long): BigInteger = {
                    if (fut1 == null || fut2 == null) {
                        println(">>> Starting fibonacci execution for number: " + num)

                        // Make sure n is not negative.
                        val n = math.abs(num)

                        val g = grid$

                        if (n <= 2)
                            return if (n == 0)
                                BigInteger.ZERO
                            else
                                BigInteger.ONE

                        // Get properly typed node-local storage.
                        val store = g.nodeLocal[Long, GridFuture[BigInteger]]

                        // Check if value is cached in node-local store first.
                        fut1 = store.get(n - 1)
                        fut2 = store.get(n - 2)

                        // If future is not cached in node-local store, cache it.
                        // Note recursive grid execution!
                        if (fut1 == null)
                            fut1 = store.addIfAbsent(n - 1, g.callAsync(UNICAST, this, n - 1, p))

                        // If future is not cached in node-local store, cache it.
                        if (fut2 == null)
                            fut2 = store.addIfAbsent(n - 2, g.callAsync(UNICAST, this, n - 2, p))

                        // If futures are not done, then wait asynchronously for the result
                        if (!fut1.isDone || !fut2.isDone) {
                            val lsnr = new CI1[GridFuture[BigInteger]]() {
                                override def apply(f: GridFuture[BigInteger]) {
                                    // This method will be called twice, once for each future.
                                    // On the second call - we have to have both futures to be done
                                    // - therefore we can call the continuation.
                                    if (fut1.isDone && fut2.isDone)
                                        // Resume job execution.
                                        jobCtx.callcc()
                                }
                            };

                            // Attach the same listener to both futures.
                            fut1.listenAsync(lsnr)
                            fut2.listenAsync(lsnr)

                            // Hold (suspend) job execution.
                            // It will be resumed in listener above via 'callcc()' call
                            // once both futures are done.
                            return jobCtx.holdcc();
                        }
                    }

                    assert(fut1.isDone && fut2.isDone)

                    // Return cached results.
                    fut1.get.add(fut2.get)
                }
            }, N, p)

            val duration = System.currentTimeMillis - start

            println(">>>")
            println(">>> Finished executing Fibonacci for '" + N + "' in " + duration + " ms.")
            println(">>> Fibonacci sequence for input number '" + N + "' is '" + fib + "'.")
            println(">>> You should see prints out every recursive Fibonacci execution on grid nodes.")
            println(">>> Check remote nodes for output.")
            println(">>>")
        }
    }
}