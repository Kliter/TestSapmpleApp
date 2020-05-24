package com.kl.testsampleapp

import com.kl.testsampleapp.data.repository.GitHubRepository
import com.kl.testsampleapp.model.Repo
import com.kl.testsampleapp.presentation.main.ListPresenter
import com.kl.testsampleapp.presentation.main.View
import com.kl.testsampleapp.rule.RxImmediateSchedulerRule
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.jupiter.api.Test

class PresenterTest {

    val view: View = mock()
    val repository: GitHubRepository = mock{
        on { listRepos(any()) } doReturn Single.just(listOf(
            Repo(), Repo(), Repo()
        ))
    }
    val presenter = ListPresenter(view,repository)

    @get:Rule
    val rule = RxImmediateSchedulerRule()

    @Test
    fun viewAndPresenterTest() {
        presenter.getRepositoryList("shiroyama")
        argumentCaptor<List<Repo>>().apply {
            verify(view, times(1)).showRepositoryList(capture())
            assertThat(firstValue).hasSize(3)
        }
    }
}