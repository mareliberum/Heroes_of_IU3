package com.example.heroesofiu3.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.heroesofiu3.data.DataEntities.GameSave
import kotlinx.coroutines.flow.Flow

@Dao
interface GameSaveDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(gameSave: GameSave)

	@Query("SELECT * FROM GameSave ORDER BY id DESC")
	fun getAll(): Flow<List<GameSave>> // Получить все сохранения

	@Query("SELECT * FROM GameSave WHERE id = :id")
	suspend fun getById(id: Int): GameSave? // Получить сохранение по ID

	@Query("DELETE FROM GameSave ")
	suspend fun deleteAll() // Удалить сохранение по ID

	@Query("SELECT COUNT(*) FROM GameSave")
	suspend fun getCount(): Int // Получить количество записей

}