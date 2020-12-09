package net.transgene.user_merge

interface UserSupplier {
    fun getUserEntries(): List<UserEntry>
}


internal const val FORMAT_MISMATCH_MESSAGE_TEMPLATE = "Error in line #%d: invalid format. Should be: username -> email[, email]. Please enter the entries again."

internal const val USER_ALREADY_IN_LIST_MESSAGE_TEMPLATE = "Error in line #%d: user with name '%s' is already in list. Please enter the entries again."

class ConsoleUserSupplier : UserSupplier {

    private val lineRegex = Regex("^(\\w+)\\s->\\s(((?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)]))(,\\s(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)]))*)\$")

    private val emailDelimiterRegex = Regex(",\\s")

    override fun getUserEntries(): List<UserEntry> {
        println("Please provide a list of user entries in the following format: username -> email[, email], each entry on a separate line.")
        println("Enter empty line to finish.")

        var entries: List<UserEntry>? = null
        while (entries == null) {
            val rawEntries = getLinesFromConsole()
            entries = extractEntries(rawEntries)
        }
        return entries
    }

    internal fun extractEntries(lines: List<String>): List<UserEntry>? {
        val entries = mutableListOf<UserEntry>()
        val users = mutableSetOf<String>()

        lines.forEachIndexed { i, line ->
            val matchResult = lineRegex.find(line)
            if (matchResult == null) {
                println(FORMAT_MISMATCH_MESSAGE_TEMPLATE.format(i + 1))
                return null
            } else {
                val username = matchResult.groupValues[1]
                if (users.contains(username)) {
                    println(USER_ALREADY_IN_LIST_MESSAGE_TEMPLATE.format(i + 1, username))
                    return null
                } else {
                    users.add(username)
                    val emails = matchResult.groupValues[2].split(emailDelimiterRegex)
                    entries.add(UserEntry(username, emails.toSet()))
                }
            }
        }
        return entries
    }

    private fun getLinesFromConsole(): List<String> {
        val lines = mutableListOf<String>()

        println("Entries:")
        while (true) {
            val line = readLine()
            if (line == null || line.isBlank()) {
                return lines
            } else {
                lines.add(line.trim())
            }
        }
    }
}
