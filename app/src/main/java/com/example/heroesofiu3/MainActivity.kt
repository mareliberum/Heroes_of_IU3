package com.example.heroesofiu3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.heroesofiu3.data.GameSavesDbRepository
import com.example.heroesofiu3.domain.viewModels.SharedViewModel
import com.example.heroesofiu3.ui.screens.GameScreen
import com.example.heroesofiu3.ui.screens.MainScreen
import com.example.heroesofiu3.ui.screens.SaveMenu
import com.example.heroesofiu3.ui.theme.HeroesOfIU3Theme

val LocalGameSavesRepository = staticCompositionLocalOf<GameSavesDbRepository> {
    error("GameSavesDbRepository not provided!")
}

val LocalSharedViewModel = staticCompositionLocalOf<SharedViewModel> {
    error("ViewModel not provided!")
}

class MainActivity : ComponentActivity() {

    private lateinit var repository: GameSavesDbRepository
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = GameSavesDbRepository(this)

        enableEdgeToEdge()

        setContent {
            // Оборачиваем все приложение в CompositionLocalProvider
            CompositionLocalProvider(
                LocalGameSavesRepository provides repository,
                LocalSharedViewModel provides sharedViewModel,
            ) {
                HeroesOfIU3Theme {
                    App()
                }
            }
        }
    }
}


@Composable
fun App() {
    val navController = rememberNavController()
    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding(),
    ) {
        Column {
            NavHost(
                navController = navController,
                startDestination = Screen.MainScreen.route
            ) {
                // Главный экран
                composable(Screen.MainScreen.route) {
                    MainScreen(navController)
                }

                // Экран новой игры  (пока нет) TODO : Сделать newGameScreen
//                composable(Screen.NewGameScreen.route) {
//                    NewGameScreen(navController)
//                }

                // Экран списка сохранений
                composable(Screen.SaveMenu.route) {
                    SaveMenu(navController)
                }

                // Экран игры
                composable(Screen.GameScreen.route) {
                    GameScreen(navController)
                }
            }

        }
    }

}

sealed class Screen(val route: String) {
    data object MainScreen : Screen("main")
    data object NewGameScreen : Screen("new_game")
    data object SaveMenu : Screen("save_menu")
    data object GameScreen : Screen("game/{gameFieldJson}")

//        {fun createRoute(gameFieldJson: String): String {
//            return "game/$gameFieldJson"
//        }}

}

//TODO защита

//  при покупке таверны дается возможность даетсяв возможность сыграть в миниигру (кости). Бросить кости - тряхнуть телефон.
// вибрации звук. У кого больше - тот победил. Возможность сделать ставку




