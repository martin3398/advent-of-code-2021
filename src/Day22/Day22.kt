import java.lang.Long.max
import java.lang.Long.min

data class Cube(val x: Pair<Long, Long>, val y: Pair<Long, Long>, val z: Pair<Long, Long>) {
    fun inSmallerCube() =
        x.first >= -50 && x.second <= 50 && y.first >= -50 && y.second <= 50 && z.first >= -50 && z.second <= 50
}

fun Cube.intersect(other: Cube): Cube? {
    val minX = min(x.second, other.x.second)
    val maxX = max(x.first, other.x.first)
    val minY = min(y.second, other.y.second)
    val maxY = max(y.first, other.y.first)
    val minZ = min(z.second, other.z.second)
    val maxZ = max(z.first, other.z.first)
    if (maxX > minX || maxY > minY || maxZ > minZ) {
        return null
    }
    return Cube(Pair(maxX, minX), Pair(maxY, minY), Pair(maxZ, minZ))
}

fun Cube.volume() = (x.second - x.first + 1) * (y.second - y.first + 1) * (z.second - z.first + 1)

fun main() {
    fun preprocess(input: List<String>): List<Pair<Boolean, Cube>> {
        fun parseCube(s: String): Pair<Boolean, Cube> {
            val split = s.split(" ")
            val onOff = split[0] == "on"
            val pos = split[1].split(",")
                .map { it.split("=")[1].split("..").map { x -> x.toLong() }.zipWithNext()[0] }
            return Pair(onOff, Cube(pos[0], pos[1], pos[2]))
        }
        return input.map { parseCube(it) }
    }

    fun calc(input: List<Pair<Boolean, Cube>>): Long {
        var parts: Map<Cube, Long> = HashMap()
        for ((onOff, cube) in input) {
            val add = HashMap<Cube, Long>()
            if (onOff) {
                add[cube] = 1
            }
            for ((part, count) in parts) {
                val intersection = cube.intersect(part)
                if (intersection != null) {
                    add[intersection] = add.getOrDefault(intersection, 0) - count
                }
            }

            parts = (parts.asSequence() + add.asSequence())
                .distinct()
                .groupBy({ it.key }, { it.value })
                .mapValues { it.value.sum() }
        }
        return parts.map { it.key.volume() * it.value }.sum()
    }

    fun part1(input: List<Pair<Boolean, Cube>>): Long {
        return calc(input.filter { it.second.inSmallerCube() })
    }

    fun part2(input: List<Pair<Boolean, Cube>>): Long {
        return calc(input)
    }

    val testInput = preprocess(readInput(22, true))
    val input = preprocess(readInput(22))

    check(part1(testInput) == 474140L)
    println(part1(input))

    check(part2(testInput) == 2758514936282235L)
    println(part2(input))
}
