package com.example.heroesofiu3.data

import android.content.Context
import com.example.heroesofiu3.data.DataEntities.GameSave
import com.example.heroesofiu3.data.room.AppDatabase
import com.example.heroesofiu3.domain.entities.gameField.GameField

class GameSavesDbRepository(context: Context) {

	suspend fun saveGame(context: Context, gameField: GameField, saveName: String) {
		val db = AppDatabase.getDatabase(context)
		val gameSave = GameSave(
			id = 0,
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

	suspend fun deleteAll(context: Context){
		val db = AppDatabase.getDatabase(context)
		db.gameSaveDao().deleteAll()
	}

	suspend fun getSavesCount(context: Context): Int {
		val db = AppDatabase.getDatabase(context)
		return db.gameSaveDao().getCount()

	}
	suspend fun getAll(context: Context): List<GameSave> {
		val db = AppDatabase.getDatabase(context)
		return db.gameSaveDao().getAll()
	}

	suspend fun deleteById(context: Context, id: Int){
		val db = AppDatabase.getDatabase(context)
		db.gameSaveDao().deleteByID(id)
	}

}


