package net.transgene.user_merge

class Merger {

    fun mergeUsers(entries: List<UserEntry>): List<UserEntry> {
        return if (entries.isEmpty()) {
            entries
        } else {
            mergeRecursive(entries, entries.map { it.emails }.reduce { acc, set -> acc + set }.size)
        }
    }

    private fun mergeRecursive(entries: List<UserEntry>, emailsCount: Int): List<UserEntry> {
        if (entries.size < 2) {
            return entries
        }
        val pivot = entries[0]
        val entriesNotEqualToPivot = mutableListOf<UserEntry>()
        var emailsCountInResult = 0

        entries.drop(1).forEach {
            val (_, currentEmails) = it
            val (_, pivotEmails) = pivot

            val bothEmails = currentEmails + pivotEmails
            if (bothEmails.size == currentEmails.size + pivotEmails.size) {
                entriesNotEqualToPivot += it
                emailsCountInResult += currentEmails.size
            } else {
                pivot.emails = pivot.emails + currentEmails
            }
        }
        emailsCountInResult += pivot.emails.size

        val result = entriesNotEqualToPivot + pivot
        return if (emailsCountInResult > emailsCount) mergeRecursive(result, emailsCount) else result
    }
}

data class UserEntry(val name: String, var emails: Set<String>)