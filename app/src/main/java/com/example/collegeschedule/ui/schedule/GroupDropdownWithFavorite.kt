package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.GroupDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDropdownWithFavorite(
    groups: List<GroupDto>,
    selectedGroup: GroupDto?,
    onGroupSelected: (GroupDto) -> Unit,
    favoriteGroups: List<String>,
    onFavoriteToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(selectedGroup?.groupName ?: "") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier.fillMaxWidth()
    ) {
        TextField(
            value = searchText,
            onValueChange = { query ->
                searchText = query
                if (query.isNotEmpty()) {
                    expanded = true
                }
            },
            label = { Text("Выберите группу") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            // СТИЛИ В ХОЛОДНОЙ ЦВЕТОВОЙ ГАММЕ:
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF0F9FF), // Светло-голубой фон при фокусе
                unfocusedContainerColor = Color.White, // Белый фон
                focusedTextColor = Color(0xFF3385FF), // Текст синий
                unfocusedTextColor = Color(0xFF3385FF), // Текст синий
                focusedIndicatorColor = Color(0xFF00A2FF), // Индикатор ярко-голубой
                unfocusedIndicatorColor = Color(0xFF3385FF), // Индикатор синий
                focusedLabelColor = Color(0xFF00A2FF), // Лейбл ярко-голубой
                unfocusedLabelColor = Color(0xFF3385FF), // Лейбл синий
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .background(Color.White) // Белый фон поля
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White) // Белый фон выпадающего списка
        ) {
            groups
                .filter { it.groupName.contains(searchText, ignoreCase = true) }
                .forEach { group ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = group.groupName,
                                    modifier = Modifier.weight(1f),
                                    color = Color(0xFF3385FF) // Синий текст групп
                                )
                                IconButton(
                                    onClick = {
                                        onFavoriteToggle(group.groupName)
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (group.groupName in favoriteGroups) {
                                            Icons.Default.Favorite
                                        } else {
                                            Icons.Default.FavoriteBorder
                                        },
                                        contentDescription = if (group.groupName in favoriteGroups) {
                                            "Удалить из избранного"
                                        } else {
                                            "Добавить в избранное"
                                        },
                                        tint = if (group.groupName in favoriteGroups) {
                                            Color(0xFF00A2FF) // Ярко-голубой для избранных
                                        } else {
                                            Color(0xFF3385FF) // Синий для не избранных
                                        }
                                    )
                                }
                            }
                        },
                        onClick = {
                            onGroupSelected(group)
                            searchText = group.groupName
                            expanded = false
                        },
                        modifier = Modifier.background(Color.White) // Белый фон пунктов
                    )
                }
        }
    }
}