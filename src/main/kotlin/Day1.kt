import java.io.File
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    println("Advent of Code day 1")

    val target = 2020
    val input = ArrayList<Int>()
    File("day1input").forEachLine { input.add(it.toInt()) }

    var timeMs = measureTimeMillis {
        outerLoop@for (i in 0..input.size - 1) {
            for (j in i + 1..input.size - 1) {
                if (input[i] + input[j] == target) {
                    println("${input[i]} + ${input[j]} = $target")
                    println("${input[i]} * ${input[j]} = ${input[i] * input[j]}")
                    break@outerLoop
                }
            }
        }
    }
    println("Solution took $timeMs ms")

    // Part 2
    timeMs = measureTimeMillis {
        outerLoop@for (i in 0..input.size - 1) {
            for (j in i + 1..input.size - 1) {
                for (k in j + 1..input.size - 1) {
                    if (input[i] + input[j] + input[k] == target) {
                        println("${input[i]} + ${input[j]} + ${input[k]} = $target")
                        println("${input[i]} * ${input[j]} * ${input[k]} = ${input[i] * input[j] * input[k]}")
                        break@outerLoop
                    }
                }
            }
        }
    }
    println("Solution took $timeMs ms")
}
