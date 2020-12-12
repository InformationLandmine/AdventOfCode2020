import java.io.File

fun main(args: Array<String>) {
    println("Advent of Code day 10")

    // Setup - Read the list of input data
    val data = ArrayList<Int>()
    File("day10input").forEachLine { data.add(it.toInt()) }
    data.add(0) // Add 0 to represent the input joltage
    data.sort() // Both parts of the problem require the adapters to be sorted by joltage

    // Part 1 - Count the number of joltage diffs of 1 and 3
    var joltDiff1 = 0
    var joltDiff3 = 0
    var currentJolts = 0
    data.forEach {
        when (it - currentJolts) {
            1 -> joltDiff1++
            3 -> joltDiff3++
        }
        currentJolts = it
    }
    joltDiff3++ // The final connection to your device counts as a joltage difference of 3

    println("There are $joltDiff1 joltage differences of 1 and $joltDiff3 joltage differences of 3 in your adapter chain")
    println("The product of the count of joltage differences is ${joltDiff1 * joltDiff3}")

    // Part 2 - Of course it couldn't be that easy!
    //          Now find out how many different adapter configurations will work

    // Start by breaking the data set into groups of sequential joltages that have a jump of 3 before and after
    val groups = ArrayList<Int>()
    currentJolts = -1
    var currentGroupSize = 0
    data.forEach {
        when (it - currentJolts) {
            1 -> currentGroupSize++
            3 -> {
                groups.add(currentGroupSize)
                currentGroupSize = 1
            }
        }
        currentJolts = it
    }
    groups.add(currentGroupSize)
    println("There are ${groups.size} groups and the largest sequential group size is ${groups.maxByOrNull { it }}")

    // Each group has a certain number of valid combinations. The product of the number of each group's number of
    // combinations is the final number of valid combinations for the entire set of adapters.
    // I don't know how to derive the formula for any given n group size, but I worked it out by hand for up to 6.
    val finalResult = groups.fold(1L) {acc, it ->
        when (it) {
            1 -> acc * 1
            2 -> acc * 1
            3 -> acc * 2
            4 -> acc * 4
            5 -> acc * 7
            6 -> acc * 13
            else -> acc * 0 // If the final answer is 0, there was a group that was too large to calculate
        }
    }
    println("There are $finalResult ways to combine the adapters to power your device")
}
