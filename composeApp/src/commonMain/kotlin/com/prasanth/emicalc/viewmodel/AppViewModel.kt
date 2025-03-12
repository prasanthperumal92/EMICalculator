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
import kotlin.math.roundToLong
import kotlin.reflect.KClass

@Stable
class AppViewModel(emiCalculationRepository: EMICalculationRepository = EMICalculationRepository()):ViewModel() {
    private val _principle:MutableState<String> = mutableStateOf((""))
    val principle:State<String> = _principle

    private val _interest:MutableState<String> = mutableStateOf("")
    val interest:State<String> = _interest

    private val _years:MutableState<String> = mutableStateOf("")
    val years:State<String> = _years

    private val _isMonth:MutableState<Boolean> = mutableStateOf(false)
    val isMonth:State<Boolean> = _isMonth

    private val months:State<Double> = derivedStateOf {
        if (years.value.isEmpty()) return@derivedStateOf 0.0
        if (isMonth.value) years.value.toDouble() else years.value.toDouble() * 12
    }

    val total = derivedStateOf { emi.value.toDouble() * months.value }
    val interestPayed = derivedStateOf { total.value - principle.value.replace(",","").ifEmpty { "0" }.toDouble() }

    val emi:State<String> = derivedStateOf {
        if (principle.value.isEmpty() || interest.value.isEmpty() || years.value.isEmpty())
            return@derivedStateOf "0"
        val principleAmnt =  principle.value.replace(",","").toDouble()
        val interestAmnt = interest.value.toDouble() / 12 / 100
        val emi = emiCalculationRepository.calculateEmi(principleAmnt, interestAmnt, months.value)
        emi.roundToLong().toString()
    }

    val amortizationSchedule:State<List<PeriodicPayment>> = derivedStateOf {
        if (principle.value.isEmpty() || interest.value.isEmpty() || years.value.isEmpty())
            return@derivedStateOf emptyList()
        val principleAmnt =  principle.value.replace(",","").toDouble()
        val interestAmnt = interest.value.toDouble() / 12 / 100
        emiCalculationRepository.calculateAmortization(principleAmnt,interestAmnt,emi.value.toDouble())
    }
    private fun String.isDigits():Boolean{
        return toDoubleOrNull() != null || isNullOrEmpty()
    }

    fun setPrinciple(value:String){
        val principle = value.replace(",","")
        if (principle.isDigits().not()) return
        _principle.value = formatNumber(principle)
    }
    fun setInterest(value: String){
        if (value.isDigits().not()) return
        _interest.value = value
    }
    fun setYears(value: String){
        if (value.isDigits().not()) return
        _years.value = value
    }
    fun setIsMonth(value:Boolean){
        _isMonth.value = value
    }

    fun toggleIsMonth(){
        _isMonth.value = isMonth.value.not()
    }

     private fun formatNumber(value:String): String{
        if (value.contains(".")) return value
        val pca = value.reversed().toMutableList().map { it.toString() }.toMutableList()
        var index = 1
        while(index <= pca.size)
        {
            if (index%2 != 0 && index!=pca.size && index!=1) {
                pca[index-1] = ","+pca[index-1]
            }
            index++
        }
        return pca.reversed().joinToString("")
    }


    companion object Factory:ViewModelProvider.Factory{
        override fun <T:  ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
            return AppViewModel() as T
        }
    }
}
