package com.example.heroesofiu3.presentation

import android.content.Context
import com.example.heroesofiu3.domain.entities.Units.Archer
import com.example.heroesofiu3.domain.entities.Units.Hero
import com.example.heroesofiu3.domain.entities.Units.Knight
import com.example.heroesofiu3.domain.entities.buildings.Barracks
import com.example.heroesofiu3.domain.entities.buildings.Castle
import com.example.heroesofiu3.domain.entities.buildings.Fort
import com.example.heroesofiu3.domain.entities.buildings.Tavern
import com.example.heroesofiu3.domain.entities.gameField.Cell
import com.example.heroesofiu3.domain.entities.gameField.GameField
import com.example.heroesofiu3.domain.entities.gameField.GameResult
import com.example.heroesofiu3.domain.entities.gameField.Terrain
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.Mockito.mock


class GameStateTest {
    private fun initializeTestField(field: GameField) {
        // Игрок
        field.getCell(1, 1)?.castle = Castle("Blue", true)
        field.getCell(1, 1)?.castle!!.addGold(1000)

        // Бот:
        field.getCell(9, 9)?.unit = Hero("Hero", false)
        field.getCell(8, 8)?.castle = Castle("Red", false)
        field.getCell(8, 8)?.castle!!.addBuiding(Barracks())
        field.getCell(8, 8)?.terrain = Terrain.UNREACHABLE
    }

    @Test
    fun `game field should be initialized correctly`() {
        val gameState = GameState(10, 10)
        assertNotNull(gameState.gameField)
        assertEquals(10, gameState.gameField.width)
        assertEquals(10, gameState.gameField.height)
    }

    @Test
    fun `resetGame should reset gameState`() {
        val gameState = GameState(10, 10)
        val cell = gameState.gameField.getCell(1,1)!!
        cell.unit = Knight("Knight", true)
        gameState.selectCell(cell, mock(Context::class.java))
        gameState.resetGame()
        assertNull(gameState.selectedCell)
        assertTrue(gameState.availableMoves.value.isEmpty())
        assertTrue(gameState.isGameOver.isEmpty())
    }

    @Test
    fun `selected cell should be stored as selected`() {
        val gameState = GameState(10, 10)
        val cell = Cell(0, 0)
        gameState.selectCell(cell, mock(Context::class.java))
        assertEquals(cell, gameState.selectedCell)
    }


    @Test
    fun `units should be able to move`() {
        val gameState = GameState(10, 10)
        val from = Cell(0, 0)
        val to = Cell(1, 1)
        from.unit = Hero("Hero", true)
        gameState.selectCell(from, mock(Context::class.java))
        gameState.selectCell(to, mock(Context::class.java))

        assertNull(from.unit)
        assertNotNull(to.unit)
    }

    @Test
    fun `units shold be able to attack`() {
        val gameState = GameState(10, 10)
        val attackerCell = Cell(0, 0)
        val defenderCell = Cell(1, 1)
        val context = mock(Context::class.java)

        attackerCell.unit = Hero("hero", true, strength = 10)
        defenderCell.unit = Hero("villain", false, health = 100)

        gameState.selectCell(attackerCell, context)
        gameState.selectCell(defenderCell, context)

        assertEquals(90, defenderCell.unit?.health) // Пример проверки здоровья после атаки
    }

    @Test
    fun `game should be over when castle is taken`() {
        val gameState = GameState(10, 10)
        val field = gameState.gameField
        val botCastleCell = field.getCell(8, 8)
        val playerCastleCell = field.getCell(8, 8)

        playerCastleCell?.castle = Castle("Blue", true)
        botCastleCell?.castle = Castle("Red", false)

        //Симуляция захвата замка бота игроком
        botCastleCell?.unit = Hero("Hero", true)
        assertEquals(GameResult.PlayerWins, gameState.checkGameOver())

        //  Симуляция захвата замка игрока ботом
        playerCastleCell?.unit = Hero("bit", false)
        assertEquals(GameResult.BotWins, gameState.checkGameOver())

    }

