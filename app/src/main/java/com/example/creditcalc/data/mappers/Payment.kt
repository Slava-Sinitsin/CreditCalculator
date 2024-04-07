package com.example.creditcalc.data.mappers

import java.util.Date

data class Payment(
    val date: Date,
    val payment: Double,
    val principal: Double,
    val interest: Double,
    val balance: Double
)