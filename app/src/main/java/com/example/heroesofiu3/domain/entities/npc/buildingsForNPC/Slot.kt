package com.example.heroesofiu3.domain.entities.npc.buildingsForNPC

import com.example.heroesofiu3.domain.entities.GAME_MINUTE
import com.example.heroesofiu3.domain.entities.GameClock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

class Slot(val id: Int) {
	private val mutex = Mutex()
	var isBusy = false
		private set
	var busyUntil = 0L

	suspend fun tryUse(service: Service): Boolean {
		if (mutex.tryLock()) {
			isBusy = true
			busyUntil = GameClock.gameTime.value + service.durationMinutes
			CoroutineScope(Dispatchers.Default).launch {
				delay(service.durationMinutes * GAME_MINUTE)
				service.effect
				isBusy = false
				busyUntil = 0
				mutex.unlock()
			}
			return true
		} else return false
	}
}
