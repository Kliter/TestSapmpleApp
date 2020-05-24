package com.kl.testsampleapp.data.api

import com.kl.testsampleapp.model.Repo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApi {

    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): Single<List<Repo>>

}