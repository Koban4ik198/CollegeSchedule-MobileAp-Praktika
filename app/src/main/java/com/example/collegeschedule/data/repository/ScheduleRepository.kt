package com.example.collegeschedule.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.collegeschedule.data.api.ScheduleApi
import com.example.collegeschedule.data.dto.GroupDto
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "favorites")

class ScheduleRepository(
    private val api: ScheduleApi,
    private val context: Context
) {
    private val dataStore = context.dataStore
    private val favoritesKey = stringSetPreferencesKey("favorite_groups")

    // Сетевые методы
    suspend fun loadSchedule(
        groupName: String,
        startDate: String,
        endDate: String
    ): List<ScheduleByDateDto> {
        return api.getSchedule(
            groupName = groupName,
            start = startDate,
            end = endDate
        )
    }

    suspend fun loadAllGroups(): List<GroupDto> {
        return api.getAllGroups()
    }

    // Избранные группы
    suspend fun addFavorite(groupName: String) {
        dataStore.edit { preferences ->
            val currentFavorites = preferences[favoritesKey] ?: emptySet()
            preferences[favoritesKey] = currentFavorites + groupName
        }
    }

    suspend fun removeFavorite(groupName: String) {
        dataStore.edit { preferences ->
            val currentFavorites = preferences[favoritesKey] ?: emptySet()
            preferences[favoritesKey] = currentFavorites - groupName
        }
    }

    fun getFavorites(): Flow<List<String>> {
        return dataStore.data.map { preferences ->
            (preferences[favoritesKey] ?: emptySet()).toList()
        }
    }

    suspend fun clearFavorites() {
        dataStore.edit { preferences ->
            preferences.remove(favoritesKey)
        }
    }
}