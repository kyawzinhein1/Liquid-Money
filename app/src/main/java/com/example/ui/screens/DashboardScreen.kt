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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TransactionEntity
import com.example.data.local.TransactionType
import com.example.ui.theme.GlassButton
import com.example.ui.theme.GlassCard
import com.example.ui.theme.GlassCyan
import com.example.ui.theme.GlassEmerald
import com.example.ui.theme.GlassProgressBar
import com.example.ui.theme.GlassRose
import com.example.ui.theme.GlassViolet
import com.example.ui.viewmodel.MoneyUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    state: MoneyUiState,
    onAddTransactionClick: () -> Unit,
    onSearchChange: (String) -> Unit,
    onDeleteTransaction: (TransactionEntity) -> Unit,
    onNavigateToTransactions: () -> Unit
) {
    val currency = state.currencySymbol
    val isDark = state.isDarkMode

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Top App Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Liquid Crystal Money",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = GlassCyan,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Financial Overview",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(GlassCyan, GlassViolet))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = "Wallet",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Hero Net Balance Glass Card
        item {
            GlassCard(
                isDark = isDark,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("net_balance_card")
            ) {
                Text(
                    text = "Total Net Balance",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$currency${String.format("%.2f", state.netBalance)}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (state.netBalance >= 0) GlassEmerald else GlassRose
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Income / Expense Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Income Box
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(GlassEmerald.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = "Income",
                                tint = GlassEmerald,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Income",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "$currency${String.format("%.2f", state.totalIncome)}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Expense Box
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(GlassRose.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = "Expense",
                                tint = GlassRose,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Expenses",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "$currency${String.format("%.2f", state.totalExpense)}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Quick Action Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GlassButton(
                    text = "Add Transaction",
                    onClick = onAddTransactionClick,
                    accentColor = GlassCyan,
                    icon = Icons.Default.Add,
                    isDark = isDark,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("add_transaction_quick_button")
                )
            }
        }

        // Monthly Budget Status Card
        item {
            val budgetLimit = state.monthlyLimit
            val usedExpense = state.totalExpense
            val budgetProgress = if (budgetLimit > 0) (usedExpense / budgetLimit).toFloat() else 0f

            GlassCard(
                isDark = isDark,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Monthly Spending Limit",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "$currency${String.format("%.0f", usedExpense)} / $currency${String.format("%.0f", budgetLimit)}",
                        fontSize = 12.sp,
                        color = if (budgetProgress > 0.9f) GlassRose else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                GlassProgressBar(
                    progress = budgetProgress,
                    fillColor = if (budgetProgress > 0.9f) GlassRose else GlassCyan,
                    isDark = isDark
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (budgetProgress > 1.0f) "Over budget by $currency${String.format("%.2f", usedExpense - budgetLimit)}"
                    else "${String.format("%.0f", (1.0f - budgetProgress) * 100)}% budget remaining",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Recent Transactions Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Activity",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                GlassButton(
                    text = "See All",
                    onClick = onNavigateToTransactions,
                    accentColor = GlassViolet,
                    isDark = isDark,
                    modifier = Modifier.testTag("see_all_transactions_button")
                )
            }
        }

        // Quick Search Field
        item {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text("Search transactions...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = GlassCyan)
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GlassCyan,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dashboard_search_input")
            )
        }

        // Recent Transactions Items (Top 5)
        val recentList = state.filteredTransactions.take(5)
        if (recentList.isEmpty()) {
            item {
                GlassCard(isDark = isDark, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "No transactions found",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        } else {
            items(recentList, key = { it.id }) { tx ->
                TransactionItemCard(
                    tx = tx,
                    currency = currency,
                    isDark = isDark,
                    onDelete = { onDeleteTransaction(tx) }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun TransactionItemCard(
    tx: TransactionEntity,
    currency: String,
    isDark: Boolean,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    val formattedDate = dateFormat.format(Date(tx.dateMillis))

    val isIncome = tx.type == TransactionType.INCOME
    val amountColor = if (isIncome) GlassEmerald else GlassRose
    val prefix = if (isIncome) "+" else "-"

    GlassCard(
        isDark = isDark,
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("transaction_item_${tx.id}")
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
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            if (isIncome) GlassEmerald.copy(alpha = 0.15f) else GlassRose.copy(alpha = 0.15f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isIncome) Icons.Default.ArrowUpward else Icons.Default.Category,
                        contentDescription = tx.category,
                        tint = amountColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = tx.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = tx.category,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = GlassCyan
                        )
                        Text(
                            text = " • $formattedDate",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$prefix$currency${String.format("%.2f", tx.amount)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = amountColor
                )

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.testTag("delete_tx_${tx.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
