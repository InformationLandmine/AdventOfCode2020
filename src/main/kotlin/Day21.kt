import java.io.File

fun main(args: Array<String>) {
    println("Advent of Code day 21")

    // Define some test data.
    val testData = listOf(
        "mxmxvkd kfcds sqjhc nhms (contains dairy, fish)",
        "trh fvjkl sbzzf mxmxvkd (contains dairy)",
        "sqjhc fvjkl (contains soy)",
        "sqjhc mxmxvkd sbzzf (contains fish)"
    )

    // Setup - Read the product ingredient lists
    val products = ArrayList<Pair<List<String>, List<String>>>()
    val inputData = ArrayList<String>()
    File("day21input").forEachLine {
        products.add(Pair(it.split('(')[0].split(' ', ',').filterNot { it == "" }.map { it.trim() },
            it.split("(contains")[1].split(",", " ", ")").filterNot { it == "" }.map {it.trim() }))
    }
    val ingredients = products.map { it.first }.flatten().toSet()
    val allergens = products.map { it.second }.flatten().toSet()

    // Create a map of all possible allergens
    val allergenMap = HashMap<String, ArrayList<String>>()
    ingredients.forEach { ingredient ->
        allergens.forEach { allergen ->
            if (products.filter { product -> product.second.contains(allergen) }.all { it.first.contains(ingredient) }) {
                if (!allergenMap.containsKey(ingredient))
                    allergenMap[ingredient] = ArrayList()
                allergenMap[ingredient]!!.add(allergen)
            }
        }
    }

    // Part 1 - find the number of times a non-allergen ingredient appears in any of the products,
    val safeIngredients = ingredients.filter { !allergenMap.containsKey(it) }
    val answer1 = safeIngredients.fold(0) { sum, ingredient -> sum + products.count { it.first.contains(ingredient)}}
    println("Part 1: The number of times non-allergen ingredients appear is: $answer1")

    // Part 2 - Reduce the allergen list until each ingredient can only be one allergen.
    while (allergenMap.any { it.value.size > 1 }) {
        val filtered = allergenMap.filter { it.value.size == 1 }
        filtered.values.forEach { solved ->
            allergenMap.keys.forEach {
                if (allergenMap[it]!!.size > 1)
                    allergenMap[it]!!.removeAll(solved)
            }
        }
    }

    // Get the ingredients in order based on the alphabetical order of the allergens.
    var answer2 = allergenMap.toList().sortedBy { (_, value) -> value.first()}.fold("") { answer, it -> answer + it.first + "," }.dropLast(1)
    println("Part 2: The ingredients in order by allergen are: $answer2")
}
