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
import org.gridgain.grid._
import cache._
import affinity._
import java.util._

/**
 * Demonstrates cache ad-hoc queries with Scalar.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
object ScalarCacheQueryExample {
    /** Cache name. */
    private val CACHE_NAME = "partitioned" // "replicated"

    /**
     * Example entry point. No arguments required.
     *
     * @param args Command line arguments. None required.
     */
    def main(args: Array[String]) {
        scalar("examples/config/spring-cache.xml") {
            example(grid$)
        }
    }

    /**
     * Runs the example.
     *
     * @param g Grid instance to use.
     */
    private def example(g: Grid) {
        if (!g.isEnterprise)
            println(">>> NOTE: in Community Edition all queries will be run localy.");

        // Populate cache.
        initialize()

        // Cache instance shortcut.
        val cache = mkCache[GridCacheAffinityKey[UUID], Person]

        // Distributed queries only supported by Enterprise Edition.
        // In Community Edition we'll use local node projection.
        val prj = if (g.isEnterprise) g else g.localNode

        // Example for SQL-based querying employees based on salary ranges.
        // Gets all persons with 'salary > 1000'.
        print("People with salary more than 1000:", cache.sql(prj, "salary > 1000").map(_._2))

        // Example for TEXT-based querying for a given string in people resumes.
        // Gets all persons with 'Bachelor' degree.
        print("People with Bachelor degree:", cache.lucene(prj, "Bachelor").map(_._2))

        // Example for SQL-based querying with custom remote transformer to make sure
        // that only required data without any overhead is returned to caller.
        // Gets last names of all 'GridGain' employees.
        print("Last names of all 'GridGain' employees:",
            cache.sqlTransform(
                prj,
                "from Person, Organization where Person.orgId = Organization.id " +
                    "and Organization.name = 'GridGain'",
                (p: Person) => p.lastName
            ).map(_._2)
        )

        // Example for SQL-based querying with custom remote and local reducers
        // to calculate average salary among all employees within a company.
        // Gets average salary of persons with 'Master' degree.
        print("Average salary of people with Master degree:",
            cache.luceneReduce(
                prj,
                "Master",
                (e: Iterable[(GridCacheAffinityKey[UUID], Person)]) => (e.map(_._2.salary).sum, e.size),
                (e: Iterable[(Double, Int)]) => e.map(_._1).sum / e.map(_._2).sum
            )
        )
    }

    /**
     * Gets instance of typed cache view to use.
     *
     * @return Cache to use.
     */
    private def mkCache[K, V]: GridCacheProjection[K, V] = {
        // In Community Edition queries work only for 'local' cache.
        // Distributed queries aren't support in Community Edition.
        cache$[K, V](if (grid$.isEnterprise) CACHE_NAME else "local").get.flagsOn(GridCacheFlag.SYNC_COMMIT)
    }

    /**
     * Populates cache with test data.
     */
    private def initialize() {
        // Organization cache projection.
        val orgCache = mkCache[UUID, Organization]

        // Organizations.
        val org1 = Organization("GridGain")
        val org2 = Organization("Other")

        orgCache += (org1.id -> org1)
        orgCache += (org2.id -> org2)

        // Person cache projection.
        val prnCache = mkCache[GridCacheAffinityKey[UUID], Person]

        // People.
        val p1 = Person(org1, "John", "Doe", 2000, "John Doe has Master Degree.")
        val p2 = Person(org1, "Jane", "Doe", 1000, "Jane Doe has Bachelor Degree.")
        val p3 = Person(org2, "John", "Smith", 1500, "John Smith has Bachelor Degree.")
        val p4 = Person(org2, "Jane", "Smith", 2500, "Jane Smith has Master Degree.")

        // Note that in this example we use custom affinity key for Person objects
        // to ensure that all persons are collocated with their organizations.
        prnCache += (p1.key -> p1)
        prnCache += (p2.key -> p2)
        prnCache += (p3.key -> p3)
        prnCache += (p4.key -> p4)
    }

    /**
     * Prints object or collection of objects to standard out.
     *
     * @param msg Message to print before object is printed.
     * @param o Object to print, can be `Iterable`.
     */
    private def print(msg: String, o: Any) {
        assert(msg != null)
        assert(o != null)

        println(">>> " + msg)

        o match {
            case it: Iterable[Any] => it.foreach(e => println(">>>     " + e.toString))
            case _ => println(">>>     " + o.toString)
        }
    }
}

/**
 * Organization class.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
private case class Organization(
    @ScalarCacheQuerySqlField
    name: String
) {
    /** Organization ID. */
    @ScalarCacheQuerySqlField
    val id = UUID.randomUUID
}

/**
 * Person class.
 *
 * @author 2005-2011 Copyright (C) GridGain Systems, Inc.
 * @version 3.0.9c.27052011
 */
private case class Person(
    org: Organization,
    firstName: String,
    lastName: String,
    @ScalarCacheQuerySqlField
    salary: Double,
    @ScalarCacheQueryLuceneField
    resume: String
) {
    /** Person ID. */
    val id = UUID.randomUUID

    /** Organization ID. */
    @ScalarCacheQuerySqlField
    val orgId = org.id

    /** Affinity key for this person. */
    val key = new GridCacheAffinityKey[UUID](id, org.id)

    /**
     * `toString` implementation.
     */
    override def toString: String = {
        firstName + " " + lastName + " [salary: " + salary + ", resume: " + resume + "]"
    }
}
