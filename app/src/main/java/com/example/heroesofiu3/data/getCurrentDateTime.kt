package com.example.heroesofiu3.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getCurrentDateTime(): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(Date())
}