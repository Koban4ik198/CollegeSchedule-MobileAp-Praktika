package com.example.collegeschedule.data.repository

import com.example.collegeschedule.data.api.ScheduleApi
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.dto.GroupDto

class ScheduleRepository(private val api: ScheduleApi) {
    suspend fun loadSchedule(group: String): List<ScheduleByDateDto> {
        return api.getSchedule(
            groupName = group,
            start = "2026-01-12",
            end = "2026-01-17"
        )
    }

    suspend fun loadAllGroups(): List<GroupDto> {
        return api.getAllGroups()
    }
}