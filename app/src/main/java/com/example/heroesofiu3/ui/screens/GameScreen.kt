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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.heroesofiu3.data.GameSavesDbRepository
import com.example.heroesofiu3.domain.entities.gameField.GameField
import com.example.heroesofiu3.domain.game.initializeField
import com.example.heroesofiu3.presentation.GameState
import com.example.heroesofiu3.ui.components.BuildMenu
import com.example.heroesofiu3.ui.components.CellInfo
import com.example.heroesofiu3.ui.components.CellView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GameScreen(repository: GameSavesDbRepository) {
    val context = LocalContext.current
    val gameState = remember { GameState(10, 10) }
    val gameField = gameState.gameField
    val selectedCell = gameState.selectedCell
    val availableMoves by gameState.availableMoves.collectAsState()
    val isGameOver = gameState.isGameOver
    val coroutineScope = rememberCoroutineScope()

    // Состояние для загруженного GameField
    var loadedGameField by remember { mutableStateOf<GameField?>(null) }


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

    // Обновление состояния игры при загрузке
    LaunchedEffect(loadedGameField) {
        println("launched effect on update")
        loadedGameField?.let { field ->
            gameState.resetGame() // Сброс текущего состояния
            gameState.updateGameField(field) // Обновление состояния игры
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


        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    repository.saveGame(context,gameField,"save one")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("SaveGame")
        }



        // TODO реализовать функцию загрузки
        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    // При обновлении запускает Launched Effect на обновление экрана
                    loadedGameField = repository.loadGame(context,1)
                    println(loadedGameField)
                    println("game loaded")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Load last game")
        }

        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    println(repository.getSavesCount(context))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("debug println saves count")
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


