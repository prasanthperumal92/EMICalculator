package com.prasanth.emicalc.model

data class PeriodicPayment(
    val date: String,
    val balance: Double,
    val emi: Double,
    val interestPaid: Double,
    val principlePaid: Double
)