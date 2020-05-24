package com.kl.testsampleapp


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.math.BigInteger

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

interface FastTest

interface SlowTest

class ExampleUnitTest {

    // JUnit4で@Categoryを使ってテストケース実行を制限する例
//    @Test
//    @Category(FastTest::class)
//    fun addition_isCorrect() {
//        assertEquals(4,2 + 2)
//    }
//
//    @Test
//    @Category(SlowTest::class)
//    fun sum_isCorrect() {
//        var result = BigInteger.ZERO
//        for (i in 1..100000000L) {
//            result = result.add(BigInteger.valueOf(i))
//        }
//        assertEquals(BigInteger("500000000500000000"), result)
//    }

    /**
     * JUnit5でテストケース実行を制限する例
     */
    @Test
    @Tag("Fast")
    fun addition_isCorerct() {
        assertEquals(4, 2 + 2)
    }

    @Test
    @Tag("Slow")
    fun sum_isCorrect() {
        var result = BigInteger.ZERO
        for (i in 1..100000000L) {
            result = result.add(BigInteger.valueOf(i))
        }
        assertEquals(BigInteger("500000000500000000"), result)
    }
}
