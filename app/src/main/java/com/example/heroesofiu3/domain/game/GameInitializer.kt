package com.example.heroesofiu3.domain.game

import com.example.heroesofiu3.domain.entities.Units.Archer
import com.example.heroesofiu3.domain.entities.Units.Hero
import com.example.heroesofiu3.domain.entities.Units.Knight
import com.example.heroesofiu3.domain.entities.buildings.Barracks
import com.example.heroesofiu3.domain.entities.buildings.Castle
import com.example.heroesofiu3.domain.entities.buildings.Fort
import com.example.heroesofiu3.domain.entities.gameField.GameField
import com.example.heroesofiu3.domain.entities.gameField.Terrain

fun initializeField(field: GameField) {
    // Игрок:
    field.getCell(1, 0)?.unit = Hero("Hero", true)
    field.getCell(2, 1)?.unit = Knight("Knight", true)
    field.getCell(0, 0)?.unit = Archer("Archer", true)
    field.getCell(8, 1)?.unit = Knight("Mega Knight", true, health = 1000, strength = 1000, maxDistance = 100)
    field.getCell(7, 1)?.unit = Knight("Fast Knight", true, health = 1000, maxDistance = 100)
    field.getCell(1, 1)?.castle = Castle("Blue", true)
    field.getCell(1, 1)?.castle!!.addGold(1000)

    // Бот:
    field.getCell(9, 9)?.unit = Hero("Hero", false)
    field.getCell(8, 9)?.unit = Knight("Knight", false)
    field.getCell(8, 8)?.castle = Castle("Red", false)
    field.getCell(8, 8)?.castle!!.addBuiding(Barracks())
    field.getCell(8, 8)?.castle!!.addBuiding(Fort())
    field.getCell(8, 8)?.terrain = Terrain.UNREACHABLE


}