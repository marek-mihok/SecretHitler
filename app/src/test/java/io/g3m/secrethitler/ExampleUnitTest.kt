package io.g3m.secrethitler

import io.g3m.secrethitler.common.PlayersInfo
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testRandom() {
        PlayersInfo.setNames(arrayListOf("1",
                "2", "3", "4",
                "n"))
        val roles = PlayersInfo.getPlayerRoles()
        print(roles)

    }
}
