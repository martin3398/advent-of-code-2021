import kotlin.math.absoluteValue

fun Triple<Int, Int, Int>.get(i: Int): Int = when (i) {
    0 -> this.first
    1 -> this.second
    2 -> this.third
    else -> throw IllegalArgumentException(i.toString())
}

fun Triple<Int, Int, Int>.rotate(d: Int): Triple<Int, Int, Int> {
    val c0 = d % 3
    val c0s = 1 - ((d / 3) % 2) * 2
    val c1 = (c0 + 1 + (d / 6) % 2) % 3
    val c1s = 1 - (d / 12) * 2
    val c2 = 3 - c0 - c1
    val c2s = c0s * c1s * (if (c1 == (c0 + 1) % 3) 1 else -1)
    return Triple(get(c0) * c0s, get(c1) * c1s, get(c2) * c2s)
}

fun Triple<Int, Int, Int>.shift(offset: Triple<Int, Int, Int>) =
    Triple(first - offset.first, second - offset.second, third - offset.third)

fun minus(e1: Triple<Int, Int, Int>, e2: Triple<Int, Int, Int>): Triple<Int, Int, Int> =
    Triple(e1.first - e2.first, e1.second - e2.second, e1.third - e2.third)

fun Triple<Int, Int, Int>.abs() = Triple(first.absoluteValue, second.absoluteValue, third.absoluteValue)

fun Triple<Int, Int, Int>.sum() = first + second + third




fun intersect(
    e1: List<Triple<Int, Int, Int>>,
    e2: List<Triple<Int, Int, Int>>
): Pair<List<Triple<Int, Int, Int>>, Triple<Int, Int, Int>>? {
    for (d in 0 until 24) {
        val rotated = e2.map { it.rotate(d) }
        for (x1 in e1) {
            for (x2 in rotated) {
                val dist = minus(x2, x1)
                val shifted = rotated.map { it.shift(dist) }
                if (e1.toSet().intersect(shifted.toSet()).size >= 12) {
                    return Pair(shifted, Triple(0, 0, 0).rotate(d).shift(dist))
                }
            }
        }
    }
    return null
}

fun main() {
    fun preprocess(input: List<String>): MutableList<List<Triple<Int, Int, Int>>> {
        val res = mutableListOf<List<Triple<Int, Int, Int>>>()
        var current = mutableListOf<Triple<Int, Int, Int>>()
        var i = 0
        while (input.size > i) {
            if (input[i].startsWith("---")) {
                if (current.isNotEmpty()) {
                    res.add(current)
                    current = mutableListOf()
                }
            } else if (input[i] != "") {
                val split = input[i].split(",").map { it.toInt() }
                current.add(Triple(split[0], split[1], split[2]))
            }
            i++
        }
        if (current.isNotEmpty()) {
            res.add(current)
        }
        return res
    }

    fun calc(input: MutableList<List<Triple<Int, Int, Int>>>): Pair<Int, Int> {
        val foundPos = HashSet<Triple<Int, Int, Int>>()
        val foundSets = mutableSetOf(0)
        val remaining = (1 until input.size).toMutableSet()
        val scannerPos = ArrayList(Array<Triple<Int, Int, Int>?>(input.size) { null }.toList())
        foundPos.addAll(input[0])
        scannerPos[0] = Triple(0, 0, 0)

        while (remaining.isNotEmpty()) {
            outer@ for (e1 in remaining) {
                for (e2 in foundSets) {
                    val newPos = intersect(input[e2], input[e1])
                    if (newPos != null) {
                        input[e1] = newPos.first
                        foundPos.addAll(newPos.first)
                        remaining.remove(e1)
                        foundSets.add(e1)
                        scannerPos[e1] = newPos.second
                        println("found: $foundSets")
                        println("remaining: $remaining")
                        println()
                        break@outer
                    }
                }
            }
        }

        val scannerPosNotNull = scannerPos.filterNotNull()
        var max = -1
        for (e1 in scannerPosNotNull) {
            for (e2 in scannerPosNotNull) {
                val manhattanDist = minus(e1, e2).abs().sum()
                if (manhattanDist > max) {
                    max = manhattanDist
                }
            }
        }

        return Pair(foundPos.size, max)
    }

    val testInput = preprocess(readInput(19, true))
    val input = preprocess(readInput(19))

    val testResult = calc(testInput)
    check(testResult.first == 79)
    check(testResult.second == 3621)

    val result = calc(input)
    println(result.first)
    println(result.second)
}
