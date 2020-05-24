package com.kl.testsampleapp

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.*
import java.sql.Connection
import java.sql.DriverManager
import java.util.stream.Stream

class ParameterizedTest {

    @Nested
    inner class FixedValueTest {
        @ParameterizedTest
        /**
         * @ValueSourceを使用すると、
         * intsに指定した配列の要素がテストメソッドの引数numに一つずつ渡されて、
         * 各要素についてテストメソッドが実行される。
         */
        @ValueSource(ints = [2, 4, 6, 8, 10])
        fun isEven(num: Int) {
            assertTrue(num % 2 == 0)
        }
    }

    enum class JvmLang {
        JAVA,
        KOTLIN,
        GROOVY,
        SCALA,
        CLOJURE,
        ETA,
        JRUBY,
        JYTHON
    }

    @Nested
    inner class EnumTest {
        @ParameterizedTest
        /**
         * @EnumSourceは指定した名前のEnum全てをパラメータとして使用してテストする。
         * クラスオブジェクトを指定すると全てのEnumでテストを実行する。
         */
        @EnumSource(JvmLang::class)
        fun useEnum(jvmLang: JvmLang) {
            assertNotNull(jvmLang)
        }

        @ParameterizedTest
        /**
         * 名前を指定すると指定したEnumだけでテストする。
         */
        @EnumSource(
            JvmLang::class,
            names = ["JAVA", "KOTLIN"]
        )
        fun androidOfficialJvmLangs(jvmLang: JvmLang) {
            assertNotNull(jvmLang)
        }

        @ParameterizedTest
        /**
         * modeにE EnumSource.Mode.EXCLUDEを指定すると、指定したEnum以外をパラメータで使用する。
         */
        @EnumSource(
            JvmLang::class,
            names = ["GROOVY", "JRUBY", "JYTHON"],
            mode = EnumSource.Mode.EXCLUDE
        )
        fun excludeScriptingLangs(jvmLang: JvmLang) {
            assertNotNull(jvmLang)
        }

        @ParameterizedTest
        /**
         * namesの正規表現に一致するEnumをテストする。
         */
        @EnumSource(
            JvmLang::class,
            mode = EnumSource.Mode.MATCH_ALL,
            names = ["^J.+"]
        )
        fun jvmLangShouldStartsWithJ(jvmLang: JvmLang) {
            assertEquals('J', jvmLang.name[0])
        }
    }

    companion object Factory {

        @JvmStatic
        fun additionParameterFactory(): Stream<Arguments> =
            Stream.of(
                Arguments.of(1, 2, 3),
                Arguments.of(10, 20, 30),
                Arguments.of(100, 200, 300)
            )
    }

    @ParameterizedTest
    /**
     * @MethodSourceメソッドを使うことでFactoryメソッドを指定することができる。
     * Factoryメソッドは基本的にstaticかつ引数なしで、戻り値はStream、Iterable、Iteratorを実装している型である必要がある。
     */
    @MethodSource("additionParameterFactory")
    fun addition_isCollect(a: Int, b: Int, expect: Int) {
        assertEquals(expect, a + b)
    }

    @Nested
    inner class ArgumentSourceTest {
        /**
         * データベースのコネクションを貼り続ける例
         */
        inner class DatabaseArgumentProvider : ArgumentsProvider {

            // Storeに保持しているコネクションを、Store破棄時にclose()するためのラッパー
            inner class CloseableConnection(
                connection: Connection
            ) : ExtensionContext.Store.CloseableResource, Connection by connection

            override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
                val connection = context.getStore(ExtensionContext.Namespace.create(context.testClass))
                    .let {
                        it.getOrComputeIfAbsent(
                            "connection",
                            {
                                CloseableConnection(
                                    DriverManager.getConnection("jdbc:h2:./testdata", "sa", "")
                                )
                            },
                            CloseableConnection::class.java
                        )
                    }
                val resultSet = connection.prepareStatement("select * from TESTDATA").executeQuery()
                val args = mutableListOf<Arguments>()
                while (resultSet.next()) {
                    args.add(
                        Arguments.of(
                            resultSet.getInt("PARAM1"),
                            resultSet.getDouble("PARAM2"),
                            resultSet.getString("PARAM3")
                        )
                    )
                }
                return args.stream()
            }
        }

        @ParameterizedTest
        @ArgumentsSource(DatabaseArgumentProvider::class)
        fun usingDataSource(param1: Int, param2: Double, param3: String) {
            assertNotNull(param1)
            assertNotNull(param2)
            assertNotNull(param3)
        }
    }
}