@file:Suppress("unused")

package yaxley.`in`.repositories

import kotlinx.serialization.Serializable

@Serializable
class TodoItem(
    val id: Int = 0,
    val title: String,
    var done: Boolean = false,
) {
    fun markDone() {
        done = true
    }

    fun markPending() {
        done = false
    }

    fun toggleDone() {
        if (done) {
            markPending()
        } else {
            markDone()
        }
    }
}

interface TodoItemRepository {
   suspend fun allItems(): List<TodoItem>
   suspend fun get(id: Int): TodoItem?
   suspend fun add(item: TodoItem)
   suspend fun add(title: String, done: Boolean)
   suspend fun remove(id: Int)
   suspend fun markAsDone(item: TodoItem): TodoItem
   suspend fun markAsPending(item: TodoItem): TodoItem
}

//data class TodoItemRepository(val items: MutableList<TodoItem>) {
//    companion object {
//        private var lastId: Int = 4
//        val items: TodoItemRepository = TodoItemRepository(
//            )
//        )
//    }
//
//    private fun addTodoItem(item: TodoItem): TodoItem {
//        if (items.any { it.id == item.id }) {
//            throw IllegalStateException("Duplicate task id (${item.id}) not allowed")
//        }
//        items.add(item)
//        return item
//    }
//
//    fun addTodoItem(title: String, done: Boolean): TodoItem {
//        val item = TodoItem(++lastId, title, done)
//        return addTodoItem(item)
//    }
//}
