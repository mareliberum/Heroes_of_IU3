package com.example.heroesofiu3.domain.entities.npc.buildingsForNPC

import com.example.heroesofiu3.domain.entities.npc.Npc

interface TimeDependantBuilding {
	val name : String

	val services : List<Service>

	val slots : List<Slot>

	suspend fun tryEnter(npc : Npc, service: Service) : EnterResult

}


enum class EnterResult{
	SUCCESS,
	FAIL,
	IN_QUEUE,
}
