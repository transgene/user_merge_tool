package net.transgene.user_merge

import org.assertj.core.api.Assertions.assertThat
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

private const val USER_1 = "USER_1"
private const val USER_2 = "USER_2"

private const val EMAIL_1 = "aaa@aaa.ru"
private const val EMAIL_2 = "aaa@bbb.ru"
private const val EMAIL_3 = "aaa@ccc.ru"

class ConsoleUserSupplierTest {

    private val stdIn = System.`in`
    private val stdOut = System.out

    private lateinit var testOut: ByteArrayOutputStream

    private val underTest = ConsoleUserSupplier()

    @Test
    fun extractEntry_whenFormatIsInvalid_thenPrintFormatMismatchError() {
        val line = "invalid"

        val actual = underTest.extractEntry(line, mutableSetOf())

        assertThat(actual).isNull()
        assertThat(testOut.toString().trim()).isEqualTo(FORMAT_MISMATCH_MESSAGE)
    }

    @Test
    fun extractEntry_whenEnteredDuplicateUser_thenPrintUserAlreadyInListError() {
        val users = mutableSetOf(USER_1)
        val line = "$USER_1 -> $EMAIL_1"

        val actual = underTest.extractEntry(line, users)

        assertThat(actual).isNull()
        assertThat(testOut.toString().trim()).isEqualTo(USER_ALREADY_IN_LIST_MESSAGE_TEMPLATE, USER_1)
    }

    @Test
    fun extractEntry_whenUserHasOneEmail_thenCorrectlyExtractIt() {
        val line = "$USER_1 -> $EMAIL_1"

        val actual = underTest.extractEntry(line, mutableSetOf())

        assertThat(actual!!.name).isEqualTo(USER_1)
        assertThat(actual.emails).containsExactly(EMAIL_1)
    }

    @Test
    fun extractEntry_whenUserHasSeveralEmails_thenCorrectlyExtractThemAll() {
        val line = "$USER_1 -> $EMAIL_1, $EMAIL_2, $EMAIL_3"

        val actual = underTest.extractEntry(line, mutableSetOf())

        assertThat(actual!!.name).isEqualTo(USER_1)
        assertThat(actual.emails).containsExactlyInAnyOrder(EMAIL_1, EMAIL_2, EMAIL_3)
    }

    @Test
    fun get_whenEnteredBlankLine_thenStopRetrievingEntries() {
        val testIn = ByteArrayInputStream((
                "$USER_1 -> $EMAIL_1\n" +
                "$USER_2 -> $EMAIL_2\n" +
                "\n"
        ).toByteArray())
        System.setIn(testIn)

        val actual = underTest.get()

        assertThat(actual).containsExactlyInAnyOrder(getUserEntry(USER_1, EMAIL_1), getUserEntry(USER_2, EMAIL_2))
    }

    @BeforeTest
    fun setSystemOut() {
        testOut = ByteArrayOutputStream()
        System.setOut(PrintStream(testOut))
    }

    @AfterTest
    fun restoreSystemInOut() {
        System.setIn(stdIn)
        System.setOut(stdOut)
    }

    private fun getUserEntry(name: String, vararg emails: String): UserEntry {
        return UserEntry(name, emails.toSet())
    }
}