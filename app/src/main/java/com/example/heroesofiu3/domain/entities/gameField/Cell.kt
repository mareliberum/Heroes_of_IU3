package com.example.heroesofiu3.domain.entities.gameField

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.heroesofiu3.domain.entities.Units.Unit
import com.example.heroesofiu3.domain.entities.buildings.Castle


class Cell(
    val x: Int,
    val y: Int,
    terrain: Terrain = Terrain.ROAD,
) {
    var terrain by mutableStateOf(terrain)
    var unit by mutableStateOf<Unit?>(null)
    var castle by mutableStateOf<Castle?>(null)

    fun buyUnit(unitToBuy: Unit) {
        castle?.spendMoney(400)
        unit = unitToBuy
    }
}
