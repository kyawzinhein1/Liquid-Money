package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TransactionEntity
import com.example.data.local.TransactionType
import com.example.ui.theme.GlassCard
import com.example.ui.theme.GlassChip
import com.example.ui.theme.GlassCyan
import com.example.ui.theme.GlassEmerald
import com.example.ui.theme.GlassRose
import com.example.ui.viewmodel.MoneyUiState

@Composable
fun TransactionsScreen(
    state: MoneyUiState,
    onSearchChange: (String) -> Unit,
    onCategoryFilterChange: (String) -> Unit,
    onTypeFilterChange: (TransactionType?) -> Unit,
    onPeriodFilterChange: (String) -> Unit,
    onAddTransactionClick: () -> Unit,
    onDeleteTransaction: (TransactionEntity) -> Unit
) {
    val isDark = state.isDarkMode
    val categories = listOf("All", "Food", "Coffee", "Housing", "Utilities", "Transport", "Tech", "Health", "Salary", "Freelance")
    val periods = listOf("This Month", "Last Month", "All Time")

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTransactionClick,
                containerColor = GlassCyan,
                contentColor = Color.White,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .padding(bottom = 70.dp)
                    .testTag("fab_add_transaction")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                Text(
                    text = "Transaction History",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Search Bar
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
                        .testTag("transactions_search_input")
                )
            }

            // Type Filter Chips (All, Expense, Income)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GlassChip(
                        text = "All Types",
                        selected = state.typeFilter == null,
                        onClick = { onTypeFilterChange(null) },
                        isDark = isDark,
                        accentColor = GlassCyan,
                        modifier = Modifier.testTag("filter_type_all")
                    )
                    GlassChip(
                        text = "Expenses",
                        selected = state.typeFilter == TransactionType.EXPENSE,
                        onClick = { onTypeFilterChange(TransactionType.EXPENSE) },
                        isDark = isDark,
                        accentColor = GlassRose,
                        modifier = Modifier.testTag("filter_type_expense")
                    )
                    GlassChip(
                        text = "Income",
                        selected = state.typeFilter == TransactionType.INCOME,
                        onClick = { onTypeFilterChange(TransactionType.INCOME) },
                        isDark = isDark,
                        accentColor = GlassEmerald,
                        modifier = Modifier.testTag("filter_type_income")
                    )
                }
            }

            // Period Filters (This Month, Last Month, All Time)
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(periods) { period ->
                        GlassChip(
                            text = period,
                            selected = state.timePeriod == period,
                            onClick = { onPeriodFilterChange(period) },
                            isDark = isDark,
                            accentColor = GlassCyan,
                            icon = Icons.Default.FilterList
                        )
                    }
                }
            }

            // Category Horizontal Filters
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { cat ->
                        GlassChip(
                            text = cat,
                            selected = state.categoryFilter == cat,
                            onClick = { onCategoryFilterChange(cat) },
                            isDark = isDark,
                            accentColor = GlassCyan
                        )
                    }
                }
            }

            // Transaction Items List
            val itemsList = state.filteredTransactions
            if (itemsList.isEmpty()) {
                item {
                    GlassCard(isDark = isDark, modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No transactions found",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Try adjusting your filters or search terms",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(itemsList, key = { it.id }) { tx ->
                    TransactionItemCard(
                        tx = tx,
                        currency = state.currencySymbol,
                        isDark = isDark,
                        onDelete = { onDeleteTransaction(tx) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(90.dp)) }
        }
    }
}
