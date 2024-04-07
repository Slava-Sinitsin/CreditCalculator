package com.example.creditcalc.data.maindb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.creditcalc.data.mappers.Calculation

@Database(
    entities = [Calculation::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class MainDB : RoomDatabase() {
    abstract val calculationDAO: CalculationDAO
}