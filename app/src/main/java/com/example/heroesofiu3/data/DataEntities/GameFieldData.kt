package com.example.heroesofiu3.data.DataEntities

data class GameFieldData(
	val width: Int,
	val height: Int,
	val cells: List<List<CellData>>,
)
