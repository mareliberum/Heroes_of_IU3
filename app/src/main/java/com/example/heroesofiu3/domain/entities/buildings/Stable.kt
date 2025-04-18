package com.example.heroesofiu3.domain.entities.buildings

import com.example.heroesofiu3.domain.entities.gameField.Cell

class Stable : Building("Stable", 400) {
    override fun executeEffect(selectedCell: Cell) {
        val unit = selectedCell.unit
        if(unit != null){
            unit.maxDistance += 1
        }
    }
}