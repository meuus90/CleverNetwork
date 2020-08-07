package com.bluewhale

import org.junit.Assert
import org.junit.Test

class CodingTest02 {
    @Test
    fun test() {
        val p1 = solution("(()())()")
        println(p1)
        Assert.assertEquals(p1, "(()())()")

        val p2 = solution(")(")
        println(p2)
        Assert.assertEquals(p2, "()")

        val p3 = solution("()))((()")
        println(p3)
        Assert.assertEquals(p3, "()(())()")
    }

    fun solution(p: String): String {
        var answer = getFormattedStr(p)
        return answer
    }

    fun getFormattedStr(w: String): String {
        val strSet = getStrSet(w)
        return if (strSet is StrSet.SeparatedSet) {
            if (checkIsComplete(strSet.u))
                strSet.u + getFormattedStr(strSet.v)
            else {
                correctStr(strSet)
            }
        } else {
            ""
        }
    }

    fun getStrSet(w: String): StrSet {
        return if (w.isEmpty())
            StrSet.EmptySet()
        else {
            var openCount = 0
            var closeCount = 0

            val strSet = StrSet.SeparatedSet()
            for (i in w.indices) {
                if (w[i] == '(')
                    openCount++
                else
                    closeCount++

                if (openCount == closeCount) {
                    strSet.u = w.substring(0, i + 1)
                    strSet.v = w.substring(i + 1, w.length)
                    break
                }
            }

            strSet
        }
    }

    fun checkIsComplete(w: String): Boolean {
        return if (w.isEmpty())
            return true
        else {
            val charStack = ArrayList<Char>()

            for (i in w.indices) {
                if (charStack.isEmpty()) {
                    if (w[i] == ')')
                        return false
                    else {
                        charStack.add(w[i])
                    }
                } else {
                    if (charStack.last() != w[i]) {
                        charStack.removeAt(charStack.lastIndex)
                    } else {
                        charStack.add(w[i])
                    }
                }
            }

            charStack.isEmpty()
        }
    }

    fun correctStr(strSet: StrSet.SeparatedSet): String {
        var newStr = "("
        newStr += revert(strSet.u.substring(1, strSet.u.length - 1))
        newStr += ")"
        newStr += getFormattedStr(strSet.v)

        return newStr
    }

    fun revert(w: String): String {
        var newStr = ""
        for (char in w.iterator()) {
            newStr += if (char == '(') ')'
            else '('
        }
        return newStr
    }

    sealed class StrSet {
        class EmptySet : StrSet()
        class SeparatedSet(var u: String = "", var v: String = "") : StrSet()
    }
}

