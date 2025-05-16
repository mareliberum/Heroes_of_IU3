package com.example.heroesofiu3.domain.entities

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val GAME_MINUTE = 100L

object GameClock {
	private val _gameTime = MutableStateFlow(0L)
	val gameTime: StateFlow<Long> = _gameTime.asStateFlow()
	private var timerJob: Job? = null
	fun start() {
		timerJob = CoroutineScope(Dispatchers.Default).launch {
			while (true) {
				delay(GAME_MINUTE)
				_gameTime.update { it + 1 }
			}
		}
	}

	fun reset() {
		timerJob?.cancel()
		_gameTime.value = 0
	}
}