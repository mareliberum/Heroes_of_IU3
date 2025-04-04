package com.example.heroesofiu3.data

import android.content.Context
import com.example.heroesofiu3.data.DataEntities.RecordSave
import com.example.heroesofiu3.data.room.AppDatabase

class RecordsDbRepository(context: Context){

	suspend fun saveRecord(context: Context, recordName: String, score : Int) {
		val db = AppDatabase.getDatabase(context)
		val currentRecord = db.recordSaveDao().getRecordByName(recordName)
		if(currentRecord == null || score > currentRecord.score ){
			db.recordSaveDao().deletePlayerRecords(recordName)
			val recordSave = RecordSave(
				id = 0,
				name = recordName,
				score = score,
				date = getCurrentDateTime(),
			)
			db.recordSaveDao().insert(recordSave)
		}
	}

	suspend fun getAll(context: Context): List<RecordSave> {
		val db = AppDatabase.getDatabase(context)
		return db.recordSaveDao().getAll()
	}

}
