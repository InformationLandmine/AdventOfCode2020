import java.io.File
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    println("Advent of Code day 4")

    // Setup - build a list of maps
    val passports = ArrayList<HashMap<String, String>>()
    var passport = HashMap<String, String>()
    File("day4input").forEachLine {
        if (it == "") {  // empty line - new passport
            passports.add(passport)
            passport = HashMap<String, String>()
        } else {
            it.split(' ').forEach {
                val kvPair = it.split(':')
                passport.put(kvPair[0], kvPair[1])
            }
        }
    }
    passports.add(passport)

    // Constraints
    val requiredKeys = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
    val byrRegex = """19[2-9][0-9]|200[0-2]""".toRegex()
    val iyrRegex = """201[0-9]|2020""".toRegex()
    val eyrRegex = """202[0-9]|2030""".toRegex()
    val hgtRegex = """((1[5-8][0-9]|19[0-3])cm)|((59|6[0-9]|7[0-6])in)""".toRegex()
    val hclRegex = """#[0-9a-f]{6}""".toRegex()
    val eclRegex = """amb|blu|brn|gry|grn|hzl|oth""".toRegex()
    val pidRegex = """\d{9}""".toRegex()

    // Part 1 - Count valid passports
    var validCount = passports.filter { it.keys.containsAll(requiredKeys) }.size
    println("There are $validCount valid passports out of ${passports.size}")

    // Part 2 - Count valid passports with stricter data validation
    validCount = passports.filter {
        it.keys.containsAll(requiredKeys) &&
        byrRegex.matches(it["byr"]?:"") &&
        iyrRegex.matches(it["iyr"]?:"") &&
        eyrRegex.matches(it["eyr"]?:"") &&
        hgtRegex.matches(it["hgt"]?:"") &&
        hclRegex.matches(it["hcl"]?:"") &&
        eclRegex.matches(it["ecl"]?:"") &&
        pidRegex.matches(it["pid"]?:"")
    }.size
    println("There are $validCount valid passports out of ${passports.size}")

}
