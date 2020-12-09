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
    fun getUserEntries_whenEnteredBlankLine_thenStopRetrievingEntries() {
        val testIn = ByteArrayInputStream((
                "$USER_1 -> $EMAIL_1\n" +
                "$USER_2 -> $EMAIL_2\n" +
                "\n"
        ).toByteArray())
        System.setIn(testIn)

        val actual = underTest.getUserEntries()

        assertThat(actual).containsExactlyInAnyOrder(getUserEntry(USER_1, EMAIL_1), getUserEntry(USER_2, EMAIL_2))
    }

    @Test
    fun extractEntries_whenFormatIsInvalid_thenPrintFormatMismatchError() {
        val lines = listOf("invalid")

        val actual = underTest.extractEntries(lines)

        assertThat(actual).isNull()
        assertThat(testOut.toString().trim()).isEqualTo(FORMAT_MISMATCH_MESSAGE_TEMPLATE, 1)
    }

    @Test
    fun extractEntries_whenEnteredDuplicateUser_thenPrintUserAlreadyInListError() {
        val lines = listOf("$USER_1 -> $EMAIL_1", "$USER_1 -> $EMAIL_2")

        val actual = underTest.extractEntries(lines)

        assertThat(actual).isNull()
        assertThat(testOut.toString().trim()).isEqualTo(USER_ALREADY_IN_LIST_MESSAGE_TEMPLATE, 2, USER_1)
    }

    @Test
    fun extractEntries_whenAllLinesAreValid_thenReturnListOfEntries() {
        val lines = listOf("$USER_1 -> $EMAIL_1", "$USER_2 -> $EMAIL_2")

        val actual = underTest.extractEntries(lines)

        assertThat(actual).containsExactlyInAnyOrder(getUserEntry(USER_1, EMAIL_1), getUserEntry(USER_2, EMAIL_2))
    }

    @Test
    fun extractEntries_whenUserHasSeveralEmails_thenExtractThemAll() {
        val lines = listOf("$USER_1 -> $EMAIL_1, $EMAIL_2, $EMAIL_3")

        val actual = underTest.extractEntries(lines)

        assertThat(actual).containsExactly(getUserEntry(USER_1, EMAIL_1, EMAIL_2, EMAIL_3))
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