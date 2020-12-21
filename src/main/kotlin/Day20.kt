import java.awt.Point
import java.io.File
import kotlin.math.roundToInt

fun main(args: Array<String>) {
    println("Advent of Code day 20")

    //                  #
    //#    ##    ##    ###
    // #  #  #  #  #  #
    val MONSTER_PATTERN = arrayListOf(
            Point(18, 0),
            Point(0, 1),
            Point(5, 1),
            Point(6, 1),
            Point(11, 1),
            Point(12, 1),
            Point(17, 1),
            Point(18, 1),
            Point(19, 1),
            Point(1, 2),
            Point(4, 2),
            Point(7, 2),
            Point(10, 2),
            Point(13, 2),
            Point(16, 2),
    )

    // Setup - Parse the image tiles.
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Part 1 - Find the corners and get the product of their id numbers. Corners will have two matching tiles.       //
    // Also store some data helpful for part 2.                                                                       //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    tiles.forEach { tile -> tile.matchingTiles = tiles.filterNot { it.number == tile.number }.filter { tile.matchesTile(it) } }
    val answer1 = tiles.filter { it.matchingTiles.size == 2 }.fold(1L) { prod, tile -> prod * tile.number }
    println("The product of the corner tiles is $answer1")

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Part 2 - Actually solve the puzzle and scan the image for sea monsters                                         //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    val solution = ArrayList<Array<Tile>>()
    val currentRow = ArrayList<Tile>()
    val sideLength = kotlin.math.sqrt(tiles.size.toDouble()).roundToInt()

    //// Step 1 - Put a corner tile in the top left of the solution in the correct orientation
    // Find a corner tile
    tiles.first { it.matchingTiles.size == 2 }.let { match ->
        // Orient the corner piece until its matches fit on the right and bottom sides
        while (!match.matchingEdges.containsAll(listOf(1, 2))) {
            match.transform()
        }
        currentRow.add(match)
    }

    //// Step 2 - Solve the first row.
    // Find all right edge matching tiles until finding another corner and orient correctly
    // This is our first row
    while (currentRow.size < sideLength) {
        val lastTile = currentRow.last()
        val thisTile = lastTile.matchForEdge(1)
        while (thisTile.matchesExactEdge(lastTile.getEdge(1)) != 3) {
            thisTile.transform()
        }
        currentRow.add(thisTile)
    }
    solution.add(currentRow.toTypedArray())
    currentRow.clear()

    //// Step 3 - Solve each remaining row
    // Solve the next row by finding the bottom edge matching tiles for the entire row
    while (solution.size < sideLength) {
        while (currentRow.size < sideLength) {
            val lastTile = solution.last()[currentRow.size]
            val thisTile = lastTile.matchForEdge(2)
            while (thisTile.matchesExactEdge(lastTile.getEdge(2)) != 0)
                thisTile.transform()
            currentRow.add(thisTile)
        }
        solution.add(currentRow.toTypedArray())
        currentRow.clear()
    }

    //// Step 4 - Find the sea monsters
    // Trim all the tile edges
    solution.forEach{ it.forEach {it.trim()} }

    // The puzzle is now solved
    // Convert the map to a 2D char array
    var seaMap = ArrayList<CharArray>()
    solution.forEach { row ->
        for (i in 0..row[0].edgeLength-1) {
            seaMap.add((row.fold("") { msg, tile -> msg + tile.getLine(i)}).toCharArray())
        }
    }
    var finalMap = seaMap.toTypedArray()

    // Orient the map until we find the sea monsters
    var monsterCount = patternMatchCount(MONSTER_PATTERN, finalMap)
    var iterations = 0
    while(monsterCount == 0){
        if (iterations++ == 3)
            finalMap.reverse()
        else
            finalMap = rotate(finalMap)
        monsterCount = patternMatchCount(MONSTER_PATTERN, finalMap)
    }

    val answer = finalMap.fold(0) { sum, row -> sum + row.count { it == '#'} } - (monsterCount * MONSTER_PATTERN.size)
    println("There are $monsterCount sea monsters in the water")
    println("The water roughness is $answer")
}

