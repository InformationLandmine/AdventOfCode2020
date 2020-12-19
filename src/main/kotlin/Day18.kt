import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    println("Advent of Code day 18")

    // Define some test input
    val testInput0 = "1 + (2 * 3) + (4 * (5 + 6))"
    val testInput1 = "2 * 3 + (4 * 5)"
    val testInput2 = "5 + (8 * 3 + 9 + 3 * 4 * 3)"
    val testInput3 = "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"
    val testInput4 = "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"

    // Read the input file and remove all whitespace so we can parse one character at a time.
    val input = ArrayList<String>()
    File("day18input").forEachLine { input.add(it.replace("\\s".toRegex(), "")) }

    // Part 1 - evaluate the expressions
    var answer = input.fold(0L) { sum, expr -> sum + eval1(expr) }
    println("Part 1: The sum of all expressions is $answer")

    // Part 2 - evaluate the expressions with "advanced" math
    answer = input.fold(0L) { sum, expr -> sum + eval2(expr) }
    println("Part 2: The sum of all expressions is $answer")
}

fun eval1(expr: String): Long {
    var result = 1L
    var op = ::opMult
    val opStack = Stack<Pair<Long, (a: Long, b: Long) -> Long>>()

    expr.forEach {
        when (it) {
            '+' -> { op = ::opAdd }
            '*' -> { op = ::opMult }
            '(' -> {
                opStack.push(Pair(result, op))
                result = 1L
                op = ::opMult
            }
            ')' -> {
                result = with(opStack.pop()) { second(first, result) }
            }
            else -> { result = op(result, it.toString().toLong()) }
        }
    }
    return result
}

fun eval2(expr: String): Long {
    var result = 0L
    var op = ::opAdd
    val opStack = Stack<Pair<Long, (a: Long, b: Long) -> Long>>()

    expr.forEach {
        when (it) {
            '+' -> { op = ::opAdd }
            '*' -> {
                opStack.push(Pair(result, ::opMult))
                result = 0L
                op = ::opAdd
            }
            '(' -> {
                opStack.push(Pair(result, op))
                result = 0L
                op = ::opAdd
            }
            ')' -> {
                while (opStack.isNotEmpty() && opStack.peek().second == ::opMult) {
                    result = with(opStack.pop()) { second(first, result) }
                }
                if (opStack.isNotEmpty() && opStack.peek().second == ::opAdd) {
                    result = with(opStack.pop()) { second(first, result) }
                }
            }
            else -> { result = op(result, it.toString().toLong()) }
        }
    }
    while (opStack.isNotEmpty()) {
        result = with(opStack.pop()) { second(first, result) }
    }
    return result
}

fun opMult(a: Long, b: Long) = a * b
fun opAdd(a: Long, b: Long) = a + b
