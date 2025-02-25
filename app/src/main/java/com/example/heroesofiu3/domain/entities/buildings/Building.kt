package com.example.heroesofiu3.domain.entities.buildings

import com.example.heroesofiu3.domain.entities.gameField.Cell

abstract class Building(
    val name : String,
    val cost : Int,
    val costOfUse : Int = 0,
) {
    abstract fun executeEffect(selectedCell: Cell)

}