// Count the number of pattern matches in the given data
fun patternMatchCount(pattern: ArrayList<Point>, data: Array<CharArray>): Int {
    var result = 0
    val patternMaxCol = (pattern.maxByOrNull { it.x }?:Point(0,0)).x
    val patternMaxRow = (pattern.maxByOrNull { it.y }?:Point(0,0)).y

    for (row in 0 until data.size-patternMaxRow) {
        for (col in 0 until data[0].size-patternMaxCol) {
            if (pattern.all { data[it.y+row][it.x+col] == '#' }) result ++
        }
    }
    return result
}

// Rotates the data in a square 2D array
fun rotate(data: Array<CharArray>): Array<CharArray> {
    val size: Int = data.size
    val result = Array(size) { CharArray(size) }
    for (i in 0 until size)
        for (j in 0 until size)
            result[i][j] = data.get(size - j - 1).get(i)
    return result
}

class Tile(val number: Long, private var data: Array<CharArray>) {

    lateinit var matchingTiles: List<Tile>      // The other tiles this one matches with
    var orientation = 0     // The current orientation of the tile

    // Return which edges this tile has matching tiles for
    val matchingEdges get () = matchingTiles.map { matchesAnyEdge(it) }
    // The length in characters of an edge
    val edgeLength get() = data[0].size

    // Get the edge from each possible tile orientation
    fun getEdge(num: Int): CharArray {
        return when (num) {
            0 -> { data.first() }                                       // Top
            1 -> { data.map { it.last() }.toCharArray() }               // Right
            2 -> { data.last()}                                         // Bottom
            3 -> { data.map { it.first() }.toCharArray() }              // Left
            4 -> { data.first().reversed().toCharArray() }              // Top flipped
            5 -> { data.map { it.last() }.reversed().toCharArray() }    // Right flipped
            6 -> { data.last().reversed().toCharArray() }               // Bottom flipped
            7 -> { data.map { it.first() }.reversed().toCharArray() }   // Right flipped
            else -> "..........".toCharArray()
        }
    }

    // Gets a line of data as a string (for printing).
    fun getLine(num: Int) = String(data[num])

    // Rotate or flip the tile to traverse through each possible orientation
    fun transform() {
        when (orientation) {
            0,1,2,4,5,6 -> {
                data = rotate(data)
                orientation++
            }
            3,7 -> {
                data.reverse()
                if (++orientation == 8) orientation = 0
            }
        }
    }

    // Remove all the edge data
    fun trim() {
        val newData = ArrayList<String>()
        for (i in 1..data.size-2) newData.add(String(data[i]))
        data = newData.map { it.drop(1).dropLast(1).toCharArray() }.toTypedArray()
    }

    // Returns true if this tile matches the given tile on any edges
    fun matchesTile(other: Tile): Boolean {
        for (i in 0..7) {
            for (j in 0..7) {
                if (getEdge(i).contentEquals(other.getEdge(j))) return true
            }
        }
        return false
    }

    // Return which edge of this tile another tile matches.
    fun matchesAnyEdge(other: Tile): Int {
        for (i in 0..7) {
            for (j in 0..7) {
                if (getEdge(i).contentEquals(other.getEdge(j))) { return i }
            }
        }
        return Int.MIN_VALUE
    }

    // Return which edge matches the given edge data.
    fun matchesExactEdge(edge: CharArray): Int {
        for (i in 0..7) {
            if (getEdge(i).contentEquals(edge)) { return i }
        }
        return Int.MIN_VALUE
    }

    // Returns the tile that matches the given edge from the list of known matches
    fun matchForEdge(edge: Int) = matchingTiles.first { matchesAnyEdge(it) == edge }

    // For printing out a tile.
    override fun toString(): String {
        var result = "Tile $number:\n"
        data.forEach { result += String(it) + "\n" }
        return result
    }
}
