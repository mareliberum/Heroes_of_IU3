package com.example.heroesofiu3.domain.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.heroesofiu3.domain.game.initializeField
import com.example.heroesofiu3.presentation.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(GameState(10,10))
    val gameState: StateFlow<GameState> = _gameState

	private var _name by mutableStateOf("John Doe")
	val name : String
		get () = _name

	fun setName(newName : String){
		_name = newName
	}

	fun resetGameState(){
		_gameState.value = GameState(10,10)
		initializeField(gameState.value.gameField)
	}

    init {
    	initializeField(gameState.value.gameField)
    }

}