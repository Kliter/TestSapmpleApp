package com.kl.testsampleapp.data.dao

import androidx.room.*
import com.kl.testsampleapp.model.Repository

@Dao
interface RepositoryDao {
    @Insert
    fun insertAll(vararg repositories: Repository)

    @Query("SELECT * FROM repository WHERE owner = :owner")
    fun findByOwner(owner: String): List<Repository>
}

@Database(entities = [Repository::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun repositoryDao(): RepositoryDao
}