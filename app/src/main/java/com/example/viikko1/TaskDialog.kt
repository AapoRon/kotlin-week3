package com.example.viikko1

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.viikko1.domain.Task

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onSave: (title: String) -> Unit
) {
    var title by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Lisää tehtävä") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = title.isNotBlank(),
                onClick = { onSave(title) }
            ) { Text("Tallenna") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Peruuta") }
        }
    )
}

@Composable
fun EditTaskDialog(
    task: Task,
    onDismiss: () -> Unit,
    onSave: (Task) -> Unit,
    onDelete: () -> Unit
) {
    var title by remember { mutableStateOf(task.title) }
    var done by remember { mutableStateOf(task.done) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Muokkaa tehtävää") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                Row {
                    Checkbox(checked = done, onCheckedChange = { done = it })
                    Spacer(Modifier.width(8.dp))
                    Text("Valmis")
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = title.isNotBlank(),
                onClick = { onSave(task.copy(title = title, done = done)) }
            ) { Text("Tallenna") }
        },
        dismissButton = {
            Row {
                TextButton(onClick = onDelete) { Text("Poista") }
                Spacer(Modifier.width(8.dp))
                TextButton(onClick = onDismiss) { Text("Peruuta") }
            }
        }
    )
}
