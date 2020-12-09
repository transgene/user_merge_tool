package net.transgene.user_merge

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test

private const val USER_1 = "USER_1"
private const val USER_2 = "USER_2"
private const val USER_3 = "USER_3"

private const val EMAIL_1 = "aaa@aaa.ru"
private const val EMAIL_2 = "aaa@bbb.ru"
private const val EMAIL_3 = "aaa@ccc.ru"

class MergerTest {

    private val underTest = Merger()

    @Test
    fun mergeUsers_whenListIsEmpty_thenReturnEmptyList() {
        val actual = underTest.mergeUsers(listOf())

        assertTrue(actual.isEmpty())
    }

    @Test
    fun mergeUsers_whenListContainsOneEntry_thenReturnSameEntries() {
        val entry = getUserEntry(USER_1, EMAIL_1)

        val actual = underTest.mergeUsers(listOf(entry))

        assertThat(actual).containsExactly(entry)
    }

    @Test
    fun mergeUsers_whenNoUsersAreOverlapping_thenReturnSameEntries() {
        val entry1 = getUserEntry(USER_1, EMAIL_1)
        val entry2 = getUserEntry(USER_2, EMAIL_2)
        val entry3 = getUserEntry(USER_3, EMAIL_3)

        val actual = underTest.mergeUsers(listOf(entry1, entry2, entry3))

        assertThat(actual).containsExactlyInAnyOrder(entry1, entry2, entry3)
    }

    @Test
    fun mergeUsers_whenUsersAreOverlapping_thenMergeSuchUsers() {
        val entry1 = getUserEntry(USER_1, EMAIL_1)
        val entry2 = getUserEntry(USER_2, EMAIL_2)
        val entry3 = getUserEntry(USER_3, EMAIL_1, EMAIL_3)

        val actual = underTest.mergeUsers(listOf(entry1, entry2, entry3))

        assertThat(actual).hasSize(2)
        assertThat(actual).contains(entry2)
        assertThat(actual).anyMatch { setOf(USER_1, USER_3).contains(it.name) && it.emails == setOf(EMAIL_1, EMAIL_3) }
    }

    private fun getUserEntry(name: String, vararg emails: String): UserEntry {
        return UserEntry(name, emails.toSet())
    }
}