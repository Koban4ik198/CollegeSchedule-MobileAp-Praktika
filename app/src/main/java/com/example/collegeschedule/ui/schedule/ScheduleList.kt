package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.utils.UiTextFormatter

@Composable
fun ScheduleList(data: List<ScheduleByDateDto>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(data) { day ->
            DayCard(day = day)
        }
    }
}

@Composable
fun DayCard(day: ScheduleByDateDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // #fff
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Заголовок дня
            Text(
                text = UiTextFormatter.formatDateWithWeekday(day.lessonDate, day.weekday),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00A2FF), // #00a2ff - основной акцент
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (day.lessons.isEmpty()) {
                Text(
                    text = "Занятий нет",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF888888), // Серый для неактивного текста
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                day.lessons.forEach { lesson ->
                    LessonCard(lesson = lesson)
                }
            }
        }
    }
}

@Composable
fun LessonCard(lesson: com.example.collegeschedule.data.dto.LessonDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F9FF) // Очень светлый голубой фон
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Время пары
            Text(
                text = "Пара ${lesson.lessonNumber} • ${UiTextFormatter.formatTimeRange(lesson.time)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF3385FF), // #3385ff - для заголовков
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Подгруппы
            lesson.groupParts.forEach { (part, info) ->
                if (info != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White // #fff
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            // Название подгруппы
                            Text(
                                text = UiTextFormatter.formatGroupPart(part),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF33AAFF), // #3af = #33aaff - для подзаголовков
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            // Предмет
                            Text(
                                text = "📚 ${info.subject}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF3385FF), // #3385ff
                                modifier = Modifier.padding(bottom = 2.dp)
                            )

                            // Преподаватель
                            Text(
                                text = "👨‍🏫 ${info.teacher} • ${info.teacherPosition}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF3385FF), // #3385ff
                                modifier = Modifier.padding(bottom = 2.dp)
                            )

                            // Аудитория
                            Text(
                                text = "🏢 ${info.building}, ауд. ${info.classroom}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF3385FF), // #3385ff
                                modifier = Modifier.padding(bottom = 2.dp)
                            )

                            // Адрес
                            if (info.address.isNotEmpty()) {
                                Text(
                                    text = "📍 ${info.address}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF888888), // Серый
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}