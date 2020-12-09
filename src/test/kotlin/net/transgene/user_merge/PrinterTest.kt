package net.transgene.user_merge

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

private const val USER_1 = "USER_1"
private const val USER_2 = "USER_2"

private const val EMAIL_1 = "aaa@aaa.ru"
private const val EMAIL_2 = "aaa@bbb.ru"
private const val EMAIL_3 = "aaa@ccc.ru"

class PrinterTest {

    private val underTest = Printer()

    @Test
    fun printUser_whenUserHasOneEmail_printItCorrectly() {
        val entry = getUserEntry(USER_1, EMAIL_1)

        val actual = underTest.printUser(entry)

        assertThat(actual).isEqualTo("%s -> %s", USER_1, EMAIL_1)
    }

    @Test
    fun printUser_whenUserHasSeveralEmails_printThemCorrectly() {
        val entry = getUserEntry(USER_1, EMAIL_1, EMAIL_2)

        val actual = underTest.printUser(entry)

        assertThat(actual).isEqualTo("%s -> %s, %s", USER_1, EMAIL_1, EMAIL_2)
    }

    @Test
    fun printUsers_whenSuppliedWithListOfUserEntries_printThemCorrectly() {
        val entry1 = getUserEntry(USER_1, EMAIL_1)
        val entry2 = getUserEntry(USER_2, EMAIL_2)

        val actual = underTest.printUsers(listOf(entry1, entry2))

        assertThat(actual).isEqualTo("[\n\t%s -> %s\n\t%s -> %s\n]", USER_1, EMAIL_1, USER_2, EMAIL_2)
    }

    private fun getUserEntry(name: String, vararg emails: String): UserEntry {
        return UserEntry(name, emails.toSet())
    }
}