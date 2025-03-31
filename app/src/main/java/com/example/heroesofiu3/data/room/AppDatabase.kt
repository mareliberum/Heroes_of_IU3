package com.example.heroesofiu3.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.heroesofiu3.data.DataEntities.GameSave

@Database(entities = [GameSave::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
	abstract fun gameSaveDao(): GameSaveDao
	companion object {
		@Volatile
		private var INSTANCE: AppDatabase? = null

		fun getDatabase(context: Context): AppDatabase {
			return INSTANCE ?: synchronized(this) {
				val instance = Room.databaseBuilder(
					context.applicationContext,
					AppDatabase::class.java,
					"game_database",

				)
					.fallbackToDestructiveMigration()
					.build()
				INSTANCE = instance
				instance
			}
		}
	}

}