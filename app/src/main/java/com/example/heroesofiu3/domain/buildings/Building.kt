package com.example.heroesofiu3.domain.buildings

import com.example.heroesofiu3.domain.gameField.Cell

abstract class Building(
    val name : String,
    val cost : Int
) {
    abstract fun executeEffect(selectedCell: Cell)

}