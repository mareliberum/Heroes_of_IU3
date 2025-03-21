package com.example.heroesofiu3.data.DataEntities

data class UnitData(
	val type: String, // Тип юнита (например, "Knight", "Archer")
	val name: String,
	val isPlayer: Boolean,
	val health: Int,
	val strength: Int,
	val hasMoved: Boolean,
	val hasAttacked : Boolean
)