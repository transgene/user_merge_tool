package net.transgene.user_merge

fun main() {
    val supplier = ConsoleUserSupplier()
    val merger = Merger()
    val printer = Printer()

    val entries = supplier.get()
    
    println("Merging the list of users: \n${printer.printUsers(entries)}\n")
    val result = merger.mergeUsers(entries)
    println("Users merged: \n${printer.printUsers(result)}\n")
}
