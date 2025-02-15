package com.example.heroesofiu3.domain.buildings

import com.example.heroesofiu3.domain.Units.Hero
import com.example.heroesofiu3.domain.gameField.Cell

class Tavern : Building("Tavern", 500)  {
    override fun executeEffect(selectedCell: Cell) {
        val castle = selectedCell.castle!!
        val gold = castle.gold

        if (gold >= 400 && selectedCell.unit == null){
            selectedCell.buyUnit(Hero("Harry", true))
        }
    }
}

