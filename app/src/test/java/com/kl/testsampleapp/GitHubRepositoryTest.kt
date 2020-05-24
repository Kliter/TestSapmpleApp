package com.kl.testsampleapp

import com.kl.testsampleapp.data.datasource.LocalDataSource
import com.kl.testsampleapp.data.repository.GitHubRepository
import com.kl.testsampleapp.model.Repo
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.stubbing.Answer

class GitHubRepositoryTest {

    val localDataSource = mock<LocalDataSource>(
        defaultAnswer = Answer { throw RuntimeException() }
    )
    lateinit var gitHubRepository: GitHubRepository

    @BeforeEach
    fun setUp() {
        doReturn(Single.just(listOf(Repo(), Repo(), Repo())))
            .whenever(localDataSource)
            .listRepos(any())
        gitHubRepository = GitHubRepository(localDataSource)
    }

    @Test
    fun verifyNoMoreInteractions() {
        gitHubRepository.listRepos("shiroyama").test().assertComplete().assertNoErrors()
        verify(localDataSource, times(1)).listRepos(eq("shiroyama"))
        verifyNoMoreInteractions(localDataSource) // モックやスパイに対してverify()した以上のインタラクションがないことを検証するためのメソッド。
    }

}