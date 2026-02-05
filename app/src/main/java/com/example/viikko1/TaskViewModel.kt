package com.example.viikko1

import androidx.lifecycle.ViewModel
import com.example.viikko1.domain.Task
import com.example.viikko1.domain.mockTasks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class TaskViewModel : ViewModel() {

    // Source of truth
    private val _allTasks = MutableStateFlow<List<Task>>(mockTasks)

    // UI: näytettävä lista (voi olla filtteroitu/sortattu)
    private val _tasks = MutableStateFlow<List<Task>>(mockTasks)
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private var currentFilter: Boolean? = null
    private var sortByDate: Boolean = false

    private fun apply() {
        var result = _allTasks.value

        currentFilter?.let { done ->
            result = result.filter { it.done == done }
        }

        if (sortByDate) {
            result = result.sortedBy { it.dueDate }
        }

        _tasks.value = result
    }

    fun showAll() {
        currentFilter = null
        apply()
    }

    fun filterByDone(done: Boolean) {
        currentFilter = done
        apply()
    }

    fun sortByDueDate() {
        sortByDate = true
        apply()
    }

    fun clearSort() {
        sortByDate = false
        apply()
    }

    fun addTask(title: String) {
        val newId = (_allTasks.value.maxOfOrNull { it.id } ?: 0) + 1
        val newTask = Task(
            id = newId,
            title = title,
            description = "",
            priority = 1,
            dueDate = LocalDate.now().plusDays(1),
            done = false
        )

        _allTasks.value = _allTasks.value + newTask
        apply()
    }

    fun toggleDone(id: Int) {
        _allTasks.value = _allTasks.value.map {
            if (it.id == id) it.copy(done = !it.done) else it
        }
        apply()
    }

    fun removeTask(id: Int) {
        _allTasks.value = _allTasks.value.filterNot { it.id == id }
        apply()
    }

    fun updateTask(updated: Task) {
        _allTasks.value = _allTasks.value.map {
            if (it.id == updated.id) updated else it
        }
        apply()
    }
}
