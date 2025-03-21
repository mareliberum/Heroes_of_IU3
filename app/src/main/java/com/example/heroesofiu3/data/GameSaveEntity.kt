package com.example.heroesofiu3.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameSave(
	@PrimaryKey(autoGenerate = true)
	val id : Int,
	val name: String, // Имя сохранения
	val gameFieldJson: String // JSON-строка GameField
)