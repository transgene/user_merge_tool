package net.transgene.user_merge

import java.util.function.Supplier

interface UserSupplier : Supplier<List<UserEntry>>


internal const val FORMAT_MISMATCH_MESSAGE = "This input does not match the format: username -> email[, email]. Please try again."

internal const val USER_ALREADY_IN_LIST_MESSAGE_TEMPLATE = "User with name '%s' is already in list. Please provide another name."

class ConsoleUserSupplier : UserSupplier {

    private val lineRegex = Regex("^(\\w+)\\s->\\s(((?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)]))(,\\s(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)]))*)\$")

    private val emailDelimiterRegex = Regex(",\\s")

    override fun get(): List<UserEntry> {
        val entries = mutableListOf<UserEntry>()
        val users = mutableSetOf<String>()

        println("Please provide a list of user entries, one by one. Enter empty line to finish.")
        while (true) {
            print("Entry: ")
            val line = readLine()
            if (line == null || line.isBlank()) {
                return entries
            } else {
                val entry = extractEntry(line, users)
                if (entry != null) {
                    entries.add(entry)
                }
            }
        }
    }

    internal fun extractEntry(line: String, enteredUsers: MutableSet<String>): UserEntry? {
        var entry: UserEntry? = null
        val matchResult = lineRegex.find(line)
        if (matchResult == null) {
            println(FORMAT_MISMATCH_MESSAGE)
        } else {
            val username = matchResult.groupValues[1]
            if (enteredUsers.contains(username)) {
                println(USER_ALREADY_IN_LIST_MESSAGE_TEMPLATE.format(username))
            } else {
                enteredUsers.add(username)
                val emails = matchResult.groupValues[2].split(emailDelimiterRegex)
                entry = UserEntry(username, emails.toSet())
            }
        }
        return entry
    }
}
