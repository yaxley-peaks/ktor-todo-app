package yaxley.`in`.repositories

import kotlinx.serialization.Serializable

@Serializable
class TodoItem(val title: String, var done: Boolean = false) {
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

data class TodoItemRepository(val items: MutableList<TodoItem>) {
    companion object {
        val items: TodoItemRepository = TodoItemRepository(
            mutableListOf(
                TodoItem("Clean"),
                TodoItem("Sleep"),
                TodoItem("Game"),
                TodoItem("Repeat"),
            )
        )
    }
}

fun TodoItemRepository.addTodoItem(item: TodoItem): TodoItem {
    items.add(item)
    return item
}


