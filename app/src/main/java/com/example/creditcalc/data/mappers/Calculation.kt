package com.example.creditcalc.data.mappers

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Calculation(
    @PrimaryKey(autoGenerate = true) val primaryKey: Int? = null,
    val payment: Payment? = null
)