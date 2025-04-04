package com.example.heroesofiu3.data.DataEntities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecordSave(
	@PrimaryKey(autoGenerate = true)
	val id : Int,
	val name: String, // имя игрока
	val score : Int,  //очки
	val date : String,
)
