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
 * Features:
 * - Validates, adds, removes, and formats schedules.
 * - Enforces logical constraints (max 2 schedules, no duplicates, no overlaps).
 * - Integrates a directed graph [ScheduleGraph] to ensure consistency.
 * - Provides reactive state via [StateFlow] for Compose UI.
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
    // 🔹 GRAPH MANAGEMENT
    // -------------------------------------------------------------------------
    private val graph = ScheduleGraph()
    private var nodeIdCounter = 0

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

    /**
     * Converts 12-hour format to 24-hour integer hour.
     */
    private fun convertTo24(hour: Int, period: String): Int =
        when (period) {
            context.getString(R.string.period_am) -> if (hour == 12) 0 else hour
            context.getString(R.string.period_pm) -> if (hour == 12) 12 else hour + 12
            else -> hour
        }

    /**
     * Returns a localized list of day names (Monday to Sunday).
     * Used by UI components such as ScheduleForm.
     */
    fun getLocalizedDays(): List<String> = dayToNum.keys.toList()

    // -------------------------------------------------------------------------
    // 🔹 CORE LOGIC
    // -------------------------------------------------------------------------

    /**
     * Adds a new schedule after validating:
     * - Non-empty days
     * - Valid start/end days
     * - Valid hour ranges
     * - Maximum of 2 schedules allowed
     * - No duplicates or overlaps
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
    ) {
        // --- Validate day selection ---
        if (startDay.isBlank() || endDay.isBlank()) {
            _message.value = context.getString(R.string.msg_field_required)
            return
        }

        val startNum = dayToNum[startDay]
        val endNum = dayToNum[endDay]

        if (startNum == null || endNum == null || startNum > endNum) {
            _message.value = context.getString(R.string.msg_schedule_invalid_days)
            return
        }

        // --- Convert to LocalTime ---
        val open24 = convertTo24(openHour.toInt(), openPeriod)
        val close24 = convertTo24(closeHour.toInt(), closePeriod)
        val startTime = LocalTime.of(open24, openMinute.toInt())
        val endTime = LocalTime.of(close24, closeMinute.toInt())

        if (startTime >= endTime) {
            _message.value = context.getString(R.string.msg_schedule_invalid_hours)
            return
        }

        // --- Create new schedule ---
        val newSchedule = Schedule(
            dayStart = startNum,
            dayEnd = endNum,
            start = startTime,
            end = endTime
        )

        // --- Enforce max 2 schedules ---
        if (_schedules.value.size >= 2) {
            _message.value = context.getString(R.string.msg_max_two_schedules)
            return
        }

        // --- Prevent duplicates ---
        if (_schedules.value.any { it == newSchedule }) {
            _message.value = context.getString(R.string.msg_duplicate_schedule)
            return
        }

        // --- Detect overlaps using graph ---
        if (graph.hasOverlap(newSchedule)) {
            _message.value = context.getString(R.string.msg_schedule_overlap)
            return
        }

        // --- Add schedule and update graph ---
        val node = ScheduleGraph.ScheduleNode(++nodeIdCounter, newSchedule)
        graph.addNode(node)
        graph.buildEdges()

        _schedules.update { it + newSchedule }
        _message.value = context.getString(R.string.msg_schedule_added)
    }

    /**
     * Removes a specific schedule from the list and rebuilds the graph.
     */
    fun removeSchedule(schedule: Schedule) {
        _schedules.update { it - schedule }
        rebuildGraph()
        _message.value = context.getString(R.string.msg_schedule_removed)
    }

    /**
     * Clears all stored schedules and resets the graph.
     */
    fun clearSchedules() {
        _schedules.value = emptyList()
        graph.clear()
        nodeIdCounter = 0
        _message.value = context.getString(R.string.msg_schedule_removed)
    }

    /**
     * Reconstructs the schedule graph after deletions.
     */
    private fun rebuildGraph() {
        graph.clear()
        nodeIdCounter = 0
        _schedules.value.forEach {
            val node = ScheduleGraph.ScheduleNode(++nodeIdCounter, it)
            graph.addNode(node)
        }
        graph.buildEdges()
    }

    /**
     * Clears the current message (to avoid repeated Toasts).
     */
    fun clearMessage() {
        _message.value = null
    }

    // -------------------------------------------------------------------------
    // 🔹 FORMAT UTILITIES FOR UI
    // -------------------------------------------------------------------------

    /**
     * Returns a human-readable schedule string.
     * Example: "Lunes a Viernes | 08:00 - 17:00"
     */
    fun formatSchedule(schedule: Schedule): String {
        val startDay = reverseDays[schedule.dayStart] ?: "?"
        val endDay = reverseDays[schedule.dayEnd] ?: "?"
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return "$startDay a $endDay | ${schedule.start.format(formatter)} - ${schedule.end.format(formatter)}"
    }
}
