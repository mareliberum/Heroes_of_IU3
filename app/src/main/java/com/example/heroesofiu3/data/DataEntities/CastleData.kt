package com.example.heroesofiu3.data.DataEntities

data class CastleData(
	val name: String,
	val isPlayer: Boolean,
	val health: Int,
	val strength: Int,
	val buildings: List<String>,
	val gold : Int,
)


