import java.io.File

fun main(args: Array<String>) {
    println("Advent of Code day 5")

    // Setup - load boarding pass data
    val boardingPasses = ArrayList<BoardingPass>()
    File("day5input").forEachLine { boardingPasses.add(BoardingPass(it)) }
    println("loaded ${boardingPasses.size} boarding passes")

    // Part 1 - Get the highest numbered seat
    val highestSeat = boardingPasses.maxByOrNull { it.seat }?.seat
    println("The highest numbered seat is $highestSeat")

    // Part 2 - Find your seat on a full flight
    val allSeats = (0..(highestSeat?:0)).toList()
    val takenSeats = boardingPasses.map { it.seat }
    val vacantSeats = allSeats.minus(takenSeats)
    val mySeat = vacantSeats.filter { !vacantSeats.contains(it - 1) && !vacantSeats.contains(it + 1) }
    println("My seat is $mySeat")
}

class BoardingPass(val encodedSeat: String) {
    val row: Int
        get() {
            var result = 0
            for (i in 0..6) {
                result += when(encodedSeat[i]) {
                    'B' -> 64 shr i
                    else -> 0
                }
            }
            return result
        }

    val col: Int
        get() {
            var result = 0
            for (i in 7..9) {
                result += when(encodedSeat[i]) {
                    'R' -> 4 shr (i - 7)
                    else -> 0
                }
            }
            return result
        }

    val seat: Int
        get() = row * 8 + col
}