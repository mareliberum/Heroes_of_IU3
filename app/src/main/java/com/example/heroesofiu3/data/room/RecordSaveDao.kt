package com.example.heroesofiu3.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.heroesofiu3.data.DataEntities.RecordSave

@Dao
interface RecordSaveDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(recordSave: RecordSave)

	@Query("SELECT * FROM RecordSave ORDER BY id DESC")
	suspend fun getAll(): List<RecordSave> // Получить все рекорды

//	@Query("SELECT * FROM RecordSave WHERE id = :id")
//	suspend fun getById(id: Int): RecordSave? // Получить рекорд по ID
//
//	@Query("DELETE FROM RecordSave ")
//	suspend fun deleteAll() // Удалить все рекорды

	@Query("DELETE FROM RecordSave WHERE id = :id ")
	suspend fun deleteByID(id : Int) // Удалить рекорд по ID

	@Query("DELETE FROM RecordSave WHERE name = :name ")
	suspend fun deletePlayerRecords(name : String) // Удалить рекорд по ID

	@Query("SELECT * FROM RecordSave WHERE name = :name")
	suspend fun getRecordByName(name: String) : RecordSave?
}
