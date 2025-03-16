package org.hse.smartcalendar.ui.elements

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.hse.smartcalendar.data.DailyTaskType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedTypeSelectionMenu(
    expanded: MutableState<Boolean>,
    type: MutableState<DailyTaskType>
) {
    val types = DailyTaskType.entries
    val currentType = remember { mutableStateOf(types[0].toString()) }
    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value },
    ) {
        TextField(
            readOnly = true,
            value = type.value.name,
            onValueChange = { },
            label = { Text("Types") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded.value
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }
        ) {
            types.forEach { selectedType ->
                DropdownMenuItem(
                    text = { Text(selectedType.name) },
                    onClick = {
                        type.value = selectedType
                        expanded.value = false
                    }
                )
            }
        }
    }
}