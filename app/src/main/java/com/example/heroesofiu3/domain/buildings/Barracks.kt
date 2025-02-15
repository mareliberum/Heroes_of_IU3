package com.example.heroesofiu3.domain.buildings

import com.example.heroesofiu3.domain.Units.Knight
import com.example.heroesofiu3.domain.gameField.Cell

class Barracks : Building("Barracks", 300) {
    override fun executeEffect(selectedCell: Cell) {
        val castle = selectedCell.castle!!
        val gold = castle.gold

        if (gold >= 400 && selectedCell.unit == null){
            selectedCell.buyUnit(Knight("Knight", isPlayer = castle.isPlayer ))
        }
    }
}