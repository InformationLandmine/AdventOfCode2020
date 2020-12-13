import java.io.File

fun main(args: Array<String>) {
    println("Advent of Code day 13")

    // Setup - Read the list of input data.
    val input = File("day13input").readLines()
    val departTime = input[0].toInt()       // First line is the departure time.

    // Store the data in pairs of (Bus number, wait time).
    val schedule: ArrayList<Pair<Long, Long>> = ArrayList()
    input[1].split(',').forEachIndexed { index, bus ->
        if (bus.toLongOrNull() != null) {
            // If the wait time is longer than the bus number, reduce it.
            schedule.add(Pair(bus.toLong(), (index % bus.toLong())))
        }
    }

    // Some test data given in the problem.
    val testSchedule1: ArrayList<Pair<Long, Long>> = arrayListOf(Pair(7L, 0L), Pair(13L, 1L), Pair(59L, 4L), Pair(31L, 6L), Pair(19L, 7L))
    val testSchedule2: ArrayList<Pair<Long, Long>> = arrayListOf(Pair(17L, 0L), Pair(13L, 2L), Pair(19L, 3L))
    val testSchedule3: ArrayList<Pair<Long, Long>> = arrayListOf(Pair(67L, 0L), Pair(7L, 1L), Pair(59L, 2L), Pair(61L, 3L))
    val testSchedule4: ArrayList<Pair<Long, Long>> = arrayListOf(Pair(67L, 0L), Pair(7L, 2L), Pair(59L, 3L), Pair(61L, 4L))
    val testSchedule5: ArrayList<Pair<Long, Long>> = arrayListOf(Pair(67L, 0L), Pair(7L, 1L), Pair(59L, 3L), Pair(61L, 4L))
    val testSchedule6: ArrayList<Pair<Long, Long>> = arrayListOf(Pair(1789L, 0L), Pair(37L, 1L), Pair(47L, 2L), Pair(1889L, 3L))

    // Part 1 - Find the next departing bus after the given departure time.
    val nextBus = schedule.minByOrNull { it.first - departTime % it.first }?:Pair(0L ,0L)
    println("The next bus is number ${nextBus.first} which arrives in ${nextBus.first - departTime % nextBus.first} minutes")
    println("Part 1 answer is ${nextBus.first * (nextBus.first - departTime % nextBus.first)}")

    // Part 2 - Find the time at which the schedule matches the input.
    val scheduleToTest = schedule
    var time = 0L

    // The first bus is here now at time = 0, and will only ever be here again at its numbered interval,
    // so start by testing times in increments of the first bus number.
    var increment = scheduleToTest.removeAt(0).first

    // Keep testing until all buses meet the specified schedule.
    while (scheduleToTest.isNotEmpty()) {
        // Find any buses that match the schedule at the current time. Change the increment to be the product of
        // itself and the matching bus number, as those intervals are the only ones that can match. Then keep
        // searching for matches with the other buses.
        time += increment
        val matches = scheduleToTest.filter { time % it.first == it.first - it.second }
        matches.forEach { increment *= it.first }
        scheduleToTest.removeAll(matches)
    }

    println("Part 2: The buses will match the given schedule at departure time ${time}")
}
