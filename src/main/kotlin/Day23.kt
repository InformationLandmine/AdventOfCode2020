import java.util.*
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    println("Advent of Code day 23")

    // Setup - Define the input data.
    val testCups = listOf(3,8,9,1,2,5,4,6,7)
    val realCups = listOf(5,8,6,4,3,9,1,7,2)

    // Store the cups in a linked list.
    var firstCup = createCupList(realCups)

    // Create a map of values to nodes... this was added so part 2 would run quickly enough.
    var cupLookup = createLookupMap(firstCup)

    // Part 1 - Play the cups game 10 times
    var currentCup = firstCup
    repeat (100) { currentCup = playOptimizedCupsRound(currentCup, 9, cupLookup) }

    var answerCup = cupLookup[1]
    var answer1 = ""
    repeat(8) { answer1 += answerCup?.next?.value; answerCup = answerCup?.next }
    println("Part 1 - The cups after cup 1 are $answer1")

    // Part 2 - SO MANY CUPS LOL
    // Set up the data structures again.
    val evenMoreCups = realCups.toMutableList()
    evenMoreCups.addAll(10..1000000)
    firstCup = createCupList(evenMoreCups)
    cupLookup = createLookupMap(firstCup)

    currentCup = firstCup
    val timeMs = measureTimeMillis {
        repeat(10000000) { currentCup = playOptimizedCupsRound(currentCup, 1000000, cupLookup) }
    }
    val cup1 = cupLookup[1]!!
    println("Part 2 - The product of the two cups after cup 1 is ${cup1.next!!.value.toLong() * cup1.next!!.next!!.value.toLong()}")
    println("Part 2 took $timeMs ms")
}

fun playOptimizedCupsRound(currentCup: CupListNode, maxCupValue: Int, cupMap:HashMap<Int, CupListNode>): CupListNode {
    // Pull out the cups to move
    val cupsToMove = currentCup.next!!
    var nextCup = currentCup
    repeat(3) { nextCup = nextCup.next!! }
    currentCup.next = nextCup.next
    cupsToMove[2]?.next = cupsToMove

    // Find the destination cup
    var destCupNum = currentCup.value - 1
    if (destCupNum == 0) destCupNum = maxCupValue
    while (cupsToMove.find(destCupNum) != null) if (--destCupNum == 0) destCupNum = maxCupValue
    //val destCup = currentCup.find(destCupNum) Part 2: NOPE, FU BUDDY
    val destCup = cupMap[destCupNum]    // Needed the lookup to solve part 2.

    // Insert the moved cups
    cupsToMove[2]?.next = destCup?.next
    destCup?.next = cupsToMove

    return currentCup.next!!
}

// Create a circular linked list from the given values.
fun createCupList(cups: List<Int>): CupListNode {
    val firstCup = CupListNode(cups.first(), null)
    with(cups.subList(1, cups.size)) {
        var prevNode: CupListNode = firstCup
        forEachIndexed { i, value ->
            val newNode = CupListNode(value, null)
            prevNode.next = newNode
            prevNode = newNode
        }
        prevNode.next = firstCup
    }
    return firstCup
}

fun createLookupMap(cupList: CupListNode): HashMap<Int, CupListNode> {
    val cupMap = HashMap<Int, CupListNode>()
    var node = cupList
    while (node.next != cupList) {
        cupMap[node.value] = node
        node = node.next!!
    }
    cupMap[node.value] = node // Make sure to add the last one! -- oops
    return cupMap
}

class CupListNode(val value: Int, var next: CupListNode?) {
    // Support indexing operator.
    operator fun get(index: Int): CupListNode? {
        var result: CupListNode? = this
        repeat(index) { result = result?.next }
        return result
    }
    // Find a node with the target value.
    // This was too slow for part 2 but it is still used for checking the removed cups.
    fun find(target: Int): CupListNode? {
        var node: CupListNode? = this
        while (node?.value != target) {
            node = node?.next
            if (node == this) { node = null; break }
        }
        return node
    }
}
