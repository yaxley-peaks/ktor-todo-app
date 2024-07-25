package yaxley.`in`.repositories

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import yaxley.`in`.db.TodoDAO
import yaxley.`in`.db.TodoTable
import yaxley.`in`.db.daoToModel
import yaxley.`in`.db.suspendTransaction

@Suppress("RedundantNullableReturnType")
class MySqlTodoItemRepository : TodoItemRepository {
    override suspend fun allItems(): List<TodoItem> =
        suspendTransaction { TodoDAO.all().map(::daoToModel) }

    override suspend fun get(id: Int): TodoItem? {
        return suspendTransaction {
            daoToModel(TodoDAO[id])
        }
    }

    override suspend fun add(item: TodoItem) {
        return suspendTransaction {
            TodoDAO.new {
                title = item.title
                done = item.done
            }
        }
    }

    override suspend fun add(title: String, done: Boolean) {
        return suspendTransaction {
            TodoDAO.new {
                this.title = title
                this.done = done
            }
        }
    }

    override suspend fun remove(id: Int) {
        return suspendTransaction {
            TodoTable.deleteWhere { TodoTable.id eq id }
        }
    }

    override suspend fun markAsDone(item: TodoItem): TodoItem {
        return suspendTransaction {
            val i = TodoDAO[item.id]
            i.done = true
            daoToModel(i)
        }
    }

    override suspend fun markAsPending(item: TodoItem): TodoItem {
        return suspendTransaction {
            val i = TodoDAO[item.id]
            i.done = false
            daoToModel(i)
        }
    }

}