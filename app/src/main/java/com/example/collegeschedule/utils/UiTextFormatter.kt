package com.example.collegeschedule.utils

import com.example.collegeschedule.data.dto.LessonGroupPart

object UiTextFormatter {

    /**
     * Преобразует enum LessonGroupPart в читаемый текст
     */
    fun formatGroupPart(part: LessonGroupPart): String {
        return when (part) {
            LessonGroupPart.FULL -> "Вся группа"
            LessonGroupPart.SUB1 -> "Подгруппа 1"
            LessonGroupPart.SUB2 -> "Подгруппа 2"
        }
    }

    /**
     * Форматирует дату из формата "2026-01-22" в "22 января, среда"
     */
    fun formatDateWithWeekday(isoDateTime: String, weekday: String): String {
        return try {
            // Обрезаем время, если оно есть
            val datePart = isoDateTime.split("T").first()
            val parts = datePart.split("-")

            if (parts.size == 3) {
                val day = parts[2].toInt()
                val month = when (parts[1].toInt()) {
                    1 -> "января"
                    2 -> "февраля"
                    3 -> "марта"
                    4 -> "апреля"
                    5 -> "мая"
                    6 -> "июня"
                    7 -> "июля"
                    8 -> "августа"
                    9 -> "сентября"
                    10 -> "октября"
                    11 -> "ноября"
                    12 -> "декабря"
                    else -> ""
                }
                "$day $month, $weekday"
            } else {
                "$isoDateTime, $weekday"
            }
        } catch (e: Exception) {
            "$isoDateTime, $weekday"
        }
    }

    /**
     * Форматирует время из "12:30-14:00" в "12:30 - 14:00"
     */
    fun formatTimeRange(time: String): String {
        return if (time.contains("-")) {
            time.replace("-", " - ")
        } else {
            time
        }
    }
}