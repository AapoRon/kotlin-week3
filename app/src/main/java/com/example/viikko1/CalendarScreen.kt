package com.example.viikko1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    navController: NavController,
    taskViewModel: TaskViewModel
) {
    val tasks by taskViewModel.tasks.collectAsState()
    var selectedTask by remember { mutableStateOf<Task?>(null) }

    // Ryhmitellään tehtävät päivämäärän mukaan
    val grouped: Map<LocalDate, List<Task>> =
        tasks.sortedBy { it.dueDate }.groupBy { it.dueDate }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kalenteri") },
                actions = {
                    // Takaisin listaan (Home)
                    IconButton(onClick = { navController.navigate(ROUTE_HOME) }) {
                        Icon(Icons.Filled.List, contentDescription = "Lista")
                    }
                    // Settings
                    IconButton(onClick = { navController.navigate(ROUTE_SETTINGS) }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Asetukset")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            LazyColumn {
                grouped.toSortedMap().forEach { (date, tasksForDay) ->

                    item {
                        Text(
                            text = date.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    items(tasksForDay) { task ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("• ${task.title}")

                            TextButton(onClick = { selectedTask = task }) {
                                Text("Edit")
                            }
                        }
                    }
                }
            }
        }
    }

    // Edit-dialogi myös kalenterista
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
