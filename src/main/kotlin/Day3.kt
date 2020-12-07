import java.io.File
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    println("Advent of Code day 3")

    // Setup
    val input: ArrayList<String> = ArrayList()
    File("day3input").forEachLine { input.add(it) }
    println("The map is ${input.size} lines long and ${input[0].length} columns wide")

    // Part 1
    var slopesToTest = listOf(MapPoint(3, 1))
    runToboggan(input, slopesToTest)

    // Part 2
    slopesToTest = listOf(MapPoint(1, 1),
                          MapPoint(3, 1),
                          MapPoint(5, 1),
                          MapPoint(7, 1),
                          MapPoint(1, 2),
    )
    runToboggan(input, slopesToTest)
}

fun runToboggan(map: List<String>, slopes: List<MapPoint>) {
    val mapWidth = map[0].length
    var treeAcc = 1
    val timeMs = measureTimeMillis {
        for (slope in slopes) {
            var treeCount = 0
            val location = slope.copy()
            while (location.y < map.size) {
                if (map[location.y][location.x] == '#') treeCount++
                location.move(slope)
                if (location.x >= mapWidth) location.x -= mapWidth
            }
            println("You encountered $treeCount trees for a slope of $slope")
            treeAcc *= treeCount
        }
    }
    println("The accumulated tree count is $treeAcc")
    println("Solution took $timeMs ms")
}

data class MapPoint(var x: Int, var y: Int) {
    fun move (slope: MapPoint) {
        x += slope.x
        y += slope.y
    }
}