package yaxley.`in`.repositories

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
        }else {
            markDone()
        }
    }
}
data class TodoItemRepository(val items: List<TodoItem>)

val items: TodoItemRepository = TodoItemRepository(listOf(
    TodoItem("Clean"),
    TodoItem("Sleep"),
    TodoItem("Game"),
    TodoItem("Repeat"),
))

