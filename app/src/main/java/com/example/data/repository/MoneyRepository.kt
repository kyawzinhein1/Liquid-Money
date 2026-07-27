package com.example.data.repository

import com.example.data.local.BudgetDao
import com.example.data.local.BudgetEntity
import com.example.data.local.SavingsGoalDao
import com.example.data.local.SavingsGoalEntity
import com.example.data.local.TransactionDao
import com.example.data.local.TransactionEntity
import kotlinx.coroutines.flow.Flow

class MoneyRepository(
    private val transactionDao: TransactionDao,
    private val savingsGoalDao: SavingsGoalDao,
    private val budgetDao: BudgetDao
) {
    val allTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
    val allGoals: Flow<List<SavingsGoalEntity>> = savingsGoalDao.getAllGoals()
    val allBudgets: Flow<List<BudgetEntity>> = budgetDao.getAllBudgets()

    suspend fun insertTransaction(transaction: TransactionEntity): Long {
        return transactionDao.insertTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: TransactionEntity) {
        transactionDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.deleteTransaction(transaction)
    }

    suspend fun deleteTransactionById(id: Long) {
        transactionDao.deleteTransactionById(id)
    }

    suspend fun insertGoal(goal: SavingsGoalEntity): Long {
        return savingsGoalDao.insertGoal(goal)
    }

    suspend fun updateGoal(goal: SavingsGoalEntity) {
        savingsGoalDao.updateGoal(goal)
    }

    suspend fun deleteGoal(goal: SavingsGoalEntity) {
        savingsGoalDao.deleteGoal(goal)
    }

    suspend fun insertBudget(budget: BudgetEntity) {
        budgetDao.insertBudget(budget)
    }

    suspend fun clearAllData() {
        transactionDao.deleteAll()
        savingsGoalDao.deleteAll()
        budgetDao.deleteAll()
    }
}
