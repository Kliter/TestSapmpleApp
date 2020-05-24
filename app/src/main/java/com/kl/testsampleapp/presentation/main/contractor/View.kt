package com.kl.testsampleapp.presentation.main

import com.kl.testsampleapp.model.Repo

interface View {
    fun showRepositoryList(list: List<Repo>)
}