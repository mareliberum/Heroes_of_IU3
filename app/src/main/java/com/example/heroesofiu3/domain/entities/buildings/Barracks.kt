package com.example.heroesofiu3.domain.entities.buildings

import com.example.heroesofiu3.domain.entities.Units.Knight
import com.example.heroesofiu3.domain.entities.gameField.Cell

class Barracks : Building("Barracks", 300, 400) {


    override fun executeEffect(selectedCell: Cell) {
        val castle = selectedCell.castle!!
        val gold = castle.gold

        if (gold >= costOfUse && selectedCell.unit == null){
            selectedCell.buyUnit(Knight("Knight", isPlayer = castle.isPlayer ))
        }
    }
}