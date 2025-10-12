package com.example.unilocal.viewmodel.schedule

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.unilocal.model.Schedule

/**
 * Represents a directed graph structure that models the relationship
 * between multiple [Schedule] entries.
 *
 * Each [Schedule] is represented as a node in the graph. Directed edges
 * between nodes indicate that one schedule ends before another begins
 * (A → B means A ends before B starts).
 *
 * This structure allows validation of:
 *  - Schedule overlaps (same or intersecting time ranges)
 *  - Logical sequencing between schedules
 *  - Maximum schedule constraints handled externally via ViewModel
 *
 * The graph is rebuilt dynamically whenever schedules are added or removed.
 */
class ScheduleGraph {

    // --- Inner node representation ---
    data class ScheduleNode(val id: Int, val schedule: Schedule)

    // --- Internal adjacency list ---
    private val adjacencyList = mutableMapOf<ScheduleNode, MutableList<ScheduleNode>>()

    /**
     * Adds a new schedule node to the graph if it doesn't already exist.
     */
    fun addNode(node: ScheduleNode) {
        adjacencyList.putIfAbsent(node, mutableListOf())
    }

    /**
     * Creates a directed edge from one schedule node to another,
     * representing that the first schedule ends before the second starts.
     */
    fun addEdge(from: ScheduleNode, to: ScheduleNode) {
        adjacencyList[from]?.add(to)
    }

    /**
     * Clears all nodes and edges from the graph.
     * Used when schedules are rebuilt or reset.
     */
    fun clear() {
        adjacencyList.clear()
    }

    /**
     * Reconstructs the directed edges between existing schedules.
     *
     * The rule applied:
     *  A → B  ⇢  if (A ends before or exactly when B starts)
     *
     * This allows topological ordering if needed for future expansions.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun buildEdges() {
        val nodes = adjacencyList.keys.toList()
        for (i in nodes.indices) {
            for (j in i + 1 until nodes.size) {
                val a = nodes[i]
                val b = nodes[j]
                if (a.schedule.end <= b.schedule.start) addEdge(a, b)
            }
        }
    }

    /**
     * Checks whether the provided [newSchedule] overlaps with any
     * existing schedule currently in the graph.
     *
     * @return `true` if any overlap is detected, `false` otherwise.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun hasOverlap(newSchedule: Schedule): Boolean {
        return adjacencyList.keys.any { existing ->
            schedulesOverlap(existing.schedule, newSchedule)
        }
    }

    /**
     * Determines whether two schedules overlap in both day and time ranges.
     *
     * Overlap conditions:
     *  - Their day ranges intersect (e.g., both include Wednesday)
     *  - Their time intervals intersect within those days
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun schedulesOverlap(a: Schedule, b: Schedule): Boolean {
        // If their day ranges don't overlap, they cannot conflict
        if (a.dayEnd < b.dayStart || b.dayEnd < a.dayStart) return false

        // Identify common day intersection
        val commonDays = (a.dayStart..a.dayEnd).intersect(b.dayStart..b.dayEnd)
        if (commonDays.isEmpty()) return false

        // Time overlap within any shared day
        return !(a.end <= b.start || b.end <= a.start)
    }

    /**
     * Returns a formatted string representation of the graph.
     * Useful for debugging or logging purposes.
     */
    override fun toString(): String {
        val builder = StringBuilder()
        for ((node, edges) in adjacencyList) {
            val edgesText = edges.joinToString(", ") { "Node(${it.id})" }
            builder.append("Node(${node.id}) → [$edgesText]\n")
        }
        return builder.toString()
    }
}
