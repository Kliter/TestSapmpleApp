package com.kl.testsampleapp.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kl.testsampleapp.R
import com.kl.testsampleapp.data.datasource.LocalDataSource
import com.kl.testsampleapp.data.repository.GitHubRepository
import com.kl.testsampleapp.model.Repo

class MainActivity : AppCompatActivity(), View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = GitHubRepository(LocalDataSource())
        val presenter = ListPresenter(this, repository)
        presenter.getRepositoryList("shiroyama")
    }

    override fun showRepositoryList(list: List<Repo>) {

    }
}
