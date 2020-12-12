import java.io.File
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    println("Advent of Code day 12")

    // Setup - Read the list of directions.
    val DIRECTIONS_REGEX = """(\S)(\d+)""".toRegex()
    val directions = ArrayList<Pair<Char, Int>>()
    File("day12input").forEachLine {
        val parse = DIRECTIONS_REGEX.matchEntire(it)
        val direction = parse?.groups?.get(1)?.value?.get(0)?:'?'
        val count = parse?.groups?.get(2)?.value?.toInt()?:0
        directions.add (Pair(direction, count))
    }

    // Part 1 - follow the directions and count the Manhattan Distance.
    var lat = 0
    var long = 0
    var facing = 0

    directions.forEach {
        when (it.first) {
            'N' -> lat += it.second
            'S' -> lat -= it.second
            'E' -> long += it.second
            'W' -> long -= it.second
            'L' -> {
                facing += it.second
                if (facing >= 360) facing -= 360
            }
            'R' -> {
                facing -= it.second
                if (facing < 0) facing += 360
            }
            'F' -> {
                when (facing) {
                    90 -> lat += it.second
                    270 -> lat -= it.second
                    0 -> long += it.second
                    180 -> long -= it.second
                }
            }
        }
    }

    var distance = lat.absoluteValue + long.absoluteValue
    println("Lat = $lat; Long = $long; Distance = $distance")

    // Part 2 - follow the new interpretation of the directions and count the Manhattan Distance
    lat = 0
    long = 0
    var waypointLat = 1
    var waypointLong = 10

    directions.forEach {
        when (it.first) {
            'N' -> waypointLat += it.second
            'S' -> waypointLat -= it.second
            'E' -> waypointLong += it.second
            'W' -> waypointLong -= it.second
            'L' -> {
                var tempLat = waypointLat
                var tempLong = waypointLong
                when (it.second) {
                    90 -> {tempLat = waypointLong; tempLong = -waypointLat}
                    180 -> {tempLat = -waypointLat; tempLong = -waypointLong}
                    270 -> {tempLat = -waypointLong; tempLong = waypointLat}
                    else -> "Rotate Left Error"
                }
                waypointLat = tempLat
                waypointLong = tempLong
            }
            'R' -> {
                var tempLat = waypointLat
                var tempLong = waypointLong
                when (it.second) {
                    90 -> {tempLat = -waypointLong; tempLong = waypointLat}
                    180 -> {tempLat = -waypointLat; tempLong = -waypointLong}
                    270 -> {tempLat = waypointLong; tempLong = -waypointLat}
                    else -> "Rotate Right Error"
                }
                waypointLat = tempLat
                waypointLong = tempLong
            }
            'F' -> {
                lat += waypointLat * it.second
                long += waypointLong * it.second
            }
            else -> println("Moving error")
        }
    }

    distance = lat.absoluteValue + long.absoluteValue
    println("Lat = $lat; Long = $long; Distance = $distance")

}
