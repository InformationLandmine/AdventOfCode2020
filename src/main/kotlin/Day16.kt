import java.io.File

fun main(args: Array<String>) {
    println("Advent of Code day 14")

    // Setup - Read and store the input data
    val constraints = ArrayList<Constraint>()
    val myTicket = ArrayList<Int>()
    val tickets = ArrayList<IntArray>()

    val input = File("day16input").readLines()
    var phase = ParsePhase.CONSTRAINTS

    // Who needs regex???
    input.forEach {
        if (it != "") {
            when (phase) {
                ParsePhase.CONSTRAINTS -> {
                    if (it == "your ticket:") {
                        phase = ParsePhase.MY_TICKET
                    } else {
                        val parsed = it.split(": ", " or ", "-")
                        constraints.add(Constraint(parsed[0], listOf(
                                IntRange(parsed[1].toInt(), parsed[2].toInt()),
                                IntRange(parsed[3].toInt(), parsed[4].toInt()),
                        )))
                    }
                }
                ParsePhase.MY_TICKET -> {
                    if (it == "nearby tickets:") {
                        phase = ParsePhase.OTHER_TICKETS
                    } else {
                        it.split(',').forEach { num -> myTicket.add(num.toInt()) }
                    }
                }
                ParsePhase.OTHER_TICKETS -> {
                    val newTicket = IntArray(20)
                    it.split(',').forEachIndexed { i, num -> newTicket[i] = num.toInt() }
                    tickets.add(newTicket)
                }
            }
        }
    }

    // Part 1 - Find the invalid tickets and sum an "error rate"
    val invalidTickets = tickets.filterNot { it.all { num -> constraints.any { it.matches(num) } } }
    val errorRate = invalidTickets.fold(0) { error, ticket -> error + ticket.filterNot { num -> constraints.any { it.matches(num) } }.sum() }
    println("Part 1: Total error rate = $errorRate")

    // Part 2 - Determine which values are for which fields
    tickets.removeAll(invalidTickets)
    // Iterate through the tickets and eliminate possible matching fields from constraints that the ticket data doesn't fit.
    tickets.forEach { ticket ->
        constraints.forEach { constraint ->
            constraint.matchingFields.removeAll(ticket.filterNot { num -> constraint.matches(num) }.map { ticket.indexOf(it) })
        }
    }
    // That narrowed it down but didn't solve it completely.
    // Find the solved constraint and remove the matching field from all others until each is solved.
    while (constraints.any { it.matchingFields.size > 1 }) {
        constraints.filter { it.matchingFields.size > 1 }.forEach { constraint ->
            constraint.matchingFields.removeIf { index -> constraints.filter { it.matchingFields.size == 1 }.any { c -> c.matchingFields.contains(index) } }
        }
    }

    val answer = constraints.filter { it.name.contains("departure") }.fold(1L) {
        total, it -> total * myTicket[it.matchingFields.first()].toLong()
    }

    println("Part 2: product of all 'departure' fields is $answer")
}

enum class ParsePhase { CONSTRAINTS, MY_TICKET, OTHER_TICKETS }

class Constraint(val name: String, private val ranges: List<IntRange>) {
    val matchingFields = (0..19).toMutableList()
    fun matches(num: Int) = ranges.any { it.contains(num) }
}
