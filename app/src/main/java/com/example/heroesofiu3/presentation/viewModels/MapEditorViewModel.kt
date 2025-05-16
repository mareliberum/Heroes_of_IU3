package com.example.heroesofiu3.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.example.heroesofiu3.domain.entities.gameField.GameField
import com.example.heroesofiu3.domain.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MapEditorViewModel() : ViewModel(){
	private val _gameState = MutableStateFlow(GameState(10,10))
	val gameState: StateFlow<GameState> = _gameState

	private val _gameField = MutableStateFlow(GameField(10,10))
	val gameField : StateFlow<GameField> = _gameField
}