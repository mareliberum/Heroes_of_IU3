package com.example.heroesofiu3.data

import android.content.Context
import com.example.heroesofiu3.domain.entities.gameField.GameField
import kotlinx.coroutines.flow.Flow

class GameSavesDbRepository(context: Context) {

	suspend fun saveGame(context: Context, gameField: GameField, saveName: String) {
		val db = AppDatabase.getDatabase(context)
		val gameSave = GameSave(
			id = 1,
			name = saveName,
			gameFieldJson = gameField.toJson()
		)
		println("saved $gameSave")
		db.gameSaveDao().insert(gameSave)

	}

	suspend fun loadGame(context: Context, saveId: Int): GameField? {
		val db = AppDatabase.getDatabase(context)
		val gameSave = db.gameSaveDao().getById(saveId)
		println(gameSave)
		return gameSave?.gameFieldJson?.toGameField()
	}

	fun getGameSaves(context: Context): Flow<List<GameSave>> {
		val db = AppDatabase.getDatabase(context)

		return db.gameSaveDao().getAll()
	}

	suspend fun getSavesCount(context: Context): Int {
		val db = AppDatabase.getDatabase(context)
		return db.gameSaveDao().getCount()

	}

}