package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savings_goals")
data class SavingsGoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val targetDateMillis: Long,
    val categoryIcon: String = "savings", // e.g., "car", "house", "flight", "gadget", "emergency"
    val colorHex: String = "#3B82F6"
)
