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
        val tickets = input.map { line ->
            val numbers = line.split(':')[1]
            val win = numbers.split('|')[0]
                .split(' ')
                .filter { it.isNotBlank() }
                .map { it.toInt() }
            val have = numbers.split('|')[1]
                .split(' ')
                .filter { it.isNotBlank() }
                .map { it.toInt() }
            Ticket(win, have)
        }
        for((idx, ticket) in tickets.withIndex()){
            val wins = ticket.win.intersect(ticket.have.toSet()).size
            for (i in idx+1..idx+wins){
                tickets[i].copies+= ticket.copies
            }
        }
        return tickets.map { it.copies }.sumUp()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("04")
    part1(input).println()
    part2(input).println()
}

data class Ticket(
    val win: List<Int>,
    val have: List<Int>,
    var copies: Int = 1
)