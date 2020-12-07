import java.io.File
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    println("Advent of Code day 6")

    // Setup - Read the data and parse into sets of group data
    val groups: ArrayList<ArrayList<Set<Char>>> = ArrayList()
    var timeMs = measureTimeMillis {
        var groupAnswers = ArrayList<Set<Char>>()
        File("day6input").forEachLine {
            if (it == "") {
                groups.add(groupAnswers)
                groupAnswers = ArrayList()
            } else {
                groupAnswers.add(it.toSet())
            }
        }
        groups.add(groupAnswers)
    }
    println("Parsing group data took $timeMs ms")

    // Part 1 - Get the count of unique answers per group
    var totalUniqueAnswers = 0
    timeMs = measureTimeMillis {
        groups.forEach { group ->
            val uniqueAnswers = hashSetOf<Char>()
            group.forEach { uniqueAnswers.addAll(it) }
            totalUniqueAnswers += uniqueAnswers.size
        }
    }
    println("The sum of unique answers per group is $totalUniqueAnswers")
    println("Part 1 solution took $timeMs ms")

    // Part 2 - Get the count of common answers per group
    var totalCommonAnswers = 0
    timeMs = measureTimeMillis {
        groups.forEach { group ->
            var commonAnswers = group[0]
            for (i in 1..group.size - 1) {
                commonAnswers = commonAnswers.intersect(group[i])
            }
            totalCommonAnswers += commonAnswers.size
        }
    }
    println("The sum of unique answers per group is $totalCommonAnswers")
    println("Part 2 solution took $timeMs ms")
}
