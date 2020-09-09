package com.network.clever

import org.junit.Assert
import org.junit.Test

class CodingTest01 {
    @Test
    fun test() {
        val s1 = solution("aabbaccc")
        println(s1)
        Assert.assertEquals(s1, 7)

        val s2 = solution("ababcdcdababcdcd")
        println(s2)
        Assert.assertEquals(s2, 9)

        val s3 = solution("abcabcdede")
        println(s3)
        Assert.assertEquals(s3, 8)

        val s4 = solution("abcabcabcabcdededededede")
        println(s4)
        Assert.assertEquals(s4, 14)

        val s5 = solution("xababcdcdababcdcd")
        println(s5)
        Assert.assertEquals(s5, 17)
    }

    fun solution(s: String): Int {
        var answer = getOptimizedLength(s)
        return answer
    }

    fun getOptimizedLength(s: String): Int {
        var shortest = s.length

        for (length in 1..s.length) {
            val subStrList = getSubStrList(s, length)
            val subStrPairList = getSubStrPairList(subStrList)
            val resultStr = makeResultString(subStrPairList)
            val subStrLength = resultStr.length

            if (subStrLength < shortest)
                shortest = subStrLength
        }

        return shortest
    }

    fun getSubStrList(s: String, length: Int): List<String> {
        val subStrList = arrayListOf<String>()

        for (i in 0 until (s.length / length)) {
            val subStr = s.substring(i * length, (i + 1) * length)
            subStrList.add(subStr)
        }

        val remain = s.drop(s.length - (s.length % length))

        if (remain.isNotEmpty())
            subStrList.add(remain)

        return subStrList
    }

    fun getSubStrPairList(subStrList: List<String>): List<SubStrPair> {
        val subStrPairList = arrayListOf<SubStrPair>()

        subStrList.forEach {
            val lastSubStrPair: SubStrPair? =
                if (subStrPairList.isNotEmpty())
                    subStrPairList.last()
                else
                    null

            if (lastSubStrPair == null || lastSubStrPair.str != it)
                subStrPairList.add(SubStrPair(it))
            else
                subStrPairList.last().count++
        }

        return subStrPairList
    }

    fun makeResultString(subStrPairList: List<SubStrPair>): String {
        var resultStr = ""
        for (pair in subStrPairList) {
            resultStr += pair.getFormattedStr()
        }

        return resultStr
    }

    data class SubStrPair(
        val str: String,
        var count: Int = 1
    ) {
        fun getFormattedStr() = "${if (count < 2) "" else "$count"}$str"
    }
}

