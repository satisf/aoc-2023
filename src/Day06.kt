import java.util.StringTokenizer

fun main() {
    fun part1(input: List<String>): Int {
        val times = input[0].split(' ').filter { it.isNotBlank() }.filter { it != "Time:"}.map { it.toInt() }
        val distances = input[1].split(' ').filter { it.isNotBlank() }.filter { it != "Distance:"}.map { it.toInt() }

        return times.mapIndexed() { index, maxTime ->
            (1..maxTime).map { it * (maxTime-it) }.filter { it > distances[index] }.size
        }.reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Int {
        val time = input[0].filter { it.isDigit() }.toLong()
        val distance = input[1].filter { it.isDigit() }.toLong()

        return (1..time).map {it * (time-it)}.filter { it > distance }.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("06")
    part1(input).println()
    part2(input).println()
}
