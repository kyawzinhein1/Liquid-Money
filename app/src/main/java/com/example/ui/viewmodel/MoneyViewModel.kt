package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.BudgetEntity
import com.example.data.local.SavingsGoalEntity
import com.example.data.local.TransactionEntity
import com.example.data.local.TransactionType
import com.example.data.repository.MoneyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

data class MoneyUiState(
    val transactions: List<TransactionEntity> = emptyList(),
    val filteredTransactions: List<TransactionEntity> = emptyList(),
    val savingsGoals: List<SavingsGoalEntity> = emptyList(),
    val budgets: List<BudgetEntity> = emptyList(),
    val searchQuery: String = "",
    val categoryFilter: String = "All",
    val typeFilter: TransactionType? = null,
    val timePeriod: String = "This Month", // "All", "This Month", "Last Month"
    val isDarkMode: Boolean = true,
    val currencySymbol: String = "$",
    val activeTab: Int = 0,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val netBalance: Double = 0.0,
    val categoryExpenses: Map<String, Double> = emptyMap(),
    val monthlyLimit: Double = 3500.0
)

private data class FilterState(
    val searchQuery: String,
    val categoryFilter: String,
    val typeFilter: TransactionType?,
    val timePeriod: String,
    val isDarkMode: Boolean
)

class MoneyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MoneyRepository

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _categoryFilter = MutableStateFlow("All")
    val categoryFilter = _categoryFilter.asStateFlow()

    private val _typeFilter = MutableStateFlow<TransactionType?>(null)
    val typeFilter = _typeFilter.asStateFlow()

    private val _timePeriod = MutableStateFlow("This Month")
    val timePeriod = _timePeriod.asStateFlow()

    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode = _isDarkMode.asStateFlow()

    private val _currencySymbol = MutableStateFlow("$")
    val currencySymbol = _currencySymbol.asStateFlow()

    private val _activeTab = MutableStateFlow(0)
    val activeTab = _activeTab.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = MoneyRepository(
            database.transactionDao(),
            database.savingsGoalDao(),
            database.budgetDao()
        )

        // Seed data if database is empty
        viewModelScope.launch {
            val existingTx = repository.allTransactions.first()
            if (existingTx.isEmpty()) {
                seedInitialData()
            }
        }
    }

    private val dbDataFlow = combine(
        repository.allTransactions,
        repository.allGoals,
        repository.allBudgets
    ) { txs, goals, budgets ->
        Triple(txs, goals, budgets)
    }

    private val filterStateFlow = combine(
        _searchQuery,
        _categoryFilter,
        _typeFilter,
        _timePeriod,
        _isDarkMode
    ) { search, cat, type, period, dark ->
        FilterState(search, cat, type, period, dark)
    }

    private val prefStateFlow = combine(
        _currencySymbol,
        _activeTab
    ) { currency, tab ->
        Pair(currency, tab)
    }

    val uiState: StateFlow<MoneyUiState> = combine(
        dbDataFlow,
        filterStateFlow,
        prefStateFlow
    ) { (txList, goalsList, budgetList), filter, (currency, tab) ->

        val search = filter.searchQuery
        val category = filter.categoryFilter
        val type = filter.typeFilter
        val period = filter.timePeriod
        val dark = filter.isDarkMode

        // Calculate totals
        var incomeSum = 0.0
        var expenseSum = 0.0
        val categoryMap = mutableMapOf<String, Double>()

        val now = Calendar.getInstance()
        val currentMonth = now.get(Calendar.MONTH)
        val currentYear = now.get(Calendar.YEAR)

        val filtered = txList.filter { tx ->
            val matchesSearch = tx.title.contains(search, ignoreCase = true) ||
                    tx.category.contains(search, ignoreCase = true) ||
                    tx.note.contains(search, ignoreCase = true)

            val matchesCategory = (category == "All" || tx.category.equals(category, ignoreCase = true))
            val matchesType = (type == null || tx.type == type)

            val cal = Calendar.getInstance().apply { timeInMillis = tx.dateMillis }
            val txMonth = cal.get(Calendar.MONTH)
            val txYear = cal.get(Calendar.YEAR)

            val matchesPeriod = when (period) {
                "This Month" -> (txMonth == currentMonth && txYear == currentYear)
                "Last Month" -> {
                    val lastMonth = if (currentMonth == 0) 11 else currentMonth - 1
                    val lastMonthYear = if (currentMonth == 0) currentYear - 1 else currentYear
                    (txMonth == lastMonth && txYear == lastMonthYear)
                }
                else -> true
            }

            matchesSearch && matchesCategory && matchesType && matchesPeriod
        }

        txList.forEach { tx ->
            if (tx.type == TransactionType.INCOME) {
                incomeSum += tx.amount
            } else {
                expenseSum += tx.amount
                val currentCategoryTotal = categoryMap.getOrDefault(tx.category, 0.0)
                categoryMap[tx.category] = currentCategoryTotal + tx.amount
            }
        }

        MoneyUiState(
            transactions = txList,
            filteredTransactions = filtered,
            savingsGoals = goalsList,
            budgets = budgetList,
            searchQuery = search,
            categoryFilter = category,
            typeFilter = type,
            timePeriod = period,
            isDarkMode = dark,
            currencySymbol = currency,
            activeTab = tab,
            totalIncome = incomeSum,
            totalExpense = expenseSum,
            netBalance = incomeSum - expenseSum,
            categoryExpenses = categoryMap
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MoneyUiState()
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onCategoryFilterChanged(category: String) {
        _categoryFilter.value = category
    }

    fun onTypeFilterChanged(type: TransactionType?) {
        _typeFilter.value = type
    }

    fun onTimePeriodChanged(period: String) {
        _timePeriod.value = period
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun setCurrency(symbol: String) {
        _currencySymbol.value = symbol
    }

    fun setActiveTab(tab: Int) {
        _activeTab.value = tab
    }

    fun addTransaction(title: String, amount: Double, type: TransactionType, category: String, note: String) {
        viewModelScope.launch {
            val tx = TransactionEntity(
                title = title,
                amount = amount,
                type = type,
                category = category,
                dateMillis = System.currentTimeMillis(),
                note = note
            )
            repository.insertTransaction(tx)
        }
    }

    fun deleteTransaction(tx: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(tx)
        }
    }

    fun addSavingsGoal(title: String, targetAmount: Double, currentAmount: Double, categoryIcon: String, colorHex: String) {
        viewModelScope.launch {
            val goal = SavingsGoalEntity(
                title = title,
                targetAmount = targetAmount,
                currentAmount = currentAmount,
                targetDateMillis = System.currentTimeMillis() + (90L * 24 * 60 * 60 * 1000), // 90 days
                categoryIcon = categoryIcon,
                colorHex = colorHex
            )
            repository.insertGoal(goal)
        }
    }

    fun updateGoalAmount(goal: SavingsGoalEntity, deltaAmount: Double) {
        viewModelScope.launch {
            val newAmount = (goal.currentAmount + deltaAmount).coerceAtLeast(0.0)
            repository.updateGoal(goal.copy(currentAmount = newAmount))
        }
    }

    fun deleteGoal(goal: SavingsGoalEntity) {
        viewModelScope.launch {
            repository.deleteGoal(goal)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllData()
        }
    }

    fun resetSeedData() {
        viewModelScope.launch {
            repository.clearAllData()
            seedInitialData()
        }
    }

    private suspend fun seedInitialData() {
        val now = System.currentTimeMillis()
        val day = 86400000L

        // Transactions
        val sampleTx = listOf(
            TransactionEntity(title = "Monthly Salary", amount = 4200.0, type = TransactionType.INCOME, category = "Salary", dateMillis = now - (2 * day), note = "Main tech job salary"),
            TransactionEntity(title = "Freelance UI Design", amount = 850.0, type = TransactionType.INCOME, category = "Freelance", dateMillis = now - (5 * day), note = "Glassmorphic dashboard contract"),
            TransactionEntity(title = "Whole Foods Market", amount = 142.50, type = TransactionType.EXPENSE, category = "Food", dateMillis = now - (1 * day), note = "Weekly organic groceries"),
            TransactionEntity(title = "Artisan Coffee Roasters", amount = 18.20, type = TransactionType.EXPENSE, category = "Coffee", dateMillis = now - (3 * day), note = "Espresso & pastries"),
            TransactionEntity(title = "Apartment Rent", amount = 1600.0, type = TransactionType.EXPENSE, category = "Housing", dateMillis = now - (10 * day), note = "Downtown studio rent"),
            TransactionEntity(title = "Electric & Water Bill", amount = 98.40, type = TransactionType.EXPENSE, category = "Utilities", dateMillis = now - (8 * day), note = "Monthly utility charges"),
            TransactionEntity(title = "Uber Rides", amount = 45.60, type = TransactionType.EXPENSE, category = "Transport", dateMillis = now - (4 * day), note = "Airport commute"),
            TransactionEntity(title = "Apple Store - Watch", amount = 399.0, type = TransactionType.EXPENSE, category = "Tech", dateMillis = now - (6 * day), note = "New smartwatch"),
            TransactionEntity(title = "Gym Membership", amount = 65.0, type = TransactionType.EXPENSE, category = "Health", dateMillis = now - (12 * day), note = "Fitness studio subscription")
        )

        sampleTx.forEach { repository.insertTransaction(it) }

        // Savings Goals
        val sampleGoals = listOf(
            SavingsGoalEntity(title = "Tokyo Summer Trip", targetAmount = 3500.0, currentAmount = 2150.0, targetDateMillis = now + (120 * day), categoryIcon = "flight", colorHex = "#38BDF8"),
            SavingsGoalEntity(title = "Emergency Reserve", targetAmount = 6000.0, currentAmount = 4800.0, targetDateMillis = now + (180 * day), categoryIcon = "shield", colorHex = "#34D399"),
            SavingsGoalEntity(title = "MacBook Pro M3", targetAmount = 2400.0, currentAmount = 1350.0, targetDateMillis = now + (60 * day), categoryIcon = "laptop", colorHex = "#A78BFA")
        )

        sampleGoals.forEach { repository.insertGoal(it) }

        // Budget
        repository.insertBudget(BudgetEntity(category = "Overall", monthlyLimit = 3500.0))
    }
}
