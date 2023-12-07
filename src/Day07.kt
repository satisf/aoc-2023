import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        return input.map {line ->
            val splitLine = line.split(' ')
            val hand = splitLine[0].map { findCardOrThrow(it) }
            val bid = splitLine[1].toInt()
            Hand(hand, bid, findHandType(splitLine[0]))
        }
            .sortedWith { a, b -> a.calculateValue() - b.calculateValue() }
            .mapIndexed { index, hand -> hand.bid * (index+1) }
            .sumUp()
    }

    fun part2(input: List<String>): Int {

        return input.map {line ->
            val splitLine = line.split(' ')
            val hand = splitLine[0].map { findCardOrThrow(it) }
            val bid = splitLine[1].toInt()
            Hand(hand, bid, findHandType2(splitLine[0]))
        }
            .sortedWith { a, b -> a.calculateValue2() - b.calculateValue2() }
            .mapIndexed { index, hand -> hand.bid * (index+1) }
            .sumUp()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("07")
    part1(input).println()
    part2(input).println()
}


private fun findHandType(hand: String): Type{
    val sortedHand = Card.entries
        .map { it.associatedChar }
        .map { char -> hand.count {char == it} }
        .sortedDescending()
    return when(sortedHand[0]){
        5 -> Type.FIVE_OF_A_KIND
        4 -> Type.FOUR_OF_A_KIND
        3 -> when(sortedHand[1]){
            2 -> Type.FULL_HOUSE
            else -> Type.THREE_OF_A_KIND
        }
        2 -> when(sortedHand[1]){
            2 -> Type.TWO_PAIR
            else -> Type.PAIR
        }
        else -> Type.HIGH_CARD
    }
}

private fun findHandType2(hand: String): Type{
    val sortedHand = Card.entries
        .map { it.associatedChar }
        .map { char -> hand.count {char == it && Card.JACK.associatedChar != it} }
        .sortedDescending().toMutableList()
        sortedHand[0]+=hand.count {it == Card.JACK.associatedChar}
    return when(sortedHand[0]){
        5 -> Type.FIVE_OF_A_KIND
        4 -> Type.FOUR_OF_A_KIND
        3 -> when(sortedHand[1]){
            2 -> Type.FULL_HOUSE
            else -> Type.THREE_OF_A_KIND
        }
        2 -> when(sortedHand[1]){
            2 -> Type.TWO_PAIR
            else -> Type.PAIR
        }
        else -> Type.HIGH_CARD
    }
}
data class Hand(
    val hand: List<Card>,
    val bid: Int,
    val type: Type
)

private fun Hand.calculateValue(): Int {
    val value = hand.map { it.value }.reversed().mapIndexed { index, i -> 14.0.pow(index) * i }.map { it.toInt() }
    return value.sumUp() + type.value
}

private fun Hand.calculateValue2(): Int {
    val value = hand.map { it.value2 }.reversed().mapIndexed { index, i -> 14.0.pow(index) * i }.map { it.toInt() }
    return value.sumUp() + type.value
}

enum class Card(val value: Int, val value2: Int, val associatedChar: Char){
    TWO(2,2, '2'),
    THREE(3, 3,'3'),
    FOUR(4, 4,'4'),
    FIVE(5,5, '5'),
    SIX(6,6, '6'),
    SEVEN(7, 7,'7'),
    EIGHT(8, 8,'8'),
    NINE(9, 9,'9'),
    TEN(10, 10,'T'),
    JACK(11, 1,'J'),
    QUEEN(12, 12,'Q'),
    KING(13, 13,'K'),
    ACE(14, 14,'A')
}

fun findCardOrThrow(char: Char): Card =
    Card.entries.find { it.associatedChar == char }?:throw Exception()

enum class Type(val value: Int){
    FIVE_OF_A_KIND(700000000),
    FOUR_OF_A_KIND(600000000),
    FULL_HOUSE(500000000),
    THREE_OF_A_KIND(400000000),
    TWO_PAIR(300000000),
    PAIR(200000000),
    HIGH_CARD(100000000)
}
