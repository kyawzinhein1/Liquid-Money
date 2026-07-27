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
import com.example.ui.theme.GlassButton
import com.example.ui.theme.GlassCyan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSavingsGoalDialog(
    isDark: Boolean,
    currencySymbol: String,
    onDismiss: () -> Unit,
    onConfirm: (title: String, targetAmount: Double, initialAmount: Double, icon: String, colorHex: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var targetText by remember { mutableStateOf("") }
    var initialText by remember { mutableStateOf("0") }
    var selectedIcon by remember { mutableStateOf("flight") }
    var selectedColorHex by remember { mutableStateOf("#38BDF8") }

    val iconList = listOf("flight", "shield", "laptop", "car", "house", "education", "gadget", "star")
    val colorList = listOf("#38BDF8", "#34D399", "#A78BFA", "#FB7185", "#FBBF24", "#EC4899")

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
                .testTag("add_savings_goal_dialog")
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
                        text = "New Savings Goal",
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

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Goal Title (e.g. Vacation Trip)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GlassCyan,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("goal_title_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = targetText,
                    onValueChange = { targetText = it },
                    label = { Text("Target Amount ($currencySymbol)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GlassCyan,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("goal_target_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = initialText,
                    onValueChange = { initialText = it },
                    label = { Text("Initial Deposit ($currencySymbol)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GlassCyan,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("goal_initial_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Accent Color Selector
                Text(
                    text = "Theme Color",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(colorList) { colorHex ->
                        val color = try {
                            Color(android.graphics.Color.parseColor(colorHex))
                        } catch (e: Exception) {
                            GlassCyan
                        }
                        val isSel = selectedColorHex == colorHex
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(color)
                                .border(
                                    if (isSel) 2.5.dp else 0.dp,
                                    Color.White,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedColorHex = colorHex }
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                val parsedTarget = targetText.toDoubleOrNull() ?: 0.0
                val parsedInitial = initialText.toDoubleOrNull() ?: 0.0
                val isValid = title.isNotBlank() && parsedTarget > 0.0

                GlassButton(
                    text = "Create Goal",
                    onClick = {
                        if (isValid) {
                            onConfirm(title.trim(), parsedTarget, parsedInitial, selectedIcon, selectedColorHex)
                            onDismiss()
                        }
                    },
                    accentColor = GlassCyan,
                    enabled = isValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_goal_button")
                )
            }
        }
    }
}
