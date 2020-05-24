package com.kl.testsampleapp.data.datasource

import com.kl.testsampleapp.data.dao.AppDatabase
import com.kl.testsampleapp.model.Repository

class RepositoryLocalDataSource(val db: AppDatabase) {
    fun insertAll(vararg repositories: Repository) {
        db.repositoryDao().insertAll(*repositories)
    }
    fun findByOwner(owner: String): List<Repository> {
        return db.repositoryDao().findByOwner(owner)
    }
}