package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GlassAmber
import com.example.ui.theme.GlassCard
import com.example.ui.theme.GlassCyan
import com.example.ui.theme.GlassEmerald
import com.example.ui.theme.GlassProgressBar
import com.example.ui.theme.GlassRose
import com.example.ui.theme.GlassViolet
import com.example.ui.viewmodel.MoneyUiState

@Composable
fun AnalyticsScreen(
    state: MoneyUiState
) {
    val isDark = state.isDarkMode
    val currency = state.currencySymbol
    val categoryExpenses = state.categoryExpenses
    val totalExpense = state.totalExpense

    val categoryColors = listOf(
        GlassCyan, GlassRose, GlassEmerald, GlassViolet, GlassAmber,
        Color(0xFFEC4899), Color(0xFF14B8A6), Color(0xFFF97316)
    )

    val sortedCategories = categoryExpenses.entries.sortedByDescending { it.value }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Spending Habits",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = GlassCyan,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Visual Analytics",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(GlassViolet.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Analytics,
                        contentDescription = "Analytics",
                        tint = GlassViolet,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Donut Chart Glass Card
        item {
            GlassCard(
                isDark = isDark,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("spending_donut_card")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.PieChart,
                        contentDescription = null,
                        tint = GlassCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Expense Breakdown",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (totalExpense <= 0) {
                    Text(
                        text = "No expense data recorded yet to render charts.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(170.dp)) {
                            var startAngle = -90f
                            val strokeWidth = 36.dp.toPx()
                            val chartSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                            val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)

                            sortedCategories.forEachIndexed { index, entry ->
                                val sweepAngle = ((entry.value / totalExpense) * 360f).toFloat()
                                val color = categoryColors[index % categoryColors.size]

                                drawArc(
                                    color = color,
                                    startAngle = startAngle,
                                    sweepAngle = sweepAngle - 2f, // 2deg gap between segments
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = chartSize,
                                    style = Stroke(width = strokeWidth)
                                )
                                startAngle += sweepAngle
                            }
                        }

                        // Center Total Text
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Spent",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "$currency${String.format("%.0f", totalExpense)}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Key Spending Insights Card
        item {
            GlassCard(isDark = isDark, modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = "Insight",
                        tint = GlassEmerald,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Smart Insights",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                val topCat = sortedCategories.firstOrNull()
                if (topCat != null && totalExpense > 0) {
                    val pct = (topCat.value / totalExpense) * 100
                    Text(
                        text = "• Highest spending is on ${topCat.key} ($currency${String.format("%.2f", topCat.value)}), accounting for ${String.format("%.1f", pct)}% of total expenses.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Text(
                        text = "• Add expenses to generate automated smart financial insights.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Detailed Category Breakdown Header
        item {
            Text(
                text = "Category Details",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Category Rows
        items(sortedCategories) { entry ->
            val index = sortedCategories.indexOf(entry)
            val color = categoryColors[index % categoryColors.size]
            val pct = if (totalExpense > 0) (entry.value / totalExpense).toFloat() else 0f

            GlassCard(
                isDark = isDark,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = entry.key,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = "$currency${String.format("%.2f", entry.value)} (${String.format("%.0f", pct * 100)}%)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                GlassProgressBar(
                    progress = pct,
                    fillColor = color,
                    isDark = isDark,
                    height = 8.dp
                )
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}
