import java.io.File
import java.util.*
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    println("Advent of Code day 22")

    // Define some test data.
    val testP1Deck = arrayListOf(9, 2, 6, 3, 1)
    val testP2Deck = arrayListOf(5, 8, 4, 7, 10)

    // Setup - Read the product ingredient lists
    val player1Deck = ArrayList<Int>()
    val player2Deck = ArrayList<Int>()
    var parsingP1 = true
    File("day22input").forEachLine {
        when {
            it.contains(':') -> {}
            it == "" -> parsingP1 = false
            else -> {
                if (parsingP1) player1Deck.add(it.toInt())
                else player2Deck.add(it.toInt())
            }
        }
    }

    // Part 1 - Play a game of combat.
    playCombat(player1Deck.toMutableList(), player2Deck.toMutableList())

    // Part 2 - Play a game of recursive combat.
    val timeMs = measureTimeMillis {
        var result = playRecursiveCombat(player1Deck.toMutableList(), player2Deck.toMutableList())
        println("${result.first} wins with ${result.second} points")
    }
    println("Recursive combat took $timeMs ms")
}

fun playCombat(p1Deck: MutableList<Int>, p2Deck: MutableList<Int>)
{
    while (p1Deck.isNotEmpty() && p2Deck.isNotEmpty()) {
        val p1Card = p1Deck.removeAt(0)
        val p2Card = p2Deck.removeAt(0)
        if (p1Card > p2Card) { p1Deck.add(p1Card); p1Deck.add(p2Card) }
        else { p2Deck.add(p2Card); p2Deck.add(p1Card) }
    }
    if (p1Deck.isNotEmpty()) {
        val answer = p1Deck.reversed().foldIndexed(0L) { ndx, sum, num -> sum + (num * (ndx + 1) )}
        println("Player 1 scored $answer")
    } else {
        val answer = p2Deck.reversed().foldIndexed(0L) { ndx, sum, num -> sum + (num * (ndx + 1) )}
        println("Player 2 scored $answer")
    }
}

fun playRecursiveCombat(p1Deck: MutableList<Int>, p2Deck: MutableList<Int>): Pair<Winner, Int>
{
    var winner = Winner.NOBODY
    var score = 0
    val gameHistory = ArrayList<Pair<List<Int>, List<Int>>>()

    while (p1Deck.isNotEmpty() && p2Deck.isNotEmpty()) {
        // If the game has been in this state before, end and declare player 1 the winner
        if (gameHistory.contains(Pair(p1Deck.toList(), p2Deck.toList()))) {
            winner = Winner.PLAYER1
            score = p1Deck.reversed().foldIndexed(0) { ndx, sum, num -> sum + (num * (ndx + 1) )}
            break
        }
        // Start this game by recording the state in the game history.
        gameHistory.add(Pair(p1Deck.toList(), p2Deck.toList()))
        val p1Card = p1Deck.removeAt(0)
        val p2Card = p2Deck.removeAt(0)
        if (p1Deck.size >= p1Card && p2Deck.size >= p2Card) {
            // Play recursive combat
            val p1SubDeck = p1Deck.toMutableList().dropLast(p1Deck.size - p1Card).toMutableList()
            val p2SubDeck = p2Deck.toMutableList().dropLast(p2Deck.size - p2Card).toMutableList()

            when (playRecursiveCombat(p1SubDeck, p2SubDeck).first) {
                Winner.PLAYER1 -> { p1Deck.add(p1Card); p1Deck.add(p2Card) }
                Winner.PLAYER2 -> { p2Deck.add(p2Card); p2Deck.add(p1Card) }
            }
        } else {
            // Play like normal
            if (p1Card > p2Card) { p1Deck.add(p1Card); p1Deck.add(p2Card) }
            else                 { p2Deck.add(p2Card); p2Deck.add(p1Card) }
        }
    }
    // Determine the winner and the score.
    if (winner == Winner.NOBODY) {
        if (p1Deck.isEmpty()) {
            winner = Winner.PLAYER2
            score = p2Deck.reversed().foldIndexed(0) { ndx, sum, num -> sum + (num * (ndx + 1) )}
        }
        else {
            winner = Winner.PLAYER1
            score = p1Deck.reversed().foldIndexed(0) { ndx, sum, num -> sum + (num * (ndx + 1) )}
        }
    }
    return Pair(winner, score)
}

enum class Winner {NOBODY, PLAYER1, PLAYER2}