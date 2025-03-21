package com.example.heroesofiu3.data

import com.example.heroesofiu3.domain.entities.Units.Unit
import com.example.heroesofiu3.domain.entities.buildings.Castle
import com.example.heroesofiu3.domain.entities.gameField.Terrain

data class CellData(
	val x: Int,
	val y: Int,
	val terrain: Terrain,
	val unit: Unit?,
	val castle: Castle?
)