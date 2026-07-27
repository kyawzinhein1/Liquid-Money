package com.example.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.SavingsGoalEntity
import com.example.ui.theme.GlassButton
import com.example.ui.theme.GlassCyan
import com.example.ui.theme.GlassEmerald
import com.example.ui.theme.GlassRose

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepositWithdrawDialog(
    goal: SavingsGoalEntity,
    isDeposit: Boolean,
    isDark: Boolean,
    currencySymbol: String,
    onDismiss: () -> Unit,
    onConfirm: (amount: Double) -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    val titleText = if (isDeposit) "Add Deposit to ${goal.title}" else "Withdraw from ${goal.title}"
    val actionColor = if (isDeposit) GlassEmerald else GlassRose

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
                .testTag("deposit_withdraw_dialog")
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
                        text = titleText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Current saved: $currencySymbol${String.format("%.2f", goal.currentAmount)} / $currencySymbol${String.format("%.2f", goal.targetAmount)}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount ($currencySymbol)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = actionColor,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("deposit_amount_input")
                )

                Spacer(modifier = Modifier.height(20.dp))

                val parsedAmount = amountText.toDoubleOrNull() ?: 0.0
                val isValid = parsedAmount > 0.0

                GlassButton(
                    text = if (isDeposit) "Confirm Deposit" else "Confirm Withdrawal",
                    onClick = {
                        if (isValid) {
                            val signedAmount = if (isDeposit) parsedAmount else -parsedAmount
                            onConfirm(signedAmount)
                            onDismiss()
                        }
                    },
                    accentColor = actionColor,
                    enabled = isValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("confirm_deposit_button")
                )
            }
        }
    }
}
