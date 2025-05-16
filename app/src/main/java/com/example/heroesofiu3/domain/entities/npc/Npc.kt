package com.example.heroesofiu3.domain.entities.npc

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Npc {

	fun startBehaviour(){
		CoroutineScope(Dispatchers.Default).launch {
			while(true){
				// choose random building
				// choose random service
				// tryEnter


				delay(10_000)
			}
		}
	}

}