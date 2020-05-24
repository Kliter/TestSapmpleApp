package com.kl.testsampleapp

import com.kl.testsampleapp.data.api.GitHubApi
import com.kl.testsampleapp.data.datasource.GitHubRemoteDataSource
import com.kl.testsampleapp.util.setBodyFromFileName
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MockWebServer {

    /**
     * mockWebServerは完全なHTTPスタックをサポートする、
     * ウェブサーバー上で動作検証するために開発されているテスト用サーバー機能
     */
    val mockWebServer = MockWebServer()

    /**
     * Dispatcherを使うとレスポンスをmockする時に便利。
     */
    val dispatcher: Dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest?): MockResponse {
            return when {
                request == null -> MockResponse().setResponseCode(400)
                request.path == null -> MockResponse().setResponseCode(400)
                request.path.matches(Regex("/users/[a-zA-Z0-9]+/repos")) -> {
                    MockResponse().setResponseCode(200).setBodyFromFileName("users_repos.json")
                }
                else -> MockResponse().setResponseCode(400)
            }
        }
    }

    lateinit var gitHubRemoteDataSource: GitHubRemoteDataSource

    @BeforeEach
    fun setUp() {
        mockWebServer.setDispatcher(dispatcher)
        mockWebServer.start() // startでウェブサーバーの機能を開始

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("")) // 向き先をmockWebServerにしている
            .client(OkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val gitHubApi = retrofit.create(GitHubApi::class.java)
        gitHubRemoteDataSource = GitHubRemoteDataSource(gitHubApi)
    }

    @Test
    fun test() {
        gitHubRemoteDataSource.listRepos("")
            .test()
            .await()
            .assertNotComplete()
        val list = gitHubRemoteDataSource.listRepos("Kliter")
            .test()
            .await()
            .assertNoErrors()
            .assertComplete()
            .values()[0]
        assertThat(list).isNotEmpty
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown() // shutdownでウェブサーバーの機能を終了
    }
}