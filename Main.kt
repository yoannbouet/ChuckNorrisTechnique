package chucknorris

import kotlin.math.pow
import kotlin.system.exitProcess

fun main() {
    while (true) {
        println("Please input operation (encode/decode/exit):")

        when(val input = readln()) {
            "encode" -> encode()
            "decode" -> decode()
            "exit" -> println("Bye!").also { exitProcess(0) }
            else -> println("There is no '$input' operation")
        }
    }
}

fun encode() {
    println("Input string:")
    val input = readln()
    var str = ""

    for (i in 0..input.lastIndex) str += charToBinary(input[i])

    println("Encoded string:\n" + chuckNorrisTechnique(str))
}

fun decode() {
    println("Input encoded string:")
    val input = readln()
    val list = Regex("0+").findAll(input).map { it.value }.toList()
    for (i in 0..list.lastIndex step 2) {
        if(list[i].length > 2) {
            println("not valid: incorrect identifier.")
            return
        }
    }

    val regex = Regex("(0{1,2}\\s0+\\s?)+")
    if(!regex.matches(input) || Regex("(0{1,2}\\s0+)+").findAll(input).map { it.value.substringAfter(' ').count() }.sum() % 7 != 0) {
        println("not valid: regex doesn't match.")
        return
    }

    println("Decoded string:\n" + binaryToString(reversedChuckNorrisTechnique(input)))
}

fun charToBinary(ch: Char): String {
    var n = ch.code
    var str = ""

    while (n != 0) {
        str += if (n % 2 == 0) 0 else 1
        n /= 2
    }

    return "%07d".format(str.reversed().toInt())
}

fun chuckNorrisTechnique(str: String): String {
    val charSequenceList = Regex("(.)\\1*").findAll(str).map { it.value }.toList()
    var result = ""

    for (i in 0..charSequenceList.lastIndex) {
        if (charSequenceList[i].first() == '0') {
            result += "00 " + charSequenceList[i] + " "
        } else if (charSequenceList[i].first() == '1') {
            result += "0 " + charSequenceList[i].replace('1', '0') + " "
        }
    }

    return result.removeSuffix(" ")
}

fun reversedChuckNorrisTechnique(str: String): String {
    val charSequenceList = Regex("0+").findAll(str).map { it.value }.toList()
    var result = ""

    for (i in 0..charSequenceList.lastIndex step 2) {
        if (charSequenceList[i] == "0") {
            repeat(charSequenceList[i + 1].length) {
                result += "1"
            }
        } else if (charSequenceList[i] == "00") {
            repeat(charSequenceList[i + 1].length) {
                result += "0"
            }
        }
    }

    return result
}

fun binaryToString(str: String): String {
    val list = str.chunked(7).map { it.reversed() }
    var charCode = 0.0
    var result = ""

    for (i in 0..list.lastIndex) {
        for (n in list[i].lastIndex downTo 0) {
            if (list[i][n] == '1') {
                charCode += 2.0.pow(n)
            }
        }
        result += charCode.toInt().toChar()
        charCode = 0.0
    }

    return result
}