    @Test
    fun `game should end if there is no units`() {
        val gameState = GameState(10, 10)
        val field = gameState.gameField

        field.getCell(0, 0)?.unit = Hero("hero", true)
        assertEquals(GameResult.PlayerWins, gameState.checkGameOver())

        field.getCell(0, 0)?.unit = Hero("hero", false)
        assertEquals(GameResult.BotWins, gameState.checkGameOver())
    }

    @Test
    fun `terrain should apply penalty on move range`() {
        val gameState = GameState(10, 10)


        // расстояние между юнитом и целевой клеткой = 5
        val unitCell = Cell(0, 0)
        val targetCell = Cell(5, 0)
        unitCell.unit = Hero("hero", true, maxDistance = 5)

        assertEquals(gameState.canMove(unitCell, targetCell, 5), true)

        for (terrain in Terrain.entries) {
            if (terrain == Terrain.ROAD) {
                targetCell.terrain = terrain
                assertEquals(gameState.canMove(unitCell, targetCell, 6), true)
                continue
            } else if (terrain == Terrain.UNREACHABLE) {
                targetCell.terrain = terrain
                assertEquals(gameState.canMove(unitCell, targetCell, Int.MAX_VALUE), false)
                continue
            } else {
                targetCell.terrain = terrain
                assertEquals(gameState.canMove(unitCell, targetCell, 5), false)
            }
        }

    }

    @Test
    fun `units should be able to attack only within their attack range`() {
        val context = mock(Context::class.java)
        val gameState = GameState(10, 10)
        val attackDistance = 3

        //  на дистанции 2..3 атакую, на 4 - нет
        for (distance in attackDistance - 1..attackDistance + 1) {
            val unitCell = Cell(1, 1)
            val targetUitCell = Cell(1 + distance, 1)

            unitCell.unit = Archer("", true, strength = 10, attackDistance = attackDistance)
            targetUitCell.unit = Hero("", false, health = 100)

            gameState.selectCell(unitCell, context)
            gameState.selectCell(targetUitCell, context)

            if (distance <= attackDistance) {
                //  Получилось атаковать, если true
                assertEquals(90, targetUitCell.unit?.health)
            } else {
                assertEquals(100, targetUitCell.unit?.health)
            }
        }
    }

    //  TODO Спросить насчет этого теста
    //  Он кажется бесполезным, поскольку клетка выбирается с экрана,
    //  на котором отображен список клеток из gameField

    @Test
    fun `gameField shouldn't contain cells outside of the field`(){
        val gameState = GameState(10,10)
        val cellOutOfBound = gameState.gameField.getCell(11,11)
        assertNull(cellOutOfBound)
    }

    // Проврка насчет нахождения юнитов на одной клетке тоже бесполезна, ввиду реализации Cell.unit

    @Test
    fun `attackUnit should kill unit if makes it's hp less than 0`(){
        val gameState = GameState(10,10)
        val unitCell = gameState.gameField.getCell(0,0)!!
        val targetCell = gameState.gameField.getCell(1,1)!!
        val mockContext = mock(Context :: class.java)
        unitCell.unit = Hero("", true, strength = 100)
        targetCell.unit = Hero("", false, health = 100)
        gameState.selectCell(unitCell, mockContext)
        gameState.selectCell(targetCell, mockContext)
        assertNull(targetCell.unit)
    }

    @Test
    fun `Barracks should spend money and spawn knight`(){
        val gameState = GameState(10,10)
        val field = gameState.gameField
        val castleCell = field.getCell(1,1)
        castleCell?.castle = Castle("", true)
        castleCell?.castle?.addGold(10000)
        castleCell?.castle?.addBuiding(Barracks())
        castleCell?.castle?.buildings?.get(0)?.executeEffect(castleCell)

        assertTrue(castleCell?.unit is Knight)
    }

