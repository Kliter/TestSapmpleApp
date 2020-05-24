package com.kl.testsampleapp.util

import com.kl.testsampleapp.data.repository.Tweet
import com.kl.testsampleapp.data.repository.TweetRepository
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.subscribeBy

class TweetFetcher(
    val repository: TweetRepository,
    val subscribeScheduler: Scheduler,
    val observeScheduler: Scheduler
) {

    fun recents(onSuccess: (List<Tweet>) -> Unit, onError: (Throwable) -> Unit) {
        repository
            .recents()
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
            .subscribeBy(
                onSuccess = onSuccess,
                onError = onError
            )
    }
}