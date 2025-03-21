package com.example.heroesofiu3.data

data class GameFieldData(
	val width: Int,
	val height: Int,
	val cells: List<List<CellData>>
)