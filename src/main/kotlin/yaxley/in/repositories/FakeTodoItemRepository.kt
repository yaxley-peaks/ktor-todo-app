package yaxley.`in`.repositories

class FakeTodoItemRepository : TodoItemRepository {
    companion object {
        private var lastId = 4
    }
    private val items =
        mutableListOf(
            TodoItem(1, "Clean"),
            TodoItem(2, "Sleep"),
            TodoItem(3, "Game"),
            TodoItem(4, "Repeat", true),
        )

    override suspend fun allItems(): List<TodoItem> {
        return items
    }

    override suspend fun get(id: Int): TodoItem? {
        return items.find { it.id == id }
    }

    override suspend fun add(item: TodoItem) {
        items.add(item)
    }

    override suspend fun add(title: String, done: Boolean){
        add(TodoItem(++lastId, title, done))
    }

    override suspend fun remove(id: Int) {
        items.removeIf { it.id == id }
    }

    override suspend fun markAsDone(item: TodoItem): TodoItem {
        val i = items.find { it.id == item.id }!!
        i.done = true
        return i
    }

    override suspend fun markAsPending(item: TodoItem): TodoItem {
        val i = items.find { it.id == item.id }!!
        i.done = false
        return i
    }
}