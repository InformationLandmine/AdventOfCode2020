import java.io.File
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    println("Advent of Code day 7")

    // This pattern works but I don't know how to parse out multiple instances of the contents.
    val BAG_INPUT_REGEX = """(\S+ \S+) bags contain(( no other bags.)|( (\d) (\S+ \S+) (bag|bags)[,.])+)""".toRegex()

    // Setup - Parse the data and create a map of bags
    val bags = HashMap<String, Bag>()
    var timeMs = measureTimeMillis {
        File("day7input").forEachLine { line ->
            // Read bag color and contents.
            val bagColor = line.substringBefore(" bags")
            val contents = line.substringAfter("contain ").split('.', ',')

            // Look up bag or create new one.
            val bag = bags[bagColor]?: Bag(bagColor)
            contents.filter { it != "no other bags" && it != "" }.forEach {
                val content = it.trim().split(' ')
                val otherBagCount = content[0].toInt()
                val otherBagColor = content[1] + " " + content[2]
                val otherBag = bags[otherBagColor]?:Bag(otherBagColor)
                bag.contents.add(Pair(otherBag, otherBagCount))
                otherBag.containedBy.add(bag)
                bags[otherBagColor] = otherBag
            }
            bags[bagColor] = bag
        }
    }
    println("Took $timeMs ms to parse input")

    // I have a shiny gold bag.
    val MY_BAG_COLOR = "shiny gold"
    val myBag = bags[MY_BAG_COLOR]?:Bag("unknown color")

    // Part 1 - How many bags can eventually contain a shiny gold bag
    // Method 1 - Iterate over all bags and filter out those which do not contain it.
    timeMs = measureTimeMillis {
        println("${bags.filter { it.value.contains(myBag) }.size} bags eventually contain a $MY_BAG_COLOR bag")
    }
    println("Part 1 method 1 solution took $timeMs ms")
    // Method 2 - start with the given bag and work backwards based on which bags contain it.
    timeMs = measureTimeMillis {
        println("${myBag.whichContain().size} bags eventually contain a $MY_BAG_COLOR bag")
    }
    println("Part 1 method 2 solution took $timeMs ms")

    // Part 2 - How many bags are in the bag
    timeMs = measureTimeMillis {
        println("A $MY_BAG_COLOR bag contains ${myBag.count()} other bags")
    }
    println("Part 2 solution took $timeMs ms")
}

class Bag(val color: String) {
    // A list of bags contained by this bag and the count of each.
    val contents: ArrayList<Pair<Bag, Int>> = ArrayList()
    // A list of bags that this bag is contained by.
    val containedBy: ArrayList<Bag> = ArrayList()

    // Retrieve a set of bags which will contain this bag at any level of nesting.
    fun whichContain(): HashSet<Bag> = containedBy.toHashSet().apply { containedBy.forEach { this.addAll(it.whichContain()) } }

    // Determine if a given bag is contained by this bag or any nested inside it.
    fun contains(bag: Bag): Boolean = contents.any { it.first == bag || it.first.contains(bag) }

    // Count how many bags are inside this bag, including nesting.
    fun count(): Int = contents.fold(0) { result, it -> result + it.second * (it.first.count() + 1) }
}