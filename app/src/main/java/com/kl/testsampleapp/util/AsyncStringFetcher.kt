package com.kl.testsampleapp.util

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class AsyncStringFetcher(val fetcher: StringFetcher) {
    val executor: ExecutorService = Executors.newCachedThreadPool()

    /**
     * Futureを使って非同期処理の結果を表現することで、
     * 非同期処理の結果をテストスレッドで同期的に取得できる。
     */
    fun fetchAsync(
        onSuccess: (value: String) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ): Future<*> {
        return executor.submit {
            try {
                val value = fetcher.fetch()
                onSuccess.invoke(value)
            } catch (error: Throwable) {
                onFailure.invoke(error)
            }
        }
    }
}