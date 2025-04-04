package com.example.heroesofiu3.data.DataEntities

import com.example.heroesofiu3.domain.entities.gameField.Terrain

data class CellData(
	val x: Int,
	val y: Int,
	val terrain: Terrain,
	val unit: UnitData?,
	val castle: CastleData?,
)
