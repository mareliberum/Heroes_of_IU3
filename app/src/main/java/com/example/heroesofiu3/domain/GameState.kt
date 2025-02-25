package com.example.heroesofiu3.domain

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.heroesofiu3.domain.entities.gameField.Cell
import com.example.heroesofiu3.domain.entities.gameField.GameField
import com.example.heroesofiu3.domain.entities.gameField.GameResult
import com.example.heroesofiu3.domain.entities.gameField.Terrain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt

class GameState(width: Int, height: Int) {
    val gameField = GameField(width, height)

    private var _selectedCell by mutableStateOf<Cell?>(null)
    val selectedCell: Cell? get() = _selectedCell

    private val _availableMoves = MutableStateFlow<List<Cell>>(emptyList())
    val availableMoves: StateFlow<List<Cell>> = _availableMoves

    private var _isGameOver by mutableStateOf("")
    val isGameOver: String get() = _isGameOver

    fun selectCell(cell: Cell, context: Context) {

        when (_selectedCell) {
            null -> {
                _selectedCell = cell
                getAvailableMoves(cell, getMaxDistanceFromSelectedCell())
            }

            cell -> {
                _selectedCell = null
                _availableMoves.value = emptyList()
            }

            else -> {
                val selectedUnit = _selectedCell!!.unit

                if (selectedUnit?.isPlayer == false) {
                    _selectedCell = null
                    _availableMoves.value = emptyList()
                    return
                }
                else if (canAttack(_selectedCell!!, cell)){
                    if (selectedUnit?.hasAttacked == true) {
                        Toast.makeText(context, "This unit has already attacked this turn.", Toast.LENGTH_SHORT).show()
                        return
                    }
                    attackUnit(_selectedCell!!, cell, context)
                    moveUnit(_selectedCell!!, cell, getMaxDistanceFromSelectedCell())
                    _selectedCell = null
                    _availableMoves.value = emptyList()
                    return
                }
                if (selectedUnit?.hasMoved == true) {
                    Toast.makeText(context, "This unit has already moved this turn.", Toast.LENGTH_SHORT).show()
                }
                moveUnit(_selectedCell!!, cell, getMaxDistanceFromSelectedCell())
                _selectedCell = null
                _availableMoves.value = emptyList()
            }
        }
    }

    fun makeBotMove(context: Context) {
        // Проверка завершения игры после хода игрока
        when (checkGameOver()) {
            GameResult.BotWins -> showGameOver(context, "Bot Wins!")
            else -> {}
        }
        resetUnitActions()
        val botUnits = gameField.getCellList().filter { it.unit != null && !it.unit!!.isPlayer }
        val playerUnits = gameField.getCellList().filter { it.unit != null && it.unit!!.isPlayer }

        val botCastleCell = findBotCastle()
        //Покупка юнитов ботом
        botCastleCell?.castle?.buildings?.get(0)?.executeEffect(botCastleCell)

        //  Копия, чтобы избежать изменений во время итерации
        val botUnitsCopy = botUnits.toList()

        botUnitsCopy.forEach { botCell ->
            val botUnitMovementDistance = getMaxDistanceForBot(botCell)
            // Поиск ближайшего юнита игрока для атаки
            val targetCell = findNearestPlayerUnit(botCell, playerUnits)

            // Поиск ближайшей соседней с юнитом клетки из которой можно атаковать
            val nearestCell: Cell? = findNearestFreeCellNearPlayerUnit(botCell, targetCell)

            //  Перемещение и проведение атаки
            if (targetCell != null && nearestCell != null && canMove(
                    botCell,
                    nearestCell,
                    botUnitMovementDistance
                )
            ) {
                moveUnit(botCell, nearestCell, botUnitMovementDistance)
                attackUnit(nearestCell, targetCell, context)

            } else {
                // Если атаковать некого, двигаемся в сторону замка игрока
                moveTowardsPlayerCastle(botCell)
            }
        }
        when (checkGameOver()) {
            GameResult.PlayerWins -> showGameOver(context, "Player Wins!")
            else -> {}
        }
    }

    private fun showGameOver(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        _isGameOver = message
    }

    private fun moveTowardsPlayerCastle(botCell: Cell) {
        val playerCastle = findPlayerCastle() ?: return
        val botUnitMovementDistance = getMaxDistanceForBot(botCell)
        var availableMoves = getAvailableForBotMoves(botCell, botUnitMovementDistance)

        if (canMove(botCell, playerCastle, botUnitMovementDistance)) {
            moveUnit(botCell, playerCastle, botUnitMovementDistance)

        } else {
            availableMoves = availableMoves.filter { it.unit == null }

            val targetCell = availableMoves.minByOrNull { cell ->
                findDistance(cell, playerCastle)
            }
            if (targetCell != null && targetCell.unit == null) {
                moveUnit(botCell, targetCell, botUnitMovementDistance)
            }
        }


    }

    private fun findPlayerCastle(): Cell? {
        return gameField.getCellList().firstOrNull { it.castle?.isPlayer == true }
    }

    private fun findBotCastle(): Cell? {
        return gameField.getCellList().firstOrNull { it.castle?.isPlayer == false }
    }

    private fun attackUnit(from: Cell, to: Cell, context: Context) {
        val attacker = from.unit ?: return
        val defender = to.unit ?: return


        if (attacker.hasAttacked) return

        if (attacker.isPlayer != defender.isPlayer) {
            defender.health -= attacker.strength
            attacker.hasAttacked = true

            Toast.makeText(
                context,
                "${defender.name} took ${attacker.strength} of damage. ${defender.name} health is ${defender.health}",
                Toast.LENGTH_LONG
            ).show()

            if (defender.health <= 0) {
                to.unit = null // Юнит игрока уничтожен
                if(attacker.isPlayer){
                    findPlayerCastle()?.castle?.addGold(1000)
                }
                else{
                    findBotCastle()?.castle?.addGold(1000)
                }
            }
        }
    }

