package com.example.heroesofiu3.domain.viewModels

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.heroesofiu3.domain.ShakeDetector
import kotlin.random.Random

class DiceGameViewModel(private val context: Context) : ViewModel() {
    
    // Состояние игры
    var playerDiceValue by mutableStateOf(1)
        private set
    
    var botDiceValue by mutableStateOf(1)
        private set
    
    var gameState by mutableStateOf(GameState.BETTING)
        private set
    
    // Ставка и золото
    var playerGold by mutableIntStateOf(100) // Начальное золото игрока
        private set
    
    var currentBet by mutableIntStateOf(10) // Текущая ставка
        private set
    
    var maxBet by mutableIntStateOf(50) // Максимальная ставка
        private set
    
    private var shakeDetector: ShakeDetector? = null
    
    // Инициализация детектора тряски
    init {
        shakeDetector = ShakeDetector(context) {
            if (gameState == GameState.WAITING_FOR_PLAYER) {
                onPlayerShake()
            }
        }
    }
    
    // Функции для управления жизненным циклом детектора тряски
    fun startListening() {
        shakeDetector?.startListening()
    }
    
    fun stopListening() {
        shakeDetector?.stopListening()
    }
    
    // Обработка тряски пользователем
    private fun onPlayerShake() {
        // Вибрация при тряске
        vibrate()
        
        // Бросаем кости игрока
        rollPlayerDice()
        
        // Меняем состояние игры
        gameState = GameState.PLAYER_ROLLED
        
        // Бот бросает кости через небольшую задержку
        Handler(Looper.getMainLooper()).postDelayed({
            rollBotDice()
            determineWinner()
        }, 1500)
    }
    
    // Бросок костей игрока
    private fun rollPlayerDice() {
        playerDiceValue = Random.nextInt(1, 7)
    }
    
    // Бросок костей бота
    private fun rollBotDice() {
        botDiceValue = Random.nextInt(1, 7)
        gameState = GameState.BOT_ROLLED
    }
    
    // Определение победителя и выплата ставок
    private fun determineWinner() {
        gameState = when {
            playerDiceValue > botDiceValue -> {
                // Игрок выиграл - получает свою ставку
                playerGold += currentBet
                GameState.PLAYER_WON
            }
            playerDiceValue < botDiceValue -> {
                // Игрок проиграл - теряет свою ставку
                playerGold -= currentBet
                GameState.BOT_WON
            }
            else -> {
                // Ничья - возврат ставки
                GameState.DRAW
            }
        }
    }
    
    // Управление ставкой
    fun increaseBet() {
        // Увеличиваем ставку на 10, не превышая максимальную и доступное золото
        val newBet = (currentBet + 10).coerceAtMost(minOf(maxBet, playerGold))
        currentBet = newBet
    }
    
    fun decreaseBet() {
        // Уменьшаем ставку на 10, не меньше минимальной (10)
        val newBet = (currentBet - 10).coerceAtLeast(10)
        currentBet = newBet
    }
    
    // Подтверждение ставки и переход к игре
    fun placeBet() {
        if (currentBet <= playerGold && currentBet >= 10) {
            gameState = GameState.WAITING_FOR_PLAYER
        }
    }
    
    // Запуск новой игры
    fun resetGame() {
        playerDiceValue = 1
        botDiceValue = 1
        
        // Проверяем, есть ли у игрока достаточно золота для новой игры
        if (playerGold < 10) {
            // Если нет, даем немного золота
            playerGold = 50
        }
        
        // Ограничиваем максимальную ставку доступным золотом
        maxBet = playerGold
        
        // Устанавливаем начальную ставку
        currentBet = minOf(10, playerGold)
        
        gameState = GameState.BETTING
    }
    
    // Вибрация устройства
    private fun vibrate() {
        try{
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) {
                val vibrationEffect = VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(vibrationEffect)
            }
        }
        catch (e : Exception){
            Log.e("log", "Error in vibration")
        }

    }
    
    // Состояния игры
    enum class GameState {
        BETTING,            // Размещение ставки
        WAITING_FOR_PLAYER, // Ожидание броска игрока
        PLAYER_ROLLED,      // Игрок бросил кости
        BOT_ROLLED,         // Бот бросил кости
        PLAYER_WON,         // Игрок победил
        BOT_WON,            // Бот победил
        DRAW                // Ничья
    }
    
    // Создаем фабрику для ViewModel, так как ей нужен контекст
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Context)
                DiceGameViewModel(application)
            }
        }
    }
    
    // Освобождаем ресурсы при уничтожении ViewModel
    override fun onCleared() {
        super.onCleared()
        stopListening()
        shakeDetector = null
    }
} 