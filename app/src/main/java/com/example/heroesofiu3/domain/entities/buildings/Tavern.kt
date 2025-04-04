package com.example.heroesofiu3.domain.entities.buildings

import com.example.heroesofiu3.domain.entities.Units.Hero
import com.example.heroesofiu3.domain.entities.gameField.Cell

class Tavern : Building("Tavern", 500, 400)  {
    override fun executeEffect(selectedCell: Cell) {
        val castle = selectedCell.castle!!
        val gold = castle.gold

        if (gold >= costOfUse && selectedCell.unit == null){
            selectedCell.buyUnit(Hero("Harry", true))
        }
    }
}

