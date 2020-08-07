package com.bluewhale

import org.junit.Assert
import org.junit.Test

class CodingTest03 {
    @Test
    fun test() {
        val key0 = solution(
            arrayOf(
                intArrayOf(1, 1, 1),
                intArrayOf(1, 1, 1),
                intArrayOf(1, 1, 1)
            ),
            arrayOf(
                intArrayOf(1, 1, 1, 1),
                intArrayOf(1, 1, 0, 1),
                intArrayOf(1, 1, 1, 1),
                intArrayOf(1, 1, 1, 1)
            )
        )
        println(key0)
        Assert.assertFalse(key0)

        val key1 = solution(
            arrayOf(
                intArrayOf(1, 1, 1),
                intArrayOf(1, 1, 1),
                intArrayOf(1, 1, 1)
            ),
            arrayOf(
                intArrayOf(1, 1, 1, 1),
                intArrayOf(1, 1, 1, 1),
                intArrayOf(1, 1, 1, 1),
                intArrayOf(0, 1, 1, 1)
            )
        )
        println(key1)
        Assert.assertTrue(key1)

        val key2 = solution(
            arrayOf(
                intArrayOf(1, 1, 1),
                intArrayOf(1, 1, 1),
                intArrayOf(1, 1, 1)
            ),
            arrayOf(
                intArrayOf(1, 1, 1, 0),
                intArrayOf(1, 1, 1, 1),
                intArrayOf(1, 1, 1, 1),
                intArrayOf(1, 1, 1, 1)
            )
        )
        println(key2)
        Assert.assertTrue(key2)

        val key3 = solution(
            arrayOf(
                intArrayOf(1, 1, 1),
                intArrayOf(1, 1, 1),
                intArrayOf(1, 1, 1)
            ),
            arrayOf(
                intArrayOf(1, 1, 1, 0),
                intArrayOf(1, 1, 1, 1),
                intArrayOf(1, 1, 1, 1),
                intArrayOf(1, 1, 1, 1)
            )
        )
        println(key3)
        Assert.assertTrue(key3)

        val key4 = solution(
            arrayOf(
                intArrayOf(1, 1, 1),
                intArrayOf(1, 1, 1),
                intArrayOf(1, 1, 1)
            ),
            arrayOf(
                intArrayOf(1, 1, 1, 1),
                intArrayOf(1, 0, 0, 0),
                intArrayOf(1, 0, 0, 0),
                intArrayOf(1, 0, 0, 0)
            )
        )
        println(key4)
        Assert.assertTrue(key4)
    }

    fun solution(key: Array<IntArray>, lock: Array<IntArray>): Boolean {

        var answer = canSolve(key, lock)
        return answer
    }

    fun canSolve(key: Array<IntArray>, lock: Array<IntArray>): Boolean {
        var twistedKey = key

        for (v in 0..3) {
            twistedKey = getRotatedArray(twistedKey)
            if (getMaxShiftCount(twistedKey, lock))
                return true
        }
        return false
    }

    fun getRotatedArray(key: Array<IntArray>): Array<IntArray> {
        val twistedKey = Array(key.size) { IntArray(key.size) { 0 } }
        for (v in key.indices) {
            for (h in key.indices) {
                twistedKey[h][key.lastIndex - v] = key[v][h]
            }
        }
        return twistedKey
    }

    fun getMaxShiftCount(key: Array<IntArray>, lock: Array<IntArray>): Boolean {
        val min = -(key.lastIndex)
        val max = key.lastIndex + (lock.size - key.size)
        for (v in min..max) {
            for (h in min..max) {
                if (checkPair(key, lock, v, h))
                    return true
            }
        }

        return false
    }

    fun checkPair(
        key: Array<IntArray>,
        lock: Array<IntArray>,
        horizontalShift: Int,
        verticalShift: Int
    ): Boolean {
        for (v in lock.indices) {
            for (h in lock.indices) {
                val keyVerticalPos = v - verticalShift
                val keyHorizontalPos = h - horizontalShift

                val item =
                    if (keyVerticalPos < 0 || keyHorizontalPos < 0 || keyVerticalPos > key.lastIndex || keyHorizontalPos > key.lastIndex)
                        0
                    else
                        key[keyVerticalPos][keyHorizontalPos]

                if (lock[v][h] + item != 1)
                    return false
            }
        }

        return true
    }
}

