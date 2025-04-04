package com.example.heroesofiu3.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.heroesofiu3.R
import com.example.heroesofiu3.domain.viewModels.DiceGameViewModel

@Composable
fun DiceGameScreen(navController: NavController) {
    // Получаем viewModel
    val viewModel: DiceGameViewModel = viewModel(factory = DiceGameViewModel.Factory)

    // Настраиваем эффект для управления жизненным циклом детектора тряски
    DisposableEffect(key1 = viewModel) {
        viewModel.startListening()
        onDispose {
            viewModel.stopListening()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Кнопка назад
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Назад",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        // Показываем количество золота
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Money,
                contentDescription = "Золото",
                tint = Color(0xFFFFD700)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${viewModel.playerGold}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Основное содержимое
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Заголовок
            Text(
                text = "Игра в кости",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Экран ставок
            if (viewModel.gameState == DiceGameViewModel.GameState.BETTING) {
                BettingSection(viewModel)
            } else {
                // Карточка с инструкцией для игры
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = getInstructionText(viewModel.gameState),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Показываем текущую ставку
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Money,
                        contentDescription = "Ставка",
                        tint = Color(0xFFFFD700)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Ставка: ${viewModel.currentBet}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Отображение костей
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Кость игрока
                    DiceDisplay(
                        value = viewModel.playerDiceValue,
                        label = "Ваш бросок",
                        isHighlighted = isPlayerWinning(viewModel.gameState)
                    )

                    // Кость бота
                    DiceDisplay(
                        value = viewModel.botDiceValue,
                        label = "Бросок бота",
                        isHighlighted = isBotWinning(viewModel.gameState)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Результат игры
                if (viewModel.gameState == DiceGameViewModel.GameState.PLAYER_WON ||
                    viewModel.gameState == DiceGameViewModel.GameState.BOT_WON ||
                    viewModel.gameState == DiceGameViewModel.GameState.DRAW
                ) {
                    Text(
                        text = getResultText(viewModel.gameState),
                        style = MaterialTheme.typography.headlineSmall,
                        color = getResultColor(viewModel.gameState),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Если была победа или поражение, показываем результат по деньгам
                    if (viewModel.gameState == DiceGameViewModel.GameState.PLAYER_WON) {
                        Text(
                            text = "+${viewModel.currentBet} золота",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Green,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    } else if (viewModel.gameState == DiceGameViewModel.GameState.BOT_WON) {
                        Text(
                            text = "-${viewModel.currentBet} золота",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Red,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    // Кнопка для новой игры
                    Button(onClick = { viewModel.resetGame() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Сыграть снова"
                        )
                        Text(
                            text = "Сыграть снова",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BettingSection(viewModel: DiceGameViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            // Заголовок секции ставок
            Text(
                text = "Сделайте вашу ставку",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Текущая ставка
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "${viewModel.currentBet}",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Контроль ставки
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Уменьшить ставку
                OutlinedButton(
                    onClick = { viewModel.decreaseBet() },
                    enabled = viewModel.currentBet > 10
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Уменьшить ставку"
                    )
                }

                // Увеличить ставку
                OutlinedButton(
                    onClick = { viewModel.increaseBet() },
                    enabled = viewModel.currentBet < viewModel.maxBet
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Увеличить ставку"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка подтверждения ставки
            Button(
                onClick = { viewModel.placeBet() },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Подтвердить ставку"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Подтвердить ставку")
            }
        }
    }
}

@Composable
fun DiceDisplay(value: Int, label: String, isHighlighted: Boolean) {
    val backgroundColor = if (isHighlighted) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Название
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Кость (пока просто отображаем число)
        Card(
            modifier = Modifier
                .size(100.dp)
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value.toString(),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

// Вспомогательные функции для получения текста и цветов
@Composable
private fun getInstructionText(gameState: DiceGameViewModel.GameState): String {
    return when (gameState) {
        DiceGameViewModel.GameState.WAITING_FOR_PLAYER -> "Встряхните устройство, чтобы бросить кости"
        DiceGameViewModel.GameState.PLAYER_ROLLED -> "Вы бросили кости..."
        DiceGameViewModel.GameState.BOT_ROLLED -> "Бот бросает кости..."
        DiceGameViewModel.GameState.BETTING -> "Сделайте вашу ставку"
        else -> "Игра завершена"
    }
}

@Composable
private fun getResultText(gameState: DiceGameViewModel.GameState): String {
    return when (gameState) {
        DiceGameViewModel.GameState.PLAYER_WON -> "Вы победили!"
        DiceGameViewModel.GameState.BOT_WON -> "Бот победил!"
        DiceGameViewModel.GameState.DRAW -> "Ничья!"
        else -> ""
    }
}

@Composable
private fun getResultColor(gameState: DiceGameViewModel.GameState): Color {
    return when (gameState) {
        DiceGameViewModel.GameState.PLAYER_WON -> Color.Green
        DiceGameViewModel.GameState.BOT_WON -> Color.Red
        else -> MaterialTheme.colorScheme.onBackground
    }
}

private fun isPlayerWinning(gameState: DiceGameViewModel.GameState): Boolean {
    return gameState == DiceGameViewModel.GameState.PLAYER_WON
}

private fun isBotWinning(gameState: DiceGameViewModel.GameState): Boolean {
    return gameState == DiceGameViewModel.GameState.BOT_WON
} 