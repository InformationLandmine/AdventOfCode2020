import java.io.File

fun main(args: Array<String>) {
    println("Advent of Code day 11")

    // Setup - Read the list of input data
    val data = ArrayList<CharArray>()
    File("day11input").forEachLine { data.add(it.toCharArray()) }

    var seatMap = SeatMap(data)
    val originalMap = seatMap.copySeats()

    // Part 1
    var iterationCount = 0
    do {
        iterationCount++
        println("There are ${seatMap.occupied} occupied seats")
    } while (seatMap.iteratePart1() != 0)

    println("SeatMap iterated $iterationCount times")
    println("There are ${seatMap.occupied} occupied seats")

    // Part 2
    iterationCount = 0
    seatMap = SeatMap(originalMap)
    do {
        iterationCount++
        println("There are ${seatMap.occupied} occupied seats")
    } while (seatMap.iteratePart2() != 0)

    println("SeatMap iterated $iterationCount times")
    println("There are ${seatMap.occupied} occupied seats")
}

class SeatMap (private var seats: ArrayList<CharArray>) {

    val occupied: Int get() { return seats.fold(0) { total, row -> total + row.count { it == '#'}} }

    private fun directNeighborCount(i: Int, j: Int): Int {
        var result = 0
        // Prior Row
        if (i > 0 && j > 0 && seats[i-1][j-1] == '#') result++
        if (i > 0 && seats[i-1][j] == '#') result++
        if (i > 0 && j < seats[i].size - 1  && seats[i-1][j+1] == '#') result++
        // Same Row
        if (j > 0 && seats[i][j-1] == '#') result++
        if (j < seats[i].size - 1 && seats[i][j+1] == '#') result++
        // Next Row
        if (i < seats.size - 1 && j > 0 && seats[i+1][j-1] == '#') result++
        if (i < seats.size - 1 && seats[i+1][j] == '#') result++
        if (i < seats.size - 1 && j < seats[i].size - 1  && seats[i+1][j+1] == '#') result++
        return result
    }

    private fun visibleNeighborCount(i: Int, j: Int): Int {
        var result = 0

        // Up Left
        var r = i
        var c = j
        while (--r >= 0 && --c >= 0 && seats[r][c] == '.') {}
        if (r >= 0 && c >= 0 && seats[r][c] == '#') result ++
        // Up
        r = i
        c = j
        while (--r >= 0 && seats[r][c] == '.') {}
        if (r >= 0 && seats[r][c] == '#') result ++
        // Up Right
        r = i
        c = j
        while (--r >= 0 && ++c < seats[r].size && seats[r][c] == '.') {}
        if (r >= 0 && c < seats[r].size && seats[r][c] == '#') result ++
        // Left
        r = i
        c = j
        while (--c >= 0 && seats[r][c] == '.') {}
        if (c >= 0 && seats[r][c] == '#') result ++
        // Right
        r = i
        c = j
        while (++c < seats[r].size && seats[r][c] == '.') {}
        if (c < seats[r].size && seats[r][c] == '#') result ++
        // Down Left
        r = i
        c = j
        while (++r < seats.size && --c >= 0 && seats[r][c] == '.') {}
        if (r < seats.size && c >= 0 && seats[r][c] == '#') result ++
        // Down
        r = i
        c = j
        while (++r < seats.size && seats[r][c] == '.') {}
        if (r < seats.size && seats[r][c] == '#') result ++
        // Down Right
        r = i
        c = j
        while (++r < seats.size && ++c < seats[r].size && seats[r][c] == '.') {}
        if (r < seats.size && c < seats[r].size && seats[r][c] == '#') result ++

        return result
    }

    fun iteratePart1(): Int {
        var changedCount = 0
        val seatsCopy = copySeats();

        for (i in 0..seats.size - 1) {
            for (j in 0..seats[i].size - 1) {
                when (seatsCopy[i][j]) {
                    'L' -> { if (directNeighborCount(i, j) == 0) {
                        seatsCopy[i][j] = '#'
                        changedCount++
                    }}
                    '#' -> { if (directNeighborCount(i, j) >= 4) {
                        seatsCopy[i][j] = 'L'
                        changedCount++
                    }}
                }
            }
        }
        seats = seatsCopy
        return changedCount
    }

    fun iteratePart2(): Int {
        var changedCount = 0
        val seatsCopy = copySeats();

        for (i in 0..seats.size - 1) {
            for (j in 0..seats[i].size - 1) {
                when (seatsCopy[i][j]) {
                    'L' -> { if (visibleNeighborCount(i, j) == 0) {
                        seatsCopy[i][j] = '#'
                        changedCount++
                    }}
                    '#' -> { if (visibleNeighborCount(i, j) >= 5) {
                        seatsCopy[i][j] = 'L'
                        changedCount++
                    }}
                }
            }
        }
        seats = seatsCopy
        return changedCount
    }

    fun copySeats(): ArrayList<CharArray> {
        val result = ArrayList<CharArray>()
        seats.forEach { row -> result.add(row.copyOf()) }
        return result
    }
}