package com.example.heroesofiu3.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GameSave::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
	abstract fun gameSaveDao(): GameSaveDao

	/*companion object {
		@Volatile
		lateinit var INSTANCE: AppDatabase

		fun initDatabase(context: Context): AppDatabase {
			val instance = Room.databaseBuilder(
				context.applicationContext,
				AppDatabase::class.java,
				"app_database"
			)
				.fallbackToDestructiveMigration()
				.build()
			INSTANCE = instance
			return instance
		}
	}*/

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