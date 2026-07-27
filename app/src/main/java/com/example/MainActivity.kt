package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.SavingsGoalEntity
import com.example.ui.dialogs.AddSavingsGoalDialog
import com.example.ui.dialogs.AddTransactionDialog
import com.example.ui.dialogs.DepositWithdrawDialog
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.SavingsGoalsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TransactionsScreen
import com.example.ui.theme.GlassBackground
import com.example.ui.theme.GlassCyan
import com.example.ui.theme.GlassMoneyTheme
import com.example.ui.viewmodel.MoneyViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MoneyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            var showAddTxDialog by remember { mutableStateOf(false) }
            var showAddGoalDialog by remember { mutableStateOf(false) }
            var selectedGoalForDeposit by remember { mutableStateOf<SavingsGoalEntity?>(null) }
            var isDepositAction by remember { mutableStateOf(true) }

            GlassMoneyTheme(darkTheme = uiState.isDarkMode) {
                GlassBackground(isDark = uiState.isDarkMode) {
                    Scaffold(
                        containerColor = Color.Transparent,
                        bottomBar = {
                            GlassBottomNavBar(
                                activeTab = uiState.activeTab,
                                isDark = uiState.isDarkMode,
                                onTabSelect = { viewModel.setActiveTab(it) }
                            )
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (uiState.activeTab) {
                                0 -> DashboardScreen(
                                    state = uiState,
                                    onAddTransactionClick = { showAddTxDialog = true },
                                    onSearchChange = { viewModel.onSearchQueryChanged(it) },
                                    onDeleteTransaction = { viewModel.deleteTransaction(it) },
                                    onNavigateToTransactions = { viewModel.setActiveTab(1) }
                                )
                                1 -> TransactionsScreen(
                                    state = uiState,
                                    onSearchChange = { viewModel.onSearchQueryChanged(it) },
                                    onCategoryFilterChange = { viewModel.onCategoryFilterChanged(it) },
                                    onTypeFilterChange = { viewModel.onTypeFilterChanged(it) },
                                    onPeriodFilterChange = { viewModel.onTimePeriodChanged(it) },
                                    onAddTransactionClick = { showAddTxDialog = true },
                                    onDeleteTransaction = { viewModel.deleteTransaction(it) }
                                )
                                2 -> AnalyticsScreen(
                                    state = uiState
                                )
                                3 -> SavingsGoalsScreen(
                                    state = uiState,
                                    onAddGoalClick = { showAddGoalDialog = true },
                                    onDepositClick = { goal ->
                                        selectedGoalForDeposit = goal
                                        isDepositAction = true
                                    },
                                    onWithdrawClick = { goal ->
                                        selectedGoalForDeposit = goal
                                        isDepositAction = false
                                    },
                                    onDeleteGoal = { viewModel.deleteGoal(it) }
                                )
                                4 -> SettingsScreen(
                                    state = uiState,
                                    onToggleDarkMode = { viewModel.toggleDarkMode() },
                                    onSetCurrency = { viewModel.setCurrency(it) },
                                    onClearData = { viewModel.clearAllData() },
                                    onResetSeedData = { viewModel.resetSeedData() }
                                )
                            }
                        }
                    }

                    // Dialogs
                    if (showAddTxDialog) {
                        AddTransactionDialog(
                            isDark = uiState.isDarkMode,
                            currencySymbol = uiState.currencySymbol,
                            onDismiss = { showAddTxDialog = false },
                            onConfirm = { title, amount, type, category, note ->
                                viewModel.addTransaction(title, amount, type, category, note)
                            }
                        )
                    }

                    if (showAddGoalDialog) {
                        AddSavingsGoalDialog(
                            isDark = uiState.isDarkMode,
                            currencySymbol = uiState.currencySymbol,
                            onDismiss = { showAddGoalDialog = false },
                            onConfirm = { title, target, initial, icon, colorHex ->
                                viewModel.addSavingsGoal(title, target, initial, icon, colorHex)
                            }
                        )
                    }

                    val goalToDeposit = selectedGoalForDeposit
                    if (goalToDeposit != null) {
                        DepositWithdrawDialog(
                            goal = goalToDeposit,
                            isDeposit = isDepositAction,
                            isDark = uiState.isDarkMode,
                            currencySymbol = uiState.currencySymbol,
                            onDismiss = { selectedGoalForDeposit = null },
                            onConfirm = { deltaAmount ->
                                viewModel.updateGoalAmount(goalToDeposit, deltaAmount)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GlassBottomNavBar(
    activeTab: Int,
    isDark: Boolean,
    onTabSelect: (Int) -> Unit
) {
    val navBg = if (isDark) Color(0xFF0F172A).copy(alpha = 0.85f) else Color(0xFFFFFFFF).copy(alpha = 0.85f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = navBg,
            border = BorderStroke(
                1.dp,
                Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = if (isDark) 0.35f else 0.8f),
                        Color.White.copy(alpha = 0.10f)
                    )
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(28.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavItem(
                    label = "Home",
                    icon = Icons.Default.Home,
                    isSelected = activeTab == 0,
                    onClick = { onTabSelect(0) },
                    testTag = "nav_home"
                )
                NavItem(
                    label = "History",
                    icon = Icons.Default.ReceiptLong,
                    isSelected = activeTab == 1,
                    onClick = { onTabSelect(1) },
                    testTag = "nav_transactions"
                )
                NavItem(
                    label = "Analytics",
                    icon = Icons.Default.Analytics,
                    isSelected = activeTab == 2,
                    onClick = { onTabSelect(2) },
                    testTag = "nav_analytics"
                )
                NavItem(
                    label = "Goals",
                    icon = Icons.Default.Savings,
                    isSelected = activeTab == 3,
                    onClick = { onTabSelect(3) },
                    testTag = "nav_goals"
                )
                NavItem(
                    label = "Settings",
                    icon = Icons.Default.Settings,
                    isSelected = activeTab == 4,
                    onClick = { onTabSelect(4) },
                    testTag = "nav_settings"
                )
            }
        }
    }
}

@Composable
fun NavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val contentColor = if (isSelected) GlassCyan else Color.Gray

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(if (isSelected) GlassCyan.copy(alpha = 0.15f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(22.dp)
            )
            AnimatedVisibility(visible = isSelected) {
                Text(
                    text = label,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = GlassCyan
                )
            }
        }
    }
}
