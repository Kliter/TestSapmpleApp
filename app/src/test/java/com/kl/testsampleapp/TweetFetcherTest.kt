package com.kl.testsampleapp

import com.kl.testsampleapp.data.repository.Tweet
import com.kl.testsampleapp.data.repository.TweetRepository
import com.kl.testsampleapp.util.TweetFetcher
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TweetFetcherTest {

    lateinit var repository: TweetRepository
    lateinit var tweetFetcher: TweetFetcher

    @BeforeEach
    fun setup() {
        repository = mock(name = "StubTweetRepository")
        val tweets = listOf(
            Tweet("hello", 1),
            Tweet("from", 2),
            Tweet("world", 3)
        )
        whenever(repository.recents()).thenReturn(Single.just(tweets)) // 実際にはDBや通信でデータを取得するところをスタブで誤魔化す。

        /**
         * Schedulers.trampoline()をつかって、
         * 非同期処理をFIFOで処理する。
         */
        tweetFetcher = TweetFetcher(
            repository,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }

    @Test
    fun tweetFetcherTest() {
        tweetFetcher.recents(
            onSuccess = {
                assertThat(it)
                    .extracting("tweet", String::class.java)
                    .containsExactly("hello", "from", "world!")
            },
            onError = {}
        )
    }
}