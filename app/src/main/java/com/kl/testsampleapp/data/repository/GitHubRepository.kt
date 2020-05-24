package com.kl.testsampleapp.data.repository

import com.kl.testsampleapp.data.datasource.LocalDataSource
import com.kl.testsampleapp.model.Repo
import io.reactivex.Single

class GitHubRepository(val localDataSource: LocalDataSource) {
    fun listRepos(user: String): Single<List<Repo>> {
        return localDataSource.listRepos(user)
    }
}