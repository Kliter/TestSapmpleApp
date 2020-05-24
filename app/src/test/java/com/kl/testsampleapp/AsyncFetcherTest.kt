package com.kl.testsampleapp

import com.kl.testsampleapp.util.AsyncStringFetcher
import com.kl.testsampleapp.util.StringFetcher
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.concurrent.CountDownLatch

class AsyncFetcherTest {

    lateinit var fetcher: StringFetcher
    lateinit var asyncFetcher: AsyncStringFetcher
    lateinit var latch: CountDownLatch


    @BeforeEach
    fun setUp() {
        fetcher = spy()
        asyncFetcher = AsyncStringFetcher(fetcher)
        latch = CountDownLatch(1)
    }

    /**
     * fetchAsync内でsleep(1000L)が実行されることで、
     * onSuccess、onFailureが実行される前にテストが終了し、
     * Success扱いになってしまう。
     */
    @Test
    fun fetchAsyncExpectFailButPass_01() {
        asyncFetcher.fetchAsync(
            onSuccess = { fail("onSuccess") },
            onFailure = { fail("onFailure") }
        )
    }

    /**
     * sleep(2000L)が入ったことでコールバックが実行するまで待つことができているが、
     * failが別スレッドで実行されているのでassertが上がらない。
     */
    @Test
    fun fetchAsyncExpectFailButPass_02() {
        asyncFetcher.fetchAsync(
            onSuccess = { fail("onSuccess") },
            onFailure = { fail("onFailure") }
        )
        Thread.sleep(2000L)
    }

    /**
     * RuntimeExceptionを期待しているが、実行されない。
     */
    @Test
    fun fetchAsync_expectPassButFail() {
        Assertions.assertThrows(
            RuntimeException::class.java
        ) {
            asyncFetcher.fetchAsync(
                onSuccess = { throw RuntimeException("onSuccess") },
                onFailure = { throw RuntimeException("onFailure") }
            )
            Thread.sleep(2000L)
        }
    }

    @Test
    fun fetchAsync_success_callbackedProperly() {
        asyncFetcher.fetchAsync(
            onSuccess = { value ->
                assertThat(value).isEqualTo("foo")
                verify(fetcher, times(1)).fetch()
                println("success")
                latch.countDown()
            },
            onFailure = {
                // NOP
            }
        )
        println("fetchAsync invoked.")
        latch.await()
    }

    @Test
    fun fetchAsync_failure_callbackedProperly() {
        doThrow(RuntimeException("ERROR")).whenever(fetcher).fetch()
        asyncFetcher.fetchAsync(
            onSuccess = {
                println("success")
            },
            onFailure = { error ->
                assertThat(error)
                    .isInstanceOf(RuntimeException::class.java)
                    .hasMessageContaining("ERROR")
                verify(fetcher, times(1)).fetch()
                print("failure")
                latch.countDown()
            }
        )
        println("fetchAsync invoked.")
        latch.await()
    }

    @Test
    fun fetchAsync_future_OK() {
        /**
         * そもそもassertを別スレッドで実行するのはよろしくないので、
         * ローカル変数で結果を保持できるようにする。
         */
        var actualValue: String? = null
        var actualError: Throwable? = null

        asyncFetcher.fetchAsync(
            onSuccess = { value ->
                actualValue = value
            },
            onFailure = { error ->
                actualError = error
            }
        ).get() // getで結果の取得を待ち合わせる。

        verify(fetcher, times(1)).fetch()
        assertThat(actualValue).isEqualTo("foo")
        assertThat(actualError).isNull()
    }

    /**
     * 失敗する。
     * 多分元のコードが間違っているorバージョン違いで差分出ている。
     */
    @Test
    fun fetchAsync_future_NG() {
        Mockito.doThrow(RuntimeException("ERROR")).whenever(fetcher).fetch()
        var actualValue: String? = null
        var actualError: Throwable? = null

        asyncFetcher.fetchAsync(
            onSuccess = { value ->
                actualValue = value
            },
            onFailure = { error ->
                actualError = error
            }
        ).get()

        verify(fetcher, times(1)).fetch()
        assertThat(actualValue).isNull()
        assertThat(actualError)
            .isExactlyInstanceOf(RuntimeException::class.java)
            .hasMessage("ERROR")
    }

}