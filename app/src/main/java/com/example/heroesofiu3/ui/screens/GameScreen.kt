package com.example.heroesofiu3.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.heroesofiu3.LocalSharedViewModel
import com.example.heroesofiu3.Screen
import com.example.heroesofiu3.domain.entities.gameField.Cell
import com.example.heroesofiu3.domain.game.initializeField
import com.example.heroesofiu3.ui.components.BuildMenu
import com.example.heroesofiu3.ui.components.CellInfo
import com.example.heroesofiu3.ui.components.CellView

@Composable
fun GameScreen(navController: NavHostController) {
    val viewModel = LocalSharedViewModel.current

    val context = LocalContext.current
    val gameState by viewModel.gameState.collectAsState()
    val gameField = gameState.gameField
    val selectedCell = gameState.selectedCell
    val availableMoves by gameState.availableMoves.collectAsState()
    val isGameOver = gameState.isGameOver

    val loadedGameField = gameState.loadedGameField

    // Состояние для отображения GameOverScreen
    var showGameOverScreen by remember { mutableStateOf(false) }


    LaunchedEffect(isGameOver) {
        if (isGameOver != "") {
            showGameOverScreen = true // Показываем GameOverScreen
        }
    }

    // Обновление состояния игры при загрузке
    LaunchedEffect(loadedGameField) {
        loadedGameField?.let { field ->
            gameState.updateGameField(field) // Обновление состояния игры
        }
        // костыль, чтобы вызвать рекомпозицию, по нормальному почему-то не работает
        gameState.selectCell(Cell(100,100), context)

    }

    // Отображение игрового поля
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, start = 14.dp, end = 14.dp, bottom = 8.dp),
    ) {
        Row(
            modifier = Modifier.clickable { navController.navigate(Screen.SaveMenu.route) },
            horizontalArrangement = Arrangement.Absolute.Right
        ) {
            Icon(
                imageVector = Icons.Rounded.Menu,
                contentDescription = "Saves",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        LazyVerticalGrid(
            modifier = Modifier
                .wrapContentSize()
                .aspectRatio(1f),
            columns = GridCells.Fixed(gameField.width),
            horizontalArrangement = Arrangement.Center
        ) {
            items(gameField.width * gameField.height) { index ->
                val x = index % gameField.width
                val y = index / gameField.width
                val currentCell = gameField.getCell(x, y)
                if (currentCell != null) {
                    CellView(
                        cell = currentCell,
                        isSelected = currentCell == selectedCell,
                        onClick = {
                            gameState.selectCell(currentCell, context)
                        },
                        isAvailable = availableMoves.contains(currentCell)
                    )
                }
            }
        }
        //  Вывод информации о клетке
        if (selectedCell != null) {
            CellInfo(selectedCell)
        }

        //  Вызов меню покупки зданий
        val selectedCastle = selectedCell?.castle
        if (selectedCastle != null && selectedCastle.isPlayer) {
            BuildMenu(selectedCell)
        }

        // кнопочки концца хода и в главное меню не показываем при отображении меню построек замка, чтобе не перегружать экран
        if(selectedCell?.castle == null){
            Column (modifier = Modifier.fillMaxSize()){
                // Кнопка "Завершить ход"
                Button(
                    onClick = {
                        gameState.makeBotMove(context) // Вызов хода бота
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Завершить ход")
                }

                Spacer(Modifier.weight(1f))

                // Кнопка в меню
                OutlinedButton(
                    onClick = { navController.navigate(Screen.MainScreen.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text("Вернуться в меню")
                }

            }
        }




    }

    if (showGameOverScreen) {
        GameOverScreen(
            message = isGameOver,
            onRestart = {
                gameState.resetGame() // Сброс состояния игры
                initializeField(gameField) // Повторная инициализация поля
                showGameOverScreen = false
            }
        )
    }
}




