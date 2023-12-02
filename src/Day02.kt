import java.util.StringTokenizer

fun main() {
    fun part1(input: List<String>): Int {
        return input.map {
            evaluateGameV1(parseLine(it))
        }.sumUp()
    }

    fun part2(input: List<String>): Int {
        return input.map {
            evaluateGameV2(parseLine(it))
        }.sumUp()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("02")
    part1(input).println()
    part2(input).println()
}

private fun parseLine(line: String): Game {
    val tok= StringTokenizer(line, ":")
    check(tok.countTokens() == 2)
    val game = Game(tok.nextToken().filter { it.isDigit() }.toInt(), mutableListOf())
    tok.nextToken().split(";").forEach { hand ->
        val handful = Handful(mutableMapOf(Color.RED to 0, Color.GREEN to 0, Color.BLUE to 0))
        val cubes = hand.split(",")
        cubes.forEach {
            val color = Color.valueOf(it.filter { it.isLetter() }.uppercase())
            handful.cubes[color] = it.filter { it.isDigit() }.toInt()
        }
        game.sets.add(handful)
    }
    return game
}

private fun evaluateGameV1(game: Game): Int {
    if(game.sets.maxBy { it.cubes[Color.RED]!! }.cubes[Color.RED]!! > 12 ){
        return 0
    }
    if(game.sets.maxBy { it.cubes[Color.GREEN]!! }.cubes[Color.GREEN]!! > 13 ){
        return 0
    }
    if(game.sets.maxBy { it.cubes[Color.BLUE]!! }.cubes[Color.BLUE]!! > 14 ){
        return 0
    }
    return game.id
}

private fun evaluateGameV2(game: Game): Int {
    return game.sets.maxBy { it.cubes[Color.RED]!! }.cubes[Color.RED]!! *
            game.sets.maxBy { it.cubes[Color.GREEN]!! }.cubes[Color.GREEN]!! *
            game.sets.maxBy { it.cubes[Color.BLUE]!! }.cubes[Color.BLUE]!!
}

data class Game (
        val id: Int,
        val sets: MutableList<Handful>
)
data class Handful(
        val cubes: MutableMap<Color, Int>
)

enum class Color {
    RED,
    GREEN,
    BLUE
}