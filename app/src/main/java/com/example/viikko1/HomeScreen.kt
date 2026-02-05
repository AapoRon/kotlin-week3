package com.example.viikko1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.viikko1.domain.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    taskViewModel: TaskViewModel
) {
    val tasks by taskViewModel.tasks.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<Task?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tehtävät") },
                actions = {
                    TextButton(onClick = { navController.navigate(ROUTE_CALENDAR) }) {
                        Text("Kalenteri")
                    }
                    TextButton(onClick = { navController.navigate(ROUTE_SETTINGS) }) {
                        Text("Asetukset")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Text("+")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            // Filtterit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { taskViewModel.showAll() }) { Text("Kaikki") }
                Button(onClick = { taskViewModel.filterByDone(false) }) { Text("Tekemättömät") }
                Button(onClick = { taskViewModel.filterByDone(true) }) { Text("Tehdyt") }
            }

            Spacer(Modifier.height(8.dp))

            // Sort
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { taskViewModel.sortByDueDate() }) { Text("Sort") }
                Button(onClick = { taskViewModel.clearSort() }) { Text("Sort off") }
            }

            Spacer(Modifier.height(16.dp))

            // Lista
            LazyColumn {
                items(tasks) { task ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Row {
                            Checkbox(
                                checked = task.done,
                                onCheckedChange = { taskViewModel.toggleDone(task.id) }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(task.title)
                        }

                        Row {
                            TextButton(onClick = { selectedTask = task }) { Text("Edit") }
                            TextButton(onClick = { taskViewModel.removeTask(task.id) }) { Text("Poista") }
                        }
                    }
                }
            }
        }
    }

    // ➕ Add dialog
    if (showAddDialog) {
        AddTaskDialog(
            onDismiss = { showAddDialog = false },
            onSave = { title ->
                taskViewModel.addTask(title)
                showAddDialog = false
            }
        )
    }

    // ✏️ Edit dialog
    selectedTask?.let { task ->
        EditTaskDialog(
            task = task,
            onDismiss = { selectedTask = null },
            onSave = { updated ->
                taskViewModel.updateTask(updated)
                selectedTask = null
            },
            onDelete = {
                taskViewModel.removeTask(task.id)
                selectedTask = null
            }
        )
    }
}
