package com.example.heroesofiu3.domain.entities

import com.example.heroesofiu3.domain.gameField.Cell
import com.example.heroesofiu3.domain.gameField.Terrain

class GameField(val width: Int, val height: Int) {

    private val cells: List<List<Cell>> = List(width) { x ->
        List(height) { y ->
            Cell(
                x, y,
                terrain = if (x == y) Terrain.ROAD
                else if (x <= 4 && y <= 4) Terrain.FRIENDLY
                else if (x >= 5 && y >= 5) Terrain.ENEMY
                else Terrain.OBSTACLE
            )
        }
    }

    fun getCell(x: Int, y: Int): Cell? {
        return cells.getOrNull(x)?.getOrNull(y)
    }

    fun getCellList(): List<Cell> {
        val cellList = mutableListOf<Cell>()
        for(list in cells){
            for (cell in list){
                cellList.add(cell)
            }
        }
        return cellList
    }
}