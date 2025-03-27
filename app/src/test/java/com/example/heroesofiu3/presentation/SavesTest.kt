package com.example.heroesofiu3.presentation

import android.content.Context
import com.example.heroesofiu3.data.GameSavesDbRepository
import com.example.heroesofiu3.domain.entities.Units.Hero
import com.example.heroesofiu3.domain.entities.buildings.Barracks
import com.example.heroesofiu3.domain.entities.buildings.Castle
import com.example.heroesofiu3.domain.entities.gameField.Terrain
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.mockito.Mockito.mock

class SavesTest {
	private fun initializeGame(): GameState {
		val gameState = GameState(10,10)
		val field = gameState.gameField
		// Игрок
		field.getCell(1, 1)?.castle = Castle("Blue", true)
		field.getCell(1, 1)?.castle!!.addGold(1000)

		// Бот:
		field.getCell(9, 9)?.unit = Hero("Hero", false)
		field.getCell(8, 8)?.castle = Castle("Red", false)
		field.getCell(8, 8)?.castle!!.addBuiding(Barracks())
		field.getCell(8, 8)?.terrain = Terrain.UNREACHABLE

		return gameState
	}

	@Test
	fun `gameField should be saved as GameFieldData`(){
		val gameState = initializeGame()
		val context = mock(Context::class.java)
		val gameField = gameState.gameField
		val repository = GameSavesDbRepository(context)
		runBlocking {
			repository.saveGame(context,gameField,"save")
			val loadedGame = repository.loadGame(context,0)
			assertNotNull(loadedGame)
		}



	}

}