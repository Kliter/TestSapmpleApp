package com.kl.testsampleapp

import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class RxTest {

    val observable = Observable.just("Rx").delay(1, TimeUnit.SECONDS)

    /**
     * onNextに値が流れてくる前にテストスレッドが終了する。
     */
    @Test
    fun observableTestNoGood() {
        observable.subscribeBy(
            onNext = {
                println(it)
                assertThat(it).isEqualTo("Rx")
            }
        )
    }

    @Test
    fun observableTestGood() {
        val subscriber = observable.test()
        subscriber
            .await()
            .assertComplete()
            .assertValue("Rx")
    }

    @Test
    fun observableTestUsedValue() {
        val listObservable: Observable<String> =
            listOf("Giants", "Dogers", "Athletics").toObservable().delay(1, TimeUnit.SECONDS)
        /**
         * values()を使って一回変数に入れることで、
         * 今までと同じようにassertThatを使うことができる。
         */
        val teams: List<String> = listObservable.test().await().assertComplete().values()
        assertThat(teams).containsExactly("Giants", "Dogers", "Athletics")
    }

    @Test
    fun observableTestError() {
        val errorObservable: Observable<RuntimeException> =
            Observable.error(RuntimeException("Oops!"))
        errorObservable
            .test()
            .await()
            .assertNotComplete() // onErrorが呼び出されたことをチェック
            .assertError(RuntimeException::class.java)
            .assertErrorMessage("Oops!")
    }


}