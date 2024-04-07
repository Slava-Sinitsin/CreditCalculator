package com.example.creditcalc.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditcalc.data.mappers.Payment
import com.example.creditcalc.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import kotlin.math.pow

@HiltViewModel
class MainScreenViewModel @Inject constructor(val repository: Repository) : ViewModel() {
    var sum by mutableStateOf(Pair("100000", true))
        private set
    var rate by mutableStateOf(Pair("5.00", true))
        private set
    var period by mutableStateOf(Pair("3", true))
        private set
    var periodTypeList by mutableStateOf(listOf("Year", "Month"))
        private set
    var selectedPeriodTypeIndex by mutableIntStateOf(0)
        private set
    var issueDate by mutableStateOf(Date())
        private set
    var repaymentTypeList by mutableStateOf(listOf("Annuity", "Differentiated"))
        private set
    var selectedRepaymentTypeIndex by mutableIntStateOf(0)
        private set
    var repaymentFrequencyList by mutableStateOf(
        listOf(
            Pair("Monthly", true),
            Pair("Quarterly", true),
            Pair("Yearly", true)
        )
    )
        private set
    var selectedRepaymentFrequencyIndex by mutableIntStateOf(0)
        private set
    var calcError by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {

        }
    }

    private fun isSumCorrect(input: String): Boolean {
        if (input.isEmpty()) {
            return true
        }
        return input.all { it in '0'..'9' }
                && input.isNotEmpty()
                && input.toIntOrNull() != null
                && input.toInt() >= 0
                && input.toLong() <= 100000000
                && input[0] != '0'
    }

    private fun isRateCorrect(input: String): Boolean {
        if (input.isEmpty()) {
            return true
        }
        try {
            val doubleValue = input.toDouble()
            if (doubleValue !in 0.0..100.0) {
                return false
            }
            val decimalPart = input
                .substringAfter('.', "")
                .trimStart('0')
            return decimalPart.length <= 3
        } catch (e: NumberFormatException) {
            return false
        }
    }

    private fun isPeriodCorrect(input: String): Boolean {
        if (calcError) {
            return false
        }
        if (input.isEmpty()) {
            return true
        }
        try {
            val doubleValue = input.toDouble()
            if ((selectedPeriodTypeIndex == 0 && doubleValue !in 0.0..50.0) ||
                (selectedPeriodTypeIndex == 1 && doubleValue !in 0.0..600.0)
            ) {
                return false
            }
        } catch (e: NumberFormatException) {
            return false
        }
        return input.all { it in '0'..'9' }
                && input.isNotEmpty()
                && input.toIntOrNull() != null
                && input[0] != '0'
                && (selectedPeriodTypeIndex == 0 && input.toInt() > 0 && input.toLong() <= 50)
                || (selectedPeriodTypeIndex == 1 && input.toInt() > 0 && input.toLong() <= 600)
    }


    fun updateSum(newSum: String) {
        sum = Pair(newSum, isSumCorrect(newSum))
    }

    fun updateRate(newRate: String) {
        rate = Pair(newRate, isRateCorrect(newRate))
    }

    fun updatePeriod(newPeriod: String) {
        calcError = false
        period = Pair(newPeriod, isPeriodCorrect(newPeriod))
        checkPeriod()
    }

    fun updatePeriodTypeIndex(index: Int) {
        selectedPeriodTypeIndex = index
        period = period.copy(second = isPeriodCorrect(period.first))
        checkPeriod()
    }

    private fun checkPeriod() {
        if (period.second && period.first.isNotEmpty()) {
            if (selectedPeriodTypeIndex == 1) {
                if (period.first.toInt() < 3) {
                    repaymentFrequencyList =
                        repaymentFrequencyList.mapIndexed { currentIndex, item ->
                            if (currentIndex != 0) {
                                item.copy(second = false)
                            } else {
                                item
                            }
                        }
                } else if (period.first.toInt() < 12) {
                    repaymentFrequencyList =
                        repaymentFrequencyList.mapIndexed { currentIndex, item ->
                            if (currentIndex == 2) {
                                item.copy(second = false)
                            } else {
                                item
                            }
                        }
                }
            } else {
                repaymentFrequencyList = repaymentFrequencyList.map { item ->
                    item.copy(second = true)
                }
            }
        }
        if (!repaymentFrequencyList[selectedRepaymentFrequencyIndex].second) {
            selectedRepaymentFrequencyIndex = 0
        }
    }

    fun updateDate(newIssueDate: Date) {
        issueDate = newIssueDate
    }

    fun updateRepaymentTypeIndex(newRepaymentTypeIndex: Int) {
        selectedRepaymentTypeIndex = newRepaymentTypeIndex
    }

    fun updateRepaymentFrequencyIndex(newRepaymentFrequencyIndex: Int) {
        selectedRepaymentFrequencyIndex = newRepaymentFrequencyIndex
    }

    private fun isAllFieldsFilled(): Boolean {
        var flag = true
        if (sum.first.isEmpty()) {
            sum = sum.copy(second = false)
            flag = false
        }
        if (rate.first.isEmpty()) {
            rate = rate.copy(second = false)
            flag = false
        }
        if (period.first.isEmpty()) {
            period = period.copy(second = false)
            flag = false
        }
        return flag
    }

    fun onCalculateButtonClick() {
        if (isAllFieldsFilled()) {
            Log.e(
                "Check",
                sum.first.toDouble().toString() + ", " +
                        rate.first.toDouble().toString() + ", " +
                        period.first.toInt().toString() + ", " +
                        periodTypeList[selectedPeriodTypeIndex] + ", " +
                        issueDate.toString() + ", " +
                        repaymentTypeList[selectedRepaymentTypeIndex] + ", " +
                        repaymentFrequencyList[selectedRepaymentFrequencyIndex]
            )
            calculatePayments(
                sum = sum.first.toDouble(),
                rate = rate.first.toDouble(),
                period = period.first.toDouble(),
                periodType = periodTypeList[selectedPeriodTypeIndex],
                issueDate = issueDate,
                repaymentType = repaymentTypeList[selectedRepaymentTypeIndex],
                repaymentFrequency = repaymentFrequencyList[selectedRepaymentFrequencyIndex].first
            ).forEachIndexed { index, item -> Log.e("calculatePayments", "${index + 1}: $item") }
        } else {
            calcError = true
        }
    }

    fun calculatePayments(
        sum: Double,
        rate: Double,
        period: Double,
        periodType: String,
        issueDate: Date,
        repaymentType: String,
        repaymentFrequency: String
    ): List<Payment> {
        val payments = mutableListOf<Payment>()
        var remainingBalance = sum
        var currentDate = issueDate

        val periodsInAYear = when (periodType) {
            "Year" -> 1
            "Month" -> 12
            else -> throw IllegalArgumentException("Invalid period type")
        }

        val repaymentPeriods = when (repaymentFrequency) {
            "Monthly" -> periodsInAYear / 12
            "Quarterly" -> periodsInAYear / 4
            "Yearly" -> 1
            else -> throw IllegalArgumentException("Invalid repayment frequency")
        }

        val monthlyRate = rate / (periodsInAYear * 100)

        var paymentAmount = 0.0
        var principalPaid = 0.0
        var interestPaid = 0.0

        var totalPayments = 0

        if (repaymentType == "Annuity") {
            paymentAmount =
                sum * (monthlyRate + monthlyRate / ((1 + monthlyRate).pow(period * repaymentPeriods) - 1))
        }

        for (i in 0 until (period.toInt() * repaymentPeriods)) {
            val daysInMonth = Calendar.getInstance()
            daysInMonth.time = currentDate
            val days = daysInMonth.getActualMaximum(Calendar.DAY_OF_MONTH)

            val monthlyInterest = remainingBalance * monthlyRate
            interestPaid += monthlyInterest

            if (repaymentType == "Annuity") {
                principalPaid = paymentAmount - monthlyInterest
            } else if (repaymentType == "Differentiated") {
                principalPaid = sum / (period * repaymentPeriods)
            }

            if (principalPaid > remainingBalance) {
                principalPaid = remainingBalance
            }

            val payment = Payment(
                currentDate,
                principalPaid + monthlyInterest,
                principalPaid,
                monthlyInterest,
                remainingBalance - principalPaid
            )

            payments.add(payment)

            remainingBalance -= principalPaid

            totalPayments++

            if (remainingBalance <= 0) {
                break
            }

            if ((i + 1) % repaymentPeriods == 0) {
                currentDate = addMonths(currentDate, 1)
            }
        }

        // Adjust last payment's balance to 0
        if (payments.isNotEmpty()) {
            val lastPayment = payments.last()
            payments[payments.size - 1] = lastPayment.copy(balance = 0.0)
        }

        return payments
    }

    fun addMonths(date: Date, months: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MONTH, months)
        return calendar.time
    }
}