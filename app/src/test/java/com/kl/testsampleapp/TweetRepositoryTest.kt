package com.kl.testsampleapp

import com.kl.testsampleapp.data.repository.TweetRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TweetRepositoryTest {

    @Test
    fun containsHelloFromWorld() {
        val repository = TweetRepository()
        val list = repository.recents().test().await().values()[0]
        assertThat(list)
            .extracting("tweet", String::class.java)
            .containsExactly("hello", "from", "world!")
    }
}