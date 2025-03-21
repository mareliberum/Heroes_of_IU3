package com.example.heroesofiu3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.example.heroesofiu3.data.GameSavesDbRepository
import com.example.heroesofiu3.ui.screens.GameScreen
import com.example.heroesofiu3.ui.theme.HeroesOfIU3Theme

class MainActivity : ComponentActivity() {

//    private lateinit var gameSaveDao: GameSaveDao
    private lateinit var repository: GameSavesDbRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = GameSavesDbRepository(this)


//        AppDatabase.initDatabase(this)
//        gameSaveDao = AppDatabase.INSTANCE.gameSaveDao()

        enableEdgeToEdge()

        setContent {
            HeroesOfIU3Theme {
                Surface {

                    GameScreen(repository)
                }
            }
        }
    }



}









