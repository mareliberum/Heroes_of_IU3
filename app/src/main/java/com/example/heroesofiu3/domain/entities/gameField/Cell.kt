package com.example.heroesofiu3.domain.entities.gameField

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.heroesofiu3.data.DataEntities.CastleData
import com.example.heroesofiu3.data.DataEntities.CellData
import com.example.heroesofiu3.data.DataEntities.UnitData
import com.example.heroesofiu3.domain.entities.Units.Archer
import com.example.heroesofiu3.domain.entities.Units.Hero
import com.example.heroesofiu3.domain.entities.Units.Knight
import com.example.heroesofiu3.domain.entities.Units.Unit
import com.example.heroesofiu3.domain.entities.buildings.Barracks
import com.example.heroesofiu3.domain.entities.buildings.Castle
import com.example.heroesofiu3.domain.entities.buildings.Fort
import com.example.heroesofiu3.domain.entities.buildings.Stable
import com.example.heroesofiu3.domain.entities.buildings.Tavern

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


    // Преобразование в CellData
    fun toCellData(): CellData {
        return CellData(x, y, terrain, unit?.toData(), castle?.toData())      // TODO castle.toData
    }

    // Восстановление из CellData
    fun updateFromCellData(cellData: CellData) {
        terrain = cellData.terrain
        unit = cellData.unit?.toUnit()
        castle = cellData.castle?.toCastle()
    }

    private fun Unit.toData(): UnitData {
        return UnitData(
            type = this::class.simpleName!!, // Сохраняем тип юнита
            name = this.name,
            isPlayer = this.isPlayer,
            health = this.health,
            strength = this.strength,
            hasMoved = this.hasMoved,
            hasAttacked = this.hasAttacked,
        )
    }

    private fun UnitData.toUnit(): Unit {
        val unit = when (type) {
            "Hero" -> Hero(name, isPlayer, health, strength)
            "Knight" -> Knight(name, isPlayer, health, strength)
            "Archer" -> Archer(name, isPlayer, health, strength)
            else -> throw IllegalArgumentException("Unknown unit type: $type")
        }
        unit.hasMoved = this.hasMoved
        unit.hasAttacked = this.hasAttacked
        return unit
    }

    private fun Castle.toData(): CastleData {
        return CastleData(
            name = this.name,
            isPlayer = this.isPlayer,
            health = this.health,
            strength = this.strength,
            buildings = this.buildings.map { it::class.simpleName!! },
            gold = this.gold
        )
    }

    private fun CastleData.toCastle(): Castle {
        val loadedCastle = Castle(
            name = this.name,
            isPlayer = this.isPlayer,
            health = this.health,
            strength = this.strength,
        )
        for (buildingName in this.buildings){
            when(buildingName){
                "Barracks" -> loadedCastle.addBuiding(Barracks())
                "Stable" -> loadedCastle.addBuiding(Stable())
                "Tavern" -> loadedCastle.addBuiding(Tavern())
                "Fort" -> loadedCastle.addBuiding(Fort())
            }
        }
        loadedCastle.addGold(this.gold)
        return loadedCastle
    }


}
