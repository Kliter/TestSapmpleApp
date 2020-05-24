package com.kl.testsampleapp.data.datasource

import com.kl.testsampleapp.model.Repo
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class LocalDataSource {
    fun listRepos(user: String): Single<List<Repo>> {
        val repos = listOf(
            Repo(1, "repo_1", "Repository_1", false, null),
            Repo(2, "repo_2", "Repository_2", false, null)
        )
        return Single.just(repos).delay(1, TimeUnit.SECONDS)
    }
}