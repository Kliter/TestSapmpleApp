package com.kl.testsampleapp

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Parent {

    var value = 2

    @BeforeEach
    fun beforeEach() {
        value = 2
    }

    @Test
    fun addition() {
        value += 2
        assertEquals(4, value)
    }

    /**
     * @Nestedを使うことで親クラスの@BeforeEachが実行された後,子クラスの@Testが実行される。
     */
    @Nested
    inner class Child {
        @Test
        fun subtraction() {
            value = 4 - value
            assertEquals(2, value)
        }
    }
}