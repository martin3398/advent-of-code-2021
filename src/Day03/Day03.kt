import kotlin.math.pow

fun main() {
    fun bitListToInt(input: List<Int>) =
        input.foldIndexed(0) { index, sum, elem -> sum + elem * (2.0).pow(input.size - index - 1).toInt() }

    fun part1(input: List<String>): Int {
        val zeros = MutableList(input[0].length) { 0 }

        for (e in input) {
            for ((i, c) in e.withIndex()) {
                if (c == '0') {
                    zeros[i]++
                }
            }
        }

        val gamma = bitListToInt(zeros.map { x -> if (x <= input.size / 2) 1 else 0 })
        val eps = bitListToInt(zeros.map { x -> if (x <= input.size / 2) 0 else 1 })

        return gamma * eps
    }

    fun filterBinary(input: List<String>, needle: Char): Int {
        var inputWc = input
        var res = 0
        val inputLen = inputWc[0].length
        for (i in 0 until inputLen) {
            val zeros = inputWc.count { it[i] == '0' } > inputWc.size / 2
            inputWc = inputWc.filter { (zeros && it[i] == needle) || (!zeros && it[i] != needle) }
            if (inputWc.size == 1) {
                res = bitListToInt(inputWc[0].map { it.toString().toInt() })
            }
        }
        return res
    }

    fun part2(input: List<String>): Int {
        val oxygen = filterBinary(input, '0')
        val co2 = filterBinary(input, '1')
        return oxygen * co2
    }

    val testInput = readInput(3, true)
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput(3)
    println(part1(input))
    println(part2(input))
}
