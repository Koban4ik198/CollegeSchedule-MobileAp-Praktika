package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.repository.ScheduleRepository

@Composable
fun FavoritesScreen(
    repository: ScheduleRepository,
    onGroupSelected: (String) -> Unit
) {
    var favoriteGroups by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        repository.getFavorites().collect { favorites ->
            favoriteGroups = favorites
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            Text(
                "Загрузка...",
                color = Color(0xFF3385FF) // Синий текст
            )
        } else if (favoriteGroups.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Нет избранных",
                    tint = Color(0xFF3385FF) // Синяя иконка
                )
                Text(
                    "Нет избранных групп",
                    color = Color(0xFF3385FF) // Синий текст
                )
                Text(
                    "Добавьте группы через выпадающий список",
                    color = Color(0xFF3385FF).copy(alpha = 0.7f), // Полупрозрачный синий
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
        } else {
            Text(
                "Избранные группы",
                color = Color(0xFF00A2FF), // Ярко-голубой заголовок
                modifier = Modifier.padding(bottom = 20.dp)
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favoriteGroups) { groupName ->
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                onGroupSelected(groupName)
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White, // Белый фон карточки
                            contentColor = Color(0xFF3385FF) // Синий текст внутри
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Text(
                            text = groupName,
                            color = Color(0xFF3385FF), // Синий текст группы
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}