import java.io.File

fun main(args: Array<String>) {
    println("Advent of Code day 20")

    // Setup - Read the list of input data
    val tiles = ArrayList<Tile>()
    var tileNum = 0L
    val data = ArrayList<CharArray>()
    File("day20input").forEachLine { line ->
        when {
            line.startsWith("Tile") -> { tileNum = line.split(' ', ':')[1].toLong() }
            line != "" -> {  data.add(line.toCharArray()) }
            else -> {
                tiles.add(Tile(tileNum, data.toTypedArray()))
                data.clear()
            }
        }
    }

    // Part 1 - Find the corners and get the product of their id numbers.
    tiles.forEach { tile -> tile.numMatches = tiles.filterNot { it.number == tile.number }.filter { tile.matchesTile(it) }.count() }
    val answer1 = tiles.filter { it.numMatches == 2 }.fold(1L) { prod, tile -> prod * tile.number }
    println("The product of the corner tiles is $answer1")

    // Part 2 - Actually solve the puzzle and scan the image for sea monsters

    val solution = ArrayList<ArrayList<Pair<Tile, Int>>>()
    // Find a corner tile
    // Store in the top left corner
    // Find a matching tile for one edge
    // Orient the corner so the matching edge is on the right
    // This is our setup to solve the first row

    // Find all right edge matching tiles until finding another corner and orient correctly
    // This is our first row

    // Solve the next row by finding the bottom edge matching tiles for the entire row
    // repeat until the last row (when we find a corner)

    // The puzzle is now solved
    // retrieve the actual image data
    // search for sea monsters (may have to change orientation)



    println("There are ${tiles.count()} tiles")
}

class Tile(val number: Long, val data: Array<CharArray>) {

    var numMatches = 0      // The number of other tiles this one matches with

    fun getEdge(num: Int): CharArray {
        return when (num) {
            0 -> { data.first() }
            1 -> { data.map { it.last() }.toCharArray() }
            2 -> { data.last()}
            3 -> {data.map { it.first() }.toCharArray() }
            4 -> { data.first().reversed().toCharArray() }
            5 -> {data.map { it.last() }.reversed().toCharArray() }
            6 -> { data.last().reversed().toCharArray() }
            7 -> { data.map { it.first() }.reversed().toCharArray() }
            else -> "..........".toCharArray()
        }
    }

    fun matchesTile(other: Tile):Boolean {
        for (i in 0..7) {
            for (j in 0..7) {
                if (getEdge(i).contentEquals(other.getEdge(j))) return true
            }
        }
        return false
    }

    fun matchesEdge(other: Tile, thisEdge: Int, otherEdge: Int) = getEdge(thisEdge).contentEquals(other.getEdge(otherEdge))

}
