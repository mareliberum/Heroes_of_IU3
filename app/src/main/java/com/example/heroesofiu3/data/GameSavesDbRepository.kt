package com.example.heroesofiu3.data

import android.content.Context
import com.example.heroesofiu3.data.DataEntities.GameSave
import com.example.heroesofiu3.data.room.AppDatabase
import com.example.heroesofiu3.domain.entities.gameField.GameField

interface IGameSavesRepository {
	suspend fun saveGame(context: Context, gameField: GameField, name: String, id: Int, player: String)
	suspend fun loadGame(context: Context, id: Int): GameField?
	suspend fun getSavesCount(context: Context): Int
	suspend fun getByPlayerName(context: Context, playerName: String): List<GameSave>
	suspend fun deleteById(context: Context, id: Int)
}

class GameSavesDbRepository(private val context: Context) : IGameSavesRepository {

	override suspend fun saveGame(context: Context, gameField: GameField, name: String, score: Int, player: String) {
		val db = AppDatabase.getDatabase(context)
		val gameSave = GameSave(
			id = 0,
			name = name,
			gameFieldJson = gameField.toJson(),
			score = score,
			player = player,
		)
		println("saved $gameSave")
		db.gameSaveDao().insert(gameSave)
	}

	override suspend fun loadGame(context: Context, id: Int): GameField? {
		val db = AppDatabase.getDatabase(context)
		val gameSave = db.gameSaveDao().getById(id)
		gameSave?.score
		return gameSave?.gameFieldJson?.toGameField()
	}

	suspend fun getScore(context: Context, saveId: Int): Int? {
		val db = AppDatabase.getDatabase(context)
		val gameSave = db.gameSaveDao().getById(saveId)
		return gameSave?.score
	}

	suspend fun deleteAll(context: Context){
		val db = AppDatabase.getDatabase(context)
		db.gameSaveDao().deleteAll()
	}

	override suspend fun getSavesCount(context: Context): Int {
		val db = AppDatabase.getDatabase(context)
		return db.gameSaveDao().getCount()
	}

	suspend fun getAll(context: Context): List<GameSave> {
		val db = AppDatabase.getDatabase(context)
		return db.gameSaveDao().getAll()
	}

	override suspend fun getByPlayerName(context: Context, playerName: String): List<GameSave> {
		val db = AppDatabase.getDatabase(context)
		return db.gameSaveDao().getByPlayerName(playerName)
	}

	override suspend fun deleteById(context: Context, id: Int){
		val db = AppDatabase.getDatabase(context)
		db.gameSaveDao().deleteByID(id)
	}

}
