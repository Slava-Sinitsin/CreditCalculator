package com.example.creditcalc.data.maindb

import androidx.room.TypeConverter
import com.example.creditcalc.data.mappers.Payment
import com.google.gson.Gson

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromPayment(coordinates: Payment?): String {
        return gson.toJson(coordinates)
    }

    @TypeConverter
    fun toPayment(coordinatesString: String?): Payment {
        return gson.fromJson(coordinatesString, Payment::class.java)
    }
}