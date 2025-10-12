package com.example.unilocal.viewmodel.schedule

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import com.example.unilocal.R
import com.example.unilocal.model.Schedule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * ViewModel responsible for managing weekly schedules for places.
 *
 * Responsibilities:
 * - Validates, adds, removes, and formats schedules.
 * - Provides reactive state for the UI.
 * - Uses localized string resources for user messages.
 */
@SuppressLint("StaticFieldLeak")
@RequiresApi(Build.VERSION_CODES.O)
class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext

    // -------------------------------------------------------------------------
    // 🔹 STATE MANAGEMENT
    // -------------------------------------------------------------------------
    private val _schedules = MutableStateFlow<List<Schedule>>(emptyList())
    val schedules: StateFlow<List<Schedule>> = _schedules

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    // -------------------------------------------------------------------------
    // 🔹 DAYS MAP AND UTILITIES
    // -------------------------------------------------------------------------
    private val dayToNum = mapOf(
        context.getString(R.string.day_monday) to 1,
        context.getString(R.string.day_tuesday) to 2,
        context.getString(R.string.day_wednesday) to 3,
        context.getString(R.string.day_thursday) to 4,
        context.getString(R.string.day_friday) to 5,
        context.getString(R.string.day_saturday) to 6,
        context.getString(R.string.day_sunday) to 7
    )

    private val reverseDays = dayToNum.entries.associate { (k, v) -> v to k }

    private fun convertTo24(hour: Int, period: String): Int =
        when (period) {
            context.getString(R.string.period_am) -> if (hour == 12) 0 else hour
            context.getString(R.string.period_pm) -> if (hour == 12) 12 else hour + 12
            else -> hour
        }

    // -------------------------------------------------------------------------
    // 🔹 CORE LOGIC
    // -------------------------------------------------------------------------

    /**
     * Adds a new schedule after validating input values.
     */
    fun addSchedule(
        startDay: String,
        endDay: String,
        openHour: String,
        openMinute: String,
        openPeriod: String,
        closeHour: String,
        closeMinute: String,
        closePeriod: String
    ): Unit {
        if (startDay.isBlank() || endDay.isBlank()) {
            _message.value = context.getString(R.string.msg_field_required)
            return
        }

        val startNum = dayToNum[startDay]
        val endNum = dayToNum[endDay]

        if (startNum == null || endNum == null) {
            _message.value = context.getString(R.string.msg_schedule_invalid_days)
            return
        }

        if (startNum > endNum) {
            _message.value = context.getString(R.string.msg_schedule_invalid_days)
            return
        }

        val open24 = convertTo24(openHour.toInt(), openPeriod)
        val close24 = convertTo24(closeHour.toInt(), closePeriod)
        val startTime = LocalTime.of(open24, openMinute.toInt())
        val endTime = LocalTime.of(close24, closeMinute.toInt())

        if (startTime >= endTime) {
            _message.value = context.getString(R.string.msg_schedule_invalid_hours)
            return
        }

        val overlap = _schedules.value.any { existing ->
            !(endTime <= existing.start || startTime >= existing.end)
        }
        if (overlap) {
            _message.value = context.getString(R.string.msg_schedule_invalid_hours)
            return
        }

        val schedule = Schedule(
            dayStart = startNum,
            dayEnd = endNum,
            start = startTime,
            end = endTime
        )

        _schedules.update { it + schedule }
        _message.value = context.getString(R.string.msg_schedule_added)
    }

    /**
     * Removes a specific schedule from the list.
     */
    fun removeSchedule(schedule: Schedule): Unit {
        _schedules.update { it - schedule }
        _message.value = context.getString(R.string.msg_schedule_removed)
    }

    /**
     * Clears all stored schedules.
     */
    fun clearSchedules(): Unit {
        _schedules.value = emptyList()
        _message.value = context.getString(R.string.msg_schedule_removed)
    }

    /**
     * Clears the current message.
     */
    fun clearMessage(): Unit {
        _message.value = null
    }

    // -------------------------------------------------------------------------
    // 🔹 FORMAT UTILITIES FOR UI
    // -------------------------------------------------------------------------

    /**
     * Returns a human-readable schedule representation.
     * Example: "Lunes a Viernes | 🕒 08:00 - 17:00"
     */
    fun formatSchedule(schedule: Schedule): String {
        val startDay = reverseDays[schedule.dayStart] ?: "?"
        val endDay = reverseDays[schedule.dayEnd] ?: "?"
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return "$startDay a $endDay | 🕒 ${schedule.start.format(formatter)} - ${schedule.end.format(formatter)}"
    }
}
