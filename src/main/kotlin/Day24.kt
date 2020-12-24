import java.io.File
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    println("Advent of Code day 24")

val testData = """sesenwnenenewseeswwswswwnenewsewsw
neeenesenwnwwswnenewnwwsewnenwseswesw
seswneswswsenwwnwse
nwnwneseeswswnenewneswwnewseswneseene
swweswneswnenwsewnwneneseenw
eesenwseswswnenwswnwnwsewwnwsene
sewnenenenesenwsewnenwwwse
wenwwweseeeweswwwnwwe
wsweesenenewnwwnwsenewsenwwsesesenwne
neeswseenwwswnwswswnw
nenwswwsewswnenenewsenwsenwnesesenew
enewnwewneswsewnwswenweswnenwsenwsw
sweneswneswneneenwnewenewwneswswnese
swwesenesewenwneswnwwneseswwne
enesenwswwswneneswsenwnewswseenwsese
wnwnesenesenenwwnenwsewesewsesesew
nenewswnwewswnenesenwnesewesw
eneswnwswnwsenenwnwnwwseeswneewsenese
neswnwewnwnwseenwseesewsenwsweewe
wseweeenwnesenwwwswnew"""

    // Setup - Parse the list of input hex directions.
    val directions = ArrayList<ArrayList<Direction>>()
    //testData.split('\n').forEach { line ->
    File("day24input").forEachLine { line ->
        val theseDirections = ArrayList<Direction>()
        var thisLine = line
        while (thisLine.isNotEmpty()) {
            val d = thisLine.first()
            thisLine = thisLine.drop(1)
            when (d) {
                'e' -> { theseDirections.add(Direction.EAST) }
                'w' -> {theseDirections.add(Direction.WEST) }
                'n' -> {
                    val d = thisLine.first()
                    thisLine = thisLine.drop(1)
                    when (d) {
                        'e' -> { theseDirections.add(Direction.NORTHEAST) }
                        'w' -> {theseDirections.add(Direction.NORTHWEST) }
                    }
                }
                's' -> {
                    val d = thisLine.first()
                    thisLine = thisLine.drop(1)
                    when (d) {
                        'e' -> { theseDirections.add(Direction.SOUTHEAST) }
                        'w' -> {theseDirections.add(Direction.SOUTHWEST) }
                    }
                }
            }
        }
        directions.add(theseDirections)
    }

    // Part 1 - follow the directions and flip the tiles at those locations
    val blackList = hashSetOf<HexTile>()
    directions.forEach { moveList ->
        val thisTile = HexTile(0, 0, 0)
        moveList.forEach { direction ->
            thisTile.move(direction)
        }
        if (blackList.contains(thisTile))
            blackList.remove(thisTile)
        else
            blackList.add(thisTile)
    }
    println("Part 1 - ${blackList.size} tiles are black")

    // Part 2 - play another cellular automata game, yay!
    // Collect the tiles to test - all the neighbors of the currently black tiles
    val timeMs = measureTimeMillis {
        repeat(100) {
            val tilesToTest = mutableSetOf<HexTile>()
            tilesToTest.addAll(blackList)
            val tilesToFlipBlack = mutableSetOf<HexTile>()
            val tilesToFlipWhite = mutableSetOf<HexTile>()
            blackList.forEach { tilesToTest.addAll(it.neighbors) }

            // Test the tiles
            tilesToTest.forEach {
                if (blackList.contains(it))
                    when (it.neighborCountIn(blackList)) {
                        0, in (3..6) -> tilesToFlipWhite.add(it)
                    }
                else if (it.neighborCountIn(blackList) == 2)
                    tilesToFlipBlack.add(it)
            }

            // Flip the tiles
            blackList.addAll(tilesToFlipBlack)
            blackList.removeAll(tilesToFlipWhite)
        }
    }
    println("Part 2 - ${blackList.size} tiles are black")
    println("Part 2 took $timeMs ms")

    println("Done")
}

data class HexTile(var x:Int, var y:Int, var z:Int) {
    val neighbors: List<HexTile> by lazy {
        listOf(HexTile(x+1, y-1, z),
               HexTile(x, y-1, z+1),
               HexTile(x-1, y, z+1),
               HexTile(x-1, y+1, z),
               HexTile(x, y+1, z-1),
               HexTile(x+1, y, z-1))
    }

    fun neighborCountIn(tileSet: Set<HexTile>) = tileSet.filter { neighbors.contains(it) }.count()

    fun move(dir: Direction) {
        when (dir) {
            Direction.EAST -> { x += 1; y -= 1 }
            Direction.SOUTHEAST -> { y -= 1; z += 1 }
            Direction.SOUTHWEST -> { x -= 1; z += 1 }
            Direction.WEST -> { x -= 1; y += 1 }
            Direction.NORTHWEST -> {y += 1; z -= 1 }
            Direction.NORTHEAST -> { x +=1; z -=1 }
        }
    }
}

enum class Direction { EAST, SOUTHEAST, SOUTHWEST, WEST, NORTHWEST, NORTHEAST }
