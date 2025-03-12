package com.prasanth.emicalc.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.prasanth.emicalc.model.PeriodicPayment
import com.prasanth.emicalc.repository.EMICalculationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.reflect.KClass

@Stable
class AppViewModel(val emiCalculationRepository: EMICalculationRepository = EMICalculationRepository()) :
    ViewModel() {
    private val _principle: MutableState<String> = mutableStateOf((""))
    val principle: State<String> = _principle
    private val _interest: MutableState<String> = mutableStateOf("0")
    val interest: State<String> = _interest
    private val _years: MutableState<String> = mutableStateOf("")
    val years: State<String> = _years
    private val _isMonth: MutableState<Boolean> = mutableStateOf(false)
    val isMonth: State<Boolean> = _isMonth
    private val errorText: MutableState<String> = mutableStateOf("")
    fun getErrorText(): State<String> = errorText

    private val months: State<Double> = derivedStateOf {
        if (years.value.isEmpty()) return@derivedStateOf 0.0
        if (isMonth.value) years.value.toDouble() else years.value.toDouble() * 12
    }

    val total = derivedStateOf { emi.value.toLong() * months.value }
    val interestPayed = derivedStateOf {
        val principle = principle.value.replace(",", "").ifEmpty { "0" }.toDouble()
        val total = total.value
        if (total > principle) {
            total - principle
        } else {
            0
        }
    }

    private fun String.isZeroOrEmpty(): Boolean {
        return isEmpty() || equals("0")
    }

    val emi: State<String> = derivedStateOf {
        errorText.value = ""
        if (validateEmi()) return@derivedStateOf "0"
        val principleAmnt = principle.value.replace(",", "").toDouble()
        val interestAmnt = interest.value.toDouble() / 12 / 100
        val emi = emiCalculationRepository.calculateEmi(principleAmnt, interestAmnt, months.value)
        emi.roundToLong().toString()
    }

    private fun validateEmi(): Boolean {
        if (principle.value.isZeroOrEmpty() || interest.value.isZeroOrEmpty() || years.value.isZeroOrEmpty())
            return true
        val months:Double = if (isMonth.value) years.value.toDouble() else years.value.toDouble() * 12
        if (months > 360) {
            errorText.value = "We currently do not support  more than 30 years"
            return true
        }
        return false
    }

    private val amortizationSchedule: MutableState<List<PeriodicPayment>> = mutableStateOf(emptyList())

    fun getAmortizationSchedule():State<List<PeriodicPayment>> = amortizationSchedule

    suspend fun calculateAmortisation() = withContext(Dispatchers.IO) {
        if (validateEmi()) {
            amortizationSchedule.value = emptyList()
            return@withContext
        }
        val emi= emi.value.toDouble()
        if (emi <= 0) {
            amortizationSchedule.value = emptyList()
            return@withContext
        }
        val principleAmnt = principle.value.replace(",", "").toDouble()
        var interestAmnt = interest.value.toDouble() / 12 / 100
        interestAmnt = interestAmnt.roundTo(7)
        amortizationSchedule.value = emiCalculationRepository.calculateAmortization(
            principleAmnt,
            interestAmnt,
            emi
        )
        return@withContext
    }
    private fun String.isDigits(): Boolean {
        return toDoubleOrNull() != null || isNullOrEmpty()
    }

    private fun Double.roundTo(numFractionDigits: Int): Double {
        val factor = 10.0.pow(numFractionDigits.toDouble())
        return (this * factor).roundToInt() / factor
    }

    fun setPrinciple(value: String) {
        val principle = value.replace(",", "")
        if (principle.isDigits().not()) return
        _principle.value = principle.formatNumber()
    }

    fun setInterest(value: String) {
        if (value.isDigits().not()) return
        _interest.value = value
    }

    fun setYears(value: String) {
        if (value.isDigits().not()) return
        _years.value = value
    }

    fun setIsMonth(value: Boolean) {
        if (value)
            _years.value = _years.value.toDouble().times(12).roundToLong().toString()
        else
            _years.value = _years.value.toDouble().div(12).roundTo(1).toString()

        _isMonth.value = value
    }

    fun toggleIsMonth() {
        setIsMonth(isMonth.value.not())
    }

    private  fun String.formatNumber(): String {
        if (contains(".")) return this
        val pca = reversed().toMutableList().map { it.toString() }.toMutableList()
        var index = 1
        while (index <= pca.size) {
            if (index % 2 != 0 && index != pca.size && index != 1) {
                pca[index - 1] = "," + pca[index - 1]
            }
            index++
        }
        return pca.reversed().joinToString("")
    }


    companion object Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
            return AppViewModel() as T
        }
    }
}
