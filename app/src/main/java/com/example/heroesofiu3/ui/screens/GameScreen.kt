package com.example.heroesofiu3.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
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
import com.example.heroesofiu3.domain.game.initializeField
import com.example.heroesofiu3.presentation.GameState
import com.example.heroesofiu3.ui.components.BuildMenu
import com.example.heroesofiu3.ui.components.CellInfo
import com.example.heroesofiu3.ui.components.CellView


@Composable
fun GameScreen() {
    val context = LocalContext.current
    val gameState = remember { GameState(10, 10) }
    val gameField = gameState.gameField
    val selectedCell = gameState.selectedCell
    val availableMoves by gameState.availableMoves.collectAsState()
    val isGameOver = gameState.isGameOver


    // Состояние для отображения GameOverScreen
    var showGameOverScreen by remember { mutableStateOf(false) }


    // Инициализация поля
    LaunchedEffect(Unit) {
        initializeField(gameField)
    }

    LaunchedEffect(isGameOver) {
        if (isGameOver != "") {
            showGameOverScreen = true // Показываем GameOverScreen
        }
    }

    // Отображение игрового поля
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, start = 14.dp, end = 14.dp, bottom = 8.dp),
    ) {
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
