package com.example.heroesofiu3.domain.viewModels

import androidx.lifecycle.ViewModel
import com.example.heroesofiu3.domain.game.initializeField
import com.example.heroesofiu3.presentation.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(GameState(10,10))
    val gameState: StateFlow<GameState> = _gameState

    init {
    	initializeField(gameState.value.gameField)
    }

}