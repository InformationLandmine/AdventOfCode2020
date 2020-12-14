import java.io.File
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    println("Advent of Code day 14")

    // Setup - Read the program
    val program = ArrayList<String>()
    File("day14input").forEachLine { program.add(it) }

    // Create a memory map
    val memory = HashMap<Long, Long>()
    var mask: String = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"

    // Part 1 - run the program using the mask when writing values to memory
    var timeMs = measureTimeMillis {
        program.forEach { line ->
            val parsed = line.split(' ', '[', ']', '=')
            when (parsed[0]) {
                "mask" -> mask = parsed[parsed.size - 1].reversed()
                "mem" -> memory[parsed[1].toLong()] = parsed[parsed.size - 1].toLong().applyMask(mask)
            }
        }
    }
    println("Part 1 program execution took $timeMs ms")
    var sum = memory.values.reduce { sum, value -> sum + value }
    println("The sum of all values in memory after part 1 is $sum")

    // Part 2 - run the program using the mask as a memory decoder
    timeMs = measureTimeMillis {
        memory.clear()
        program.forEach { line ->
            val parsed = line.split(' ', '[', ']', '=')
            when (parsed[0]) {
                "mask" -> mask = parsed[parsed.size - 1]
                "mem" -> applyMask(mask, parsed[1].toLong()).forEach { memory[it] = parsed[parsed.size - 1].toLong() }
            }
        }
    }
    println("Part 2 program execution took $timeMs ms")
    sum = memory.values.reduce { sum, value -> sum + value }
    println("The sum of all values in memory after part 2 is $sum")
}

// Function to apply a given mask to a Long
fun Long.applyMask(mask: String): Long {
    var result = this
    mask.forEachIndexed { bit, value ->
        when (value) {
            '1' -> result = result or (1L shl bit)
            '0' -> result = result and (Long.MAX_VALUE xor (1L shl bit))
        }
    }
    return result
}

// For part 2, return a list of values after applying a mask with "floating" bits
fun applyMask(mask: String, value: Long): ArrayList<Long> {
    val result = ArrayList<Long>()

    // Convert the address to a string for easier parsing and "unrolling" of floating bits
    val address = value.toString(2).padStart(36, '0').toCharArray()
    mask.forEachIndexed { bit, char ->
        when (char) {
            '1' -> address[bit] = '1'
            'X' -> address[bit] = 'X'
        }
    }
    val addresses = ArrayList<CharArray>()
    addresses.add(address)

    // Expand the masked address.
    // This is really dumb, but it works. For each string with a floating bit, generate two strings with
    // the floating bit replaced by 0 and 1. Keep going until all floating bits are resolved.
    while (addresses.any { it.contains('X') }) {
        addresses.find { it.contains('X') }?.let { address ->
            addresses.remove(address)
            val index = address.indexOfFirst { it == 'X' }
            address[index] = '0'
            addresses.add(address.copyOf())
            address[index] = '1'
            addresses.add(address.copyOf())
        }
    }

    // Convert the addresses back to Longs
    addresses.forEach { result.add(String(it).toLong(2)) }
    return result
}
