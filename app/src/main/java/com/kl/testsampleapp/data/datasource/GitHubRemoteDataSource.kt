package com.kl.testsampleapp.data.datasource

import com.kl.testsampleapp.data.api.GitHubApi
import com.kl.testsampleapp.model.Repo
import com.kl.testsampleapp.model.Repository
import io.reactivex.Single

class GitHubRemoteDataSource(val gitHubService: GitHubApi) {
    fun listRepos(user: String): Single<List<Repo>> {
        return gitHubService.listRepos(user)
    }
}