package com.kl.testsampleapp

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.kl.testsampleapp.data.datasource.RepositoryLocalDataSource
import com.kl.testsampleapp.data.dao.AppDatabase
import com.kl.testsampleapp.model.Repository
import org.assertj.core.api.Assertions.assertThat
import org.junit.experimental.runners.Enclosed
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * EnclosedRunnerを使うことでテストを構造化でき、可読性が上がる。
 */
@RunWith(Enclosed::class)
class EnclosedTest {
    abstract class DBTest {
        lateinit var repositoryLocalDataSource: RepositoryLocalDataSource

        /**
         * 抽象クラスを作ってRoomデータベースを初期化し、
         * サブクラスで共有するテスト対象のRepositoryLocalDataSourceを用意する。
         */
        @BeforeEach
        fun setUpParent() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            val db = Room
                .databaseBuilder(context, AppDatabase::class.java, "DB")
                .allowMainThreadQueries()
                .build()
            repositoryLocalDataSource =
                RepositoryLocalDataSource(db)
        }

        @AfterEach
        fun tearDownParent() {
        }
    }

    // DBTestを継承することでsetUpParentが事前に実行され、その後の処理だけ分けることができる。
    /**
     * レコードが空の状態から始まるテストケースを作る。
     */
    @RunWith(RobolectricTestRunner::class)
    class BlankRecord : DBTest() {
        @Test
        fun insertAll_successfully_persist_record() {
            repositoryLocalDataSource.insertAll(Repository(0, "hello", "hello", "kk "))
            val shiroyamaOwners = repositoryLocalDataSource.findByOwner("kk")
            assertThat(shiroyamaOwners).hasSize(1)
        }
    }

    /**
     * レコードが入った状態か始まるテストケースを作る。
     */
    @RunWith(RobolectricTestRunner::class)
    class RecordPrepared : DBTest() {
        @BeforeEach
        fun setUp() {
            repositoryLocalDataSource.insertAll(
                Repository(1, "hello", "hello", "shiroyama"),
                Repository(2, "world", "world", "shiroyama"),
                Repository(3, "yay!", "yay!", "yamazaki")
            )
        }

        @Test
        fun findByOwner_givenShiroyama_returnsSizeCount2() {
            val shiroyamaOwners = repositoryLocalDataSource.findByOwner("shiroyama")
            assertThat(shiroyamaOwners).hasSize(2)
        }

        @Test
        fun findByOwner_givenYamazaki_returnsSizeCount1() {
            val yamazakiOwners = repositoryLocalDataSource.findByOwner("yamazaki")
            assertThat(yamazakiOwners).hasSize(1)
        }
    }
}