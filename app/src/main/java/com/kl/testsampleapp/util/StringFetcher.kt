package com.kl.testsampleapp.util

open class StringFetcher {
    fun fetch(): String {
        Thread.sleep(1000L)
        return "foo"
    }
}