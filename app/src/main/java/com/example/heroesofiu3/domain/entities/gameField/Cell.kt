package com.example.heroesofiu3.domain.entities.gameField

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.heroesofiu3.domain.entities.Units.Unit
import com.example.heroesofiu3.domain.entities.buildings.Castle

class Cell(
    val x: Int,
    val y: Int,
    val terrain: Terrain = Terrain.ROAD
) {
    var unit by mutableStateOf<Unit?>(null)
    var castle by mutableStateOf<Castle?>(null)

    fun buyUnit(unitToBuy: Unit) {
        castle?.spendMoney(400)
        unit = unitToBuy
    }

    fun boostUnit() {
        if (unit != null && unit?.isBoosted == false) {
            unit!!.maxDistance += 1
            unit!!.isBoosted = true
        }
    }




}
