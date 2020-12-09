package net.transgene.user_merge

class Printer {

    fun printUsers(entries: List<UserEntry>): String {
        return entries.joinToString("\n") { printUser(it) }
    }

    fun printUser(entry: UserEntry): String {
        return "${entry.name} -> ${entry.emails.joinToString(", ")}"
    }
}