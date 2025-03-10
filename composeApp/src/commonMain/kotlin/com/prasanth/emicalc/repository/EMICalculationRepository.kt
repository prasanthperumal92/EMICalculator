package com.prasanth.emicalc.repository

import com.prasanth.emicalc.model.PeriodicPayment
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.plus
import kotlin.math.pow

class EMICalculationRepository {
    fun calculateEmi(principle:Double, interest: Double, months: Double):Double{
        return  (principle * interest * calculateDenom(interest,months))
    }

    private fun calculateDenom(interest:Double, months:Double): Double{
        val exp = (1 + interest).pow(months)
        return  exp / (exp-1)
    }

    fun calculateAmortization(balance: Double, interestPerMonth: Double, emi: Double):List<PeriodicPayment> {
        val schedule = ArrayList<PeriodicPayment>()
        var currentBalance = balance
        var index = 0
        currentAmortizationDate = Clock.System.now()
        while (currentBalance > emi){
            val interestPaid = currentBalance * interestPerMonth
            val principlePaid = emi - interestPaid
            val periodicPayment =
                PeriodicPayment(
                    getAmortizationDateMonth(),
                    currentBalance,
                    emi,
                    interestPaid,
                    principlePaid
                )
            schedule.add(periodicPayment)
            currentBalance -= principlePaid
            index++
        }
        schedule.add(PeriodicPayment(getAmortizationDateMonth(),currentBalance,currentBalance,0.0,0.0))
        return schedule
    }
    private var currentAmortizationDate = Clock.System.now()

    private fun getAmortizationDateMonth():String{
        val dateFormat = DateTimeComponents.Format {
            monthName(names = MonthNames.ENGLISH_ABBREVIATED)
            char('/')
            year()
        }
        val systemTZ = TimeZone.currentSystemDefault()
        currentAmortizationDate = currentAmortizationDate.plus(1, DateTimeUnit.MONTH, systemTZ)
        return currentAmortizationDate.format(format = dateFormat)
    }
}