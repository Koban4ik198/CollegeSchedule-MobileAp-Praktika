package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.GroupDto
import com.example.collegeschedule.data.repository.ScheduleRepository
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.network.RetrofitInstance
import com.example.collegeschedule.utils.getWeekDateRange
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ScheduleScreen(
    repository: ScheduleRepository,
    initialGroupName: String? = null
) {

    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var groups by remember { mutableStateOf<List<GroupDto>>(emptyList()) }
    var selectedGroup by remember { mutableStateOf<GroupDto?>(null) }
    var loadingSchedule by remember { mutableStateOf(false) }
    var loadingGroups by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    var favoriteGroups by remember { mutableStateOf<List<String>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            groups = repository.loadAllGroups()
            if (groups.isNotEmpty()) {
                // Если передана группа из избранного — выбираем её
                val initialGroup = groups.find { it.groupName == initialGroupName }
                selectedGroup = initialGroup ?: groups.first()
            }
        } catch (e: Exception) {
            error = "Ошибка загрузки списка групп: ${e.message}"
        } finally {
            loadingGroups = false
        }
    }

    // Подписка на изменения избранных групп
    LaunchedEffect(Unit) {
        repository.getFavorites().collect { favorites ->
            favoriteGroups = favorites
        }
    }

    LaunchedEffect(selectedGroup) {
        if (selectedGroup == null) return@LaunchedEffect

        loadingSchedule = true
        error = null

        try {
            val (start, end) = getWeekDateRange()
            schedule = repository.loadSchedule(
                groupName = selectedGroup!!.groupName,
                startDate = start,
                endDate = end
            )
        } catch (e: Exception) {
            error = "Ошибка загрузки расписания: ${e.message}"
            schedule = emptyList()
        } finally {
            loadingSchedule = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Выпадающий список групп
            if (loadingGroups) {
                CircularProgressIndicator()
            } else if (error != null) {
                Text("Ошибка: $error")
            } else {
                GroupDropdownWithFavorite(
                    groups = groups,
                    selectedGroup = selectedGroup,
                    onGroupSelected = { group ->
                        selectedGroup = group
                    },
                    favoriteGroups = favoriteGroups,
                    onFavoriteToggle = { groupName ->
                        scope.launch {
                            if (groupName in favoriteGroups) {
                                repository.removeFavorite(groupName)
                            } else {
                                repository.addFavorite(groupName)
                            }
                        }
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Отображение расписания или индикатора загрузки
            when {
                loadingSchedule -> {
                    CircularProgressIndicator(modifier = Modifier.padding(vertical = 32.dp))
                }
                error != null && schedule.isEmpty() -> {
                    Text("Ошибка загрузки расписания: $error")
                }
                else -> {
                    ScheduleList(schedule)
                }
            }
        }
    }
}