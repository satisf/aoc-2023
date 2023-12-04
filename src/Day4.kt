import java.util.StringTokenizer

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { line ->
            val numbers = line.split(':')[1]
            val win = numbers.split('|')[0]
                .split(' ')
                .filter { it.isNotBlank() }
                .map { it.toInt() }
            val have = numbers.split('|')[1]
                .split(' ')
                .filter { it.isNotBlank() }
                .map { it.toInt() }
            win.intersect(have.toSet()).foldIndexed(0) {idx,acc, _ -> if (idx == 0) 1 else acc * 2}
        }.sumUp()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("04_test")
    check(part1(testInput) == 13)
    //check(part2(testInput) == 0)

    val input = readInput("04")
    part1(input).println()
    part2(input).println()
}
