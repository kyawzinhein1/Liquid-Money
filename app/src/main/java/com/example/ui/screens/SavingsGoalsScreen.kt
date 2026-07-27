package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SavingsGoalEntity
import com.example.ui.theme.GlassButton
import com.example.ui.theme.GlassCard
import com.example.ui.theme.GlassCyan
import com.example.ui.theme.GlassEmerald
import com.example.ui.theme.GlassProgressBar
import com.example.ui.theme.GlassRose
import com.example.ui.theme.GlassViolet
import com.example.ui.viewmodel.MoneyUiState

@Composable
fun SavingsGoalsScreen(
    state: MoneyUiState,
    onAddGoalClick: () -> Unit,
    onDepositClick: (SavingsGoalEntity) -> Unit,
    onWithdrawClick: (SavingsGoalEntity) -> Unit,
    onDeleteGoal: (SavingsGoalEntity) -> Unit
) {
    val isDark = state.isDarkMode
    val currency = state.currencySymbol
    val goals = state.savingsGoals

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddGoalClick,
                containerColor = GlassCyan,
                contentColor = Color.White,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .padding(bottom = 70.dp)
                    .testTag("fab_add_savings_goal")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Goal")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                            text = "Target Savings",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = GlassCyan,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Savings Goals",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(GlassEmerald.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Savings,
                            contentDescription = "Savings",
                            tint = GlassEmerald,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            if (goals.isEmpty()) {
                item {
                    GlassCard(isDark = isDark, modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No savings goals set",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tap the + button to create your first goal",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(goals, key = { it.id }) { goal ->
                    SavingsGoalCard(
                        goal = goal,
                        currency = currency,
                        isDark = isDark,
                        onDeposit = { onDepositClick(goal) },
                        onWithdraw = { onWithdrawClick(goal) },
                        onDelete = { onDeleteGoal(goal) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(90.dp)) }
        }
    }
}

@Composable
fun SavingsGoalCard(
    goal: SavingsGoalEntity,
    currency: String,
    isDark: Boolean,
    onDeposit: () -> Unit,
    onWithdraw: () -> Unit,
    onDelete: () -> Unit
) {
    val progress = if (goal.targetAmount > 0) (goal.currentAmount / goal.targetAmount).toFloat() else 0f
    val percentage = (progress * 100).coerceAtMost(100f)

    val goalColor = try {
        Color(android.graphics.Color.parseColor(goal.colorHex))
    } catch (e: Exception) {
        GlassCyan
    }

    val iconVector = when (goal.categoryIcon.lowercase()) {
        "flight" -> Icons.Default.Flight
        "shield" -> Icons.Default.Shield
        "laptop" -> Icons.Default.Laptop
        else -> Icons.Default.Star
    }

    GlassCard(
        isDark = isDark,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("savings_goal_${goal.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(goalColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = goal.title,
                        tint = goalColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = goal.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "$currency${String.format("%.2f", goal.currentAmount)} of $currency${String.format("%.2f", goal.targetAmount)}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${String.format("%.0f", percentage)}%",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (percentage >= 100f) GlassEmerald else goalColor
                )

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.testTag("delete_goal_${goal.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Goal",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        GlassProgressBar(
            progress = progress,
            fillColor = goalColor,
            isDark = isDark,
            height = 10.dp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Action Buttons: + Deposit / - Withdraw
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            GlassButton(
                text = "+ Deposit",
                onClick = onDeposit,
                accentColor = GlassEmerald,
                isDark = isDark,
                modifier = Modifier
                    .weight(1f)
                    .testTag("deposit_btn_${goal.id}")
            )

            GlassButton(
                text = "- Withdraw",
                onClick = onWithdraw,
                accentColor = GlassRose,
                isDark = isDark,
                modifier = Modifier
                    .weight(1f)
                    .testTag("withdraw_btn_${goal.id}")
            )
        }
    }
}
