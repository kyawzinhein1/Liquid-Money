package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.ui.theme.GlassButton
import com.example.ui.theme.GlassCard
import com.example.ui.theme.GlassCyan
import com.example.ui.theme.GlassEmerald
import com.example.ui.theme.GlassRose
import com.example.ui.theme.GlassViolet
import com.example.ui.viewmodel.MoneyUiState

@Composable
fun SettingsScreen(
    state: MoneyUiState,
    onToggleDarkMode: () -> Unit,
    onSetCurrency: (String) -> Unit,
    onClearData: () -> Unit,
    onResetSeedData: () -> Unit
) {
    val isDark = state.isDarkMode
    val currencies = listOf("$", "€", "£", "¥", "₹")

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
                        text = "Preferences & Data",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = GlassCyan,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Settings",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(GlassCyan.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = GlassCyan,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Dark Mode Toggle Glass Card
        item {
            GlassCard(isDark = isDark, modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(GlassViolet.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                                contentDescription = "Dark mode",
                                tint = GlassViolet,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Liquid Crystal Dark Theme",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isDark) "Obsidian Dark Glass" else "Frosted Light Glass",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Switch(
                        checked = isDark,
                        onCheckedChange = { onToggleDarkMode() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = GlassCyan,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.testTag("dark_mode_switch")
                    )
                }
            }
        }

        // Currency Selector Card
        item {
            GlassCard(isDark = isDark, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Preferred Currency",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Select default symbol for all displays",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(currencies) { sym ->
                        val isSel = state.currencySymbol == sym
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isSel) GlassCyan else if (isDark) Color(0xFF1E293B) else Color(0xFFE2E8F0)
                                )
                                .clickable { onSetCurrency(sym) }
                                .padding(horizontal = 18.dp, vertical = 10.dp)
                                .testTag("currency_$sym")
                        ) {
                            Text(
                                text = sym,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Local Database File Management Card
        item {
            GlassCard(isDark = isDark, modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Storage,
                        contentDescription = "Database",
                        tint = GlassEmerald,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Local Room File Database",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "All financial records and savings goals are stored locally in SQLite / Room file database on device.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GlassButton(
                        text = "Reload Demo Data",
                        onClick = onResetSeedData,
                        accentColor = GlassEmerald,
                        icon = Icons.Default.Refresh,
                        isDark = isDark,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("reset_demo_data_button")
                    )

                    GlassButton(
                        text = "Clear All Data",
                        onClick = onClearData,
                        accentColor = GlassRose,
                        icon = Icons.Default.Delete,
                        isDark = isDark,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("clear_data_button")
                    )
                }
            }
        }

        // GitHub Actions CI/CD Info Card
        item {
            GlassCard(isDark = isDark, modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = "GitHub",
                        tint = GlassCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "GitHub Action & APK Build",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Workflow File: .github/workflows/build-apk.yml\nPushing to GitHub automatically triggers a Github Action that compiles and exports the release APK artifact.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}
