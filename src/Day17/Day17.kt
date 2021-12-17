fun main() {

    fun preprocess(input: List<String>): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        val split = input[0].split(" ")
        val x = split[2].split("=")[1].split(",")[0].split("..")
        val y = split[3].split("=")[1].split("..")
        return Pair(Pair(x[0].toInt(), x[1].toInt()), Pair(y[0].toInt(), y[1].toInt()))
    }

    fun calc(input: Pair<Pair<Int, Int>, Pair<Int, Int>>): Pair<Int, Int> {
        val xRange = input.first.first..input.first.second
        val yRange = input.second.first..input.second.second
        var count = 0
        var bestY = 0
        // range -500..500 should be sufficient
        for (vxSearch in -500..500) {
            for (vySearch in -500..500) {
                var x = 0
                var y = 0
                var vx = vxSearch
                var vy = vySearch
                var maxY = 0
                while (y >= input.second.first) {
                    x += vx
                    y += vy
                    if (vx != 0) {
                        vx += if (vx > 0) -1 else 1
                    }
                    if (y > maxY) {
                        maxY = y
                    }
                    vy--
                    if (x in xRange && y in yRange) {
                        count++
                        if (maxY > bestY) {
                            bestY = maxY
                        }
                        break
                    }
                }
            }
        }
        return Pair(bestY, count)
    }

    fun part1(input: Pair<Pair<Int, Int>, Pair<Int, Int>>): Int {
        return calc(input).first
    }

    fun part2(input: Pair<Pair<Int, Int>, Pair<Int, Int>>): Int {
        return calc(input).second
    }

    val testInput = preprocess(readInput(17, true))
    val input = preprocess(readInput(17))

    check(part1(testInput) == 45)
    println(part1(input))

    check(part2(testInput) == 112)
    println(part2(input))
}
