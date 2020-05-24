package com.kl.testsampleapp

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.kl.testsampleapp.data.datasource.RepositoryLocalDataSource
import com.kl.testsampleapp.data.dao.AppDatabase
import com.kl.testsampleapp.model.Repository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RepositoryLocalDataSourceTest {
    lateinit var repositoryLocalDataSource: RepositoryLocalDataSource

    @BeforeEach
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val db = Room
            .databaseBuilder(context, AppDatabase::class.java, "DB")
            .allowMainThreadQueries()
            .build()
        repositoryLocalDataSource =
            RepositoryLocalDataSource(db)
    }

    /**
     * テストケース同士でレコードが共有されることはない。
     * 同名DBにアクセスしても干渉し合わない。
     */
    @Test
    fun insertAll_finishesSuccessfully() {
        var list = repositoryLocalDataSource.findByOwner("shiroyama")
        assertThat(list).isEmpty()

        val owner = "shiroyama"
        repositoryLocalDataSource.insertAll(
            Repository(1, "hello", "hello", owner),
            Repository(2, "world", "world", owner)
        )

        list = repositoryLocalDataSource.findByOwner("shiroyama")
        assertThat(list).hasSize(2)
    }

    @Test
    fun findByOwner_expectsEmpty() {
        val list = repositoryLocalDataSource.findByOwner("shiroyama")
        assertThat(list).isEmpty()
    }
}