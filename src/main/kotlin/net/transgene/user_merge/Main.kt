package net.transgene.user_merge

fun main() {
    val supplier = ConsoleUserSupplier()
    val merger = Merger()
    val printer = Printer()

    val entries = supplier.getUserEntries()

    val result = merger.mergeUsers(entries)
    println("Users merged: \n\n${printer.printUsers(result)}\n")
}