    private fun findNearestPlayerUnit(botCell: Cell, playerUnits: List<Cell>): Cell? {
        val nearest = playerUnits.minByOrNull { cell ->
            findDistance(botCell, cell)
        }
        return nearest
    }

    private fun findNearestFreeCellNearPlayerUnit(botCell: Cell, playerUnitCell: Cell?): Cell? {
        if (playerUnitCell != null) {
            if (findDistance(botCell, playerUnitCell) == 1) return botCell

            var neighbors = getNeighbors(playerUnitCell)
            neighbors = neighbors.filter { it.unit == null }

            return neighbors.minByOrNull { cell ->
                findDistance(botCell, cell)
            }
        } else return null
    }

    private fun getNeighbors(cell: Cell): List<Cell> {
        val neighbors = mutableListOf<Cell>()

        // Перебираем все возможные смещения (включая диагонали)
        for (dx in -1..1) {
            for (dy in -1..1) {
                if (dx == 0 && dy == 0) continue // Пропускаем текущую клетку

                val neighborX = cell.x + dx
                val neighborY = cell.y + dy

                // Проверяем, что соседняя клетка находится в пределах поля
                val neighborCell = gameField.getCell(neighborX, neighborY)
                if (neighborCell != null) {
                    neighbors.add(neighborCell)
                }
            }
        }
        return neighbors
    }

    private fun moveUnit(from: Cell, to: Cell, maxDistance: Int) {
        val unit = from.unit ?: return
        if (unit.hasMoved) return
        if (to.unit == null && canMove(from, to, maxDistance)) {
            to.unit = from.unit
            from.unit = null
            unit.hasMoved = true
        }
    }

    private fun findDistance(from: Cell, to: Cell): Int {
        val dx = abs(to.x - from.x)
        val dy = abs(to.y - from.y)
        val distance = sqrt((dx * dx + dy * dy).toDouble()).roundToInt()
        return distance
    }

    private fun getAvailableMoves(from: Cell, maxDistance: Int) {
        val list = mutableListOf<Cell>()
        val cellList = gameField.getCellList()

        for (cell in cellList) {
            if (canMove(from, cell, maxDistance)) {
                list.add(cell)
            }
        }
        _availableMoves.value = list
    }

    private fun getAvailableForBotMoves(from: Cell, maxDistance: Int): List<Cell> {
        val list = mutableListOf<Cell>()
        val cellList = gameField.getCellList().filter { it.unit == null }
        for (cell in cellList) {
            if (canMove(from, cell, maxDistance)) {
                list.add(cell)
            }
        }
        return list
    }

    private fun getMaxDistanceForBot(cell: Cell): Int {
        val maxDistance = cell.unit?.maxDistance ?: 0
        return maxDistance
    }

    private fun getMaxDistanceFromSelectedCell(): Int {
        val maxDistance = _selectedCell!!.unit?.maxDistance ?: 0

        return maxDistance
    }

    private fun canMove(from: Cell, to: Cell, maxDistance: Int): Boolean {
        var distance = findDistance(from, to)

        if(from.unit?.isPlayer == true){
            distance += when (to.terrain) {
                Terrain.ROAD -> if (from.unit != null) -1 else 0// бонус на дороге
                Terrain.FRIENDLY -> 1 // Штраф на дружественной территории
                Terrain.ENEMY -> 3 // Штраф на вражеской территории
                Terrain.OBSTACLE -> 3 // Препятствие
            }
        }else{
            distance += when (to.terrain) {
                Terrain.ROAD -> if (from.unit != null) -1 else 0 // бонус на дороге
                Terrain.FRIENDLY -> 3 // Штраф на дружественной территории
                Terrain.ENEMY -> 1 // Штраф на вражеской территории
                Terrain.OBSTACLE -> 3 // Препятствие
            }
        }

//        distance += terrainPenalty
        return distance <= maxDistance
    }

    private fun canAttack(from: Cell, to: Cell): Boolean {
        val distance = findDistance(from, to)
        val attackDistance = from.unit?.attackDistance ?: 0
        return distance <= attackDistance && to.unit?.isPlayer != from.unit?.isPlayer
    }

    private fun resetUnitActions() {
        gameField.getCellList().forEach { cell ->
            cell.unit?.hasMoved = false
            cell.unit?.hasAttacked = false
        }
    }

    private fun checkGameOver(): GameResult {
        val playerCastle = findPlayerCastle()
        val botCastle = findBotCastle()

        // Проверка захвата замков

        // юнит бота на замке игрока
        if (playerCastle?.unit?.isPlayer == false) {
            return GameResult.BotWins // Бот захватил замок игрока
        }

        // юнит игрока в замке бота
        if (botCastle?.unit?.isPlayer == true) {
            return GameResult.PlayerWins // Игрок захватил замок бота
        }
        // Проверка наличия юнитов и героев
        val playerUnits = gameField.getCellList().filter { it.unit?.isPlayer == true }
        val botUnits = gameField.getCellList().filter { it.unit?.isPlayer == false }

        if (playerUnits.isEmpty()) {
            return GameResult.BotWins // У игрока не осталось юнитов
        }
        if (botUnits.isEmpty()) {
            return GameResult.PlayerWins // У бота не осталось юнитов
        }

        return GameResult.Continue // Игра продолжается
    }

}