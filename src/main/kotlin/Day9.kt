import java.io.File

fun main(args: Array<String>) {
    println("Advent of Code day 9")

    // Constants
    val preambleLength = 25

    // Setup - Read the list of input data
    val data = ArrayList<Long>()
    File("day9input").forEachLine { data.add(it.toLong()) }

    // Part 1 - Find the first number in the list that is not a sum of two of the working set
    val workingSet = data.subList(0, preambleLength)
    var currentIndex = preambleLength

    var target = 0L
    do {
        target = data[currentIndex++]
        var valid = false
        outerLoop@ for (i in 0..workingSet.size - 1) {
            for (j in i + 1..workingSet.size - 1) {
                if (workingSet[i] + workingSet[j] == target) {
                    valid = true
                    break@outerLoop
                }
            }
        }
        if (valid) {
            workingSet.removeAt(0)
            workingSet.add(target)
        }
    } while (valid)
    println("The first nonconforming value is $target")

    // Part 2 - Find the range in the data that adds up to the target
    var startingIndex = 0
    var complete = false

    do {
        var sum = 0L
        currentIndex = startingIndex++
        do { sum += data[currentIndex++] } while (sum < target)
        complete = (sum == target)
    } while (!complete)

    // The answer is the sum of the min and max values in the range.
    val subList = data.subList(startingIndex - 1, currentIndex)
    println("Sublist size: ${subList.size}; min + max: ${(subList.minByOrNull { it }?:0L) + (subList.maxByOrNull { it }?:0L)}")
}
