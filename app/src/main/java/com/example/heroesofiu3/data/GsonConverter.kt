package com.example.heroesofiu3.data

import com.example.heroesofiu3.domain.entities.gameField.GameField
import com.google.gson.Gson

val gson = Gson()

// Сериализация
fun GameField.toJson(): String {
	val cellsData = this.cells.map { row -> row.map { it.toCellData() } }
	val gameFieldData = GameFieldData(this.width, this.height, cellsData)
	return gson.toJson(gameFieldData)
}


// Десериализация
fun String.toGameField(): GameField {
	val gameFieldData = gson.fromJson(this, GameFieldData::class.java)
	val gameField = GameField(gameFieldData.width, gameFieldData.height)
	gameFieldData.cells.forEachIndexed { x, row ->
		row.forEachIndexed { y, cellData ->
			gameField.getCell(x, y)?.updateFromCellData(cellData)
		}
	}
	return gameField
}