import java.io.File
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    println("Advent of Code day 1")

    val target = 2020
    val input: ArrayList<Int> = ArrayList()
    File("day1input").forEachLine {
        input.add(it.toInt())
    }

    val timeMs = measureTimeMillis {
        input.forEach { i: Int ->
            input.forEach { j: Int ->
                input.forEach { k: Int ->
                    if (/*setOf(i, j, k).size == 3 &&*/ i + j + k == target) {
                        println("$i + $j + $k = $target; $i * $j * $k = ${i * j * k}")
                    }
                }
            }
        }
    }
    println("Solution took $timeMs ms")
}

//    input.forEachIndexed { iIndex: Int, i: Int ->
//        input.subList(iIndex, input.size).forEachIndexed { jIndex: Int, j: Int ->
//            input.subList(jIndex, input.size).forEach { k: Int ->
//                if (setOf(i, j, k).size == 3 && i + j + k == 2020) {
//                    println("$i plus $j plus $k = 2020; $i * $j * $k = ${i * j * k}")
//                }
//            }
//        }
//    }
//}