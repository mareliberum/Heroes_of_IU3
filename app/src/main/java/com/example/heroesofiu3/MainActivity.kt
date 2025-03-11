package com.example.heroesofiu3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.example.heroesofiu3.ui.screens.GameScreen
import com.example.heroesofiu3.ui.theme.HeroesOfIU3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HeroesOfIU3Theme {
                Surface {
                    GameScreen()
                }
            }
        }
    }
}









