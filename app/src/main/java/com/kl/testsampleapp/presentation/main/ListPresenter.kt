package com.kl.testsampleapp.presentation.main

import com.kl.testsampleapp.data.repository.GitHubRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ListPresenter(val view: View, val repository: GitHubRepository): Presenter { // Viewを受け取るようにすることで、Activityを使わなくて済む。
    override fun getRepositoryList(name: String) {
        repository.listRepos(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy { view.showRepositoryList(it) }
    }
}