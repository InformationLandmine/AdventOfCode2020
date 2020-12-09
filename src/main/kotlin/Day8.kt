import java.io.File

fun main(args: Array<String>) {
    println("Advent of Code day 8")

    // Setup - Parse the program data and create a list of instructions and parameters
    val inst = ArrayList<Triple<String, Int, Boolean>>()
    File("day8input").forEachLine { line ->
        val tokens = line.split(' ')
        inst.add(Triple(tokens[0], tokens[1].toInt(), false))
    }

    // Part 1 - run the program and find the infinite loop
    // Copy the original program
    val prog = ArrayList<Triple<String, Int, Boolean>>()
    prog.addAll(inst)
    runProgram(prog)

    // Part 2 - find the incorrect instruction
    var modIndex = 0
    do {
        // Copy the original program
        val prog = ArrayList<Triple<String, Int, Boolean>>()
        prog.addAll(inst)

        // Swap the current instruction if it is nop or jmp
        when (prog[modIndex].first) {
            "jmp" -> prog[modIndex] = prog[modIndex].copy(first = "nop")
            "nop" -> prog[modIndex] = prog[modIndex].copy(first = "jmp")
        }
        modIndex++
    } while (!runProgram(prog))
    println ("Incorrect instruction was #$modIndex: ${inst[modIndex]}")
}

fun runProgram(inst: ArrayList<Triple<String, Int, Boolean>>): Boolean {
    var result = false
    var acc = 0     // Accumulator
    var ip = 0      // Instruction pointer

    // Run until we reach exactly the end of the program or until an instruction is reached for the second time
    while (ip != inst.size && !inst[ip].third) {
        inst[ip] = inst[ip].copy(third = true)   // set the instruction as reached
        when (inst[ip].first) {
            "nop" -> ip++
            "acc" -> acc += inst[ip++].second
            "jmp" -> ip += inst[ip].second
        }
    }
    if (ip == inst.size) {
        println("Program completed successfully. acc = $acc")
        result = true
    } else {
        println("Infinite loop encountered. Acc = $acc; ip = $ip")
    }

    return result
}