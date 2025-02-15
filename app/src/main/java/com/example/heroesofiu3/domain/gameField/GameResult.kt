package com.example.heroesofiu3.domain.gameField

sealed class GameResult {
    object PlayerWins : GameResult()
    object BotWins : GameResult()
    object Continue : GameResult()
}

