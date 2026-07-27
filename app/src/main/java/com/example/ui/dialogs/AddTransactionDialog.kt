package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.TransactionType
import com.example.ui.theme.GlassButton
import com.example.ui.theme.GlassCyan
import com.example.ui.theme.GlassEmerald
import com.example.ui.theme.GlassRose

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    isDark: Boolean,
    currencySymbol: String,
    onDismiss: () -> Unit,
    onConfirm: (title: String, amount: Double, type: TransactionType, category: String, note: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(TransactionType.EXPENSE) }
    var selectedCategory by remember { mutableStateOf("Food") }
    var note by remember { mutableStateOf("") }

    val expenseCategories = listOf("Food", "Coffee", "Housing", "Utilities", "Transport", "Tech", "Health", "Shopping", "Entertainment", "Other")
    val incomeCategories = listOf("Salary", "Freelance", "Investment", "Bonus", "Gift", "Other")

    val currentCategories = if (type == TransactionType.EXPENSE) expenseCategories else incomeCategories

    val dialogBg = if (isDark) Color(0xFF0F172A).copy(alpha = 0.95f) else Color(0xFFF8FAFC).copy(alpha = 0.95f)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = dialogBg,
            border = androidx.compose.foundation.BorderStroke(
                1.5.dp,
                Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = if (isDark) 0.4f else 0.8f),
                        Color.Transparent
                    )
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("add_transaction_dialog")
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "New Transaction",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Type Toggle (Expense / Income)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isDark) Color(0xFF1E293B) else Color(0xFFE2E8F0))
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (type == TransactionType.EXPENSE) GlassRose.copy(alpha = 0.85f) else Color.Transparent)
                            .clickable {
                                type = TransactionType.EXPENSE
                                if (!expenseCategories.contains(selectedCategory)) {
                                    selectedCategory = expenseCategories.first()
                                }
                            }
                            .padding(vertical = 10.dp)
                            .testTag("type_expense_tab"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Expense",
                            color = if (type == TransactionType.EXPENSE) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (type == TransactionType.INCOME) GlassEmerald.copy(alpha = 0.85f) else Color.Transparent)
                            .clickable {
                                type = TransactionType.INCOME
                                if (!incomeCategories.contains(selectedCategory)) {
                                    selectedCategory = incomeCategories.first()
                                }
                            }
                            .padding(vertical = 10.dp)
                            .testTag("type_income_tab"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Income",
                            color = if (type == TransactionType.INCOME) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title / Description") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GlassCyan,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("transaction_title_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Amount Input
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount ($currencySymbol)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GlassCyan,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("transaction_amount_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Category Chips
                Text(
                    text = "Category",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(currentCategories) { category ->
                        val isSel = selectedCategory == category
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSel) GlassCyan else if (isDark) Color(0xFF1E293B) else Color(0xFFE2E8F0)
                                )
                                .border(
                                    1.dp,
                                    if (isSel) Color.White else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedCategory = category }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = category,
                                color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontSize = 12.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Note Input
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note (Optional)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GlassCyan,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("transaction_note_input")
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Save Button
                val parsedAmount = amountText.toDoubleOrNull() ?: 0.0
                val isValid = title.isNotBlank() && parsedAmount > 0.0

                GlassButton(
                    text = "Add Transaction",
                    onClick = {
                        if (isValid) {
                            onConfirm(title.trim(), parsedAmount, type, selectedCategory, note.trim())
                            onDismiss()
                        }
                    },
                    accentColor = if (type == TransactionType.EXPENSE) GlassRose else GlassEmerald,
                    enabled = isValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_transaction_button")
                )
            }
        }
    }
}
