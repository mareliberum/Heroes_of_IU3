package com.example.heroesofiu3.domain.entities.gameField

sealed class GameResult {
    data object PlayerWins : GameResult()
    data object BotWins : GameResult()
    data object Continue : GameResult()
}

