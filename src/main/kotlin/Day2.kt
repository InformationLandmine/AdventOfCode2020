import java.io.File
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    println("Advent of Code day 2")

    // Part 1
    var validCount = 0
    var invalidCount = 0
    var timeMs = measureTimeMillis {
        File("day2input").forEachLine {
            val parts = it.split('-', ' ', ':')
            val lowBound = parts[0].toInt()
            val highBound = parts[1].toInt()
            val char = parts[2][0]
            val count = parts[4].filter { it == char}.count()
            if (count >= lowBound && count <= highBound) validCount++
            else invalidCount ++
        }

        println("There are $validCount valid passwords and $invalidCount invalid passwords")
    }
    println("Solution took $timeMs ms")

    // Part 2
    validCount = 0
    invalidCount = 0
    timeMs = measureTimeMillis {
        File("day2input").forEachLine {
            val parts = it.split('-', ' ', ':')
            val ndx1 = parts[0].toInt() - 1
            val ndx2 = parts[1].toInt() - 1
            val char = parts[2][0]
            if ((parts[4][ndx1] == char).xor(parts[4][ndx2] == char)) validCount++
            else invalidCount ++
        }

        println("There are $validCount valid passwords and $invalidCount invalid passwords")
    }
    println("Solution took $timeMs ms")
}
