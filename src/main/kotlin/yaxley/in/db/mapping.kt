package yaxley.`in`.db

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import yaxley.`in`.repositories.TodoItem

object TodoTable : IntIdTable("TodoList") {
    val title = varchar("title", 255)
    val done = bool("Done")
}

class TodoDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TodoDAO>(TodoTable)

    var title by TodoTable.title
    var done by TodoTable.done
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun daoToModel(dao: TodoDAO) = TodoItem(
    id = dao.id.value,
    title = dao.title,
    done = dao.done,
)