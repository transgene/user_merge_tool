package net.transgene.user_merge

class Printer {

    fun printUsers(entries: List<UserEntry>): String {
        return "[\n\t${entries.joinToString("\n\t") { printUser(it) }}\n]"
    }

    fun printUser(entry: UserEntry): String {
        return "${entry.name} -> ${entry.emails.joinToString(", ")}"
    }
}