    @Test
    fun `Tavern should spend money and spawn hero`(){
        val gameState = GameState(10,10)
        val field = gameState.gameField
        val castleCell = field.getCell(1,1)
        castleCell?.castle = Castle("", true)
        castleCell?.castle?.addGold(10000)
        castleCell?.castle?.addBuiding(Tavern())
        castleCell?.castle?.buildings?.get(0)?.executeEffect(castleCell)
        assertTrue(castleCell?.unit is Hero)
    }

    @Test
    fun `bot should buy units if has money`(){
        val mockContext = mock(Context :: class.java)
        val gameState = GameState(10,10)
        val field = gameState.gameField
        initializeTestField(field)
        val botCastleCell = field.getCell(8,8)
        val botCastle = botCastleCell?.castle

        gameState.makeBotMove(mockContext)
        assertNull(botCastleCell?.unit)

        botCastle?.addGold(1000)
        gameState.makeBotMove(mockContext)
        assertNotNull(botCastleCell?.unit)
    }

    @Test
    fun `bot should come up and attack closest unit within range`(){
        val mockContext = mock(Context :: class.java)
        val gameState = GameState(10,10)
        val field = gameState.gameField
        val playerUnit = Hero("player", true, health = 100)
        field.getCell(2,2)?.unit = playerUnit
        field.getCell(1,1)?.unit = Hero("bot", false, strength = 90)

        gameState.makeBotMove(mockContext)

        assert(playerUnit.health < 100)
    }

    @Test
    fun `bot should take over castle, when there is no player's unit in range`(){
        val mockContext = mock(Context :: class.java)
        val gameState = GameState(10,10)
        val field = gameState.gameField
        initializeTestField(field)
        val playerCastle = field.getCell(1,1)
        val botUnit = Hero("bot", false)
        field.getCell(2,2)?.unit = botUnit

        gameState.makeBotMove(mockContext)
        assertTrue(playerCastle?.unit == botUnit)
    }
    @Test
    fun `attackCastle with Hero and Fort should start siege and decrease it's hp`(){
        val mockContext = mock(Context :: class.java)
        val gameState = GameState(10,10)
        val field = gameState.gameField
        initializeTestField(field)
        val castleCell = field.getCell(1,1)
        castleCell?.castle?.addBuiding(Fort())
        castleCell?.unit = Hero("", true)
        field.getCell(2,2)?.unit = Hero("bot", false)

        val castleHealthBeforeAttack = castleCell?.castle?.health ?: 0
        gameState.makeBotMove(mockContext)

        assertTrue(castleCell?.castle?.isUnderSiege == true)
        assertTrue((castleCell?.castle?.health ?: 0) < castleHealthBeforeAttack)
    }

    @Test
    fun `attackCastle under siege from WALL should decrease attacker's hp`(){
        val mockContext = mock(Context :: class.java)
        val gameState = GameState(10,10)
        val field = gameState.gameField
        initializeTestField(field)
        val castleCell = field.getCell(1,1)
        castleCell?.castle?.addBuiding(Fort())
        castleCell?.unit = Hero("", true)

        val botUnit = Hero("bot", false, health = 100)
        val botCell = field.getCell(2,1)
        botCell?.unit = botUnit

        gameState.makeBotMove(mockContext)
        assertTrue(botUnit.health < 100)
    }

    @Test
    fun `attackCastle under siege from GATE shouldn't decrease attacker's hp`(){
        val mockContext = mock(Context :: class.java)
        val gameState = GameState(10,10)
        val field = gameState.gameField
        initializeTestField(field)
        val castleCell = field.getCell(1,1)
        castleCell?.castle?.addBuiding(Fort())
        castleCell?.unit = Hero("", true)

        val botUnit = Hero("bot", false, health = 100)
        val botCell =field.getCell(2,2)
        botCell?.unit = botUnit
        botCell?.terrain = Terrain.GATE

        gameState.makeBotMove(mockContext)
        assertTrue(botUnit.health == 100)
    }

}