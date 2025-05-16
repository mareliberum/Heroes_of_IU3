package com.example.heroesofiu3.domain.entities.npc.buildingsForNPC

import com.example.heroesofiu3.domain.entities.npc.Npc

class Hotel : TimeDependantBuilding {
	override val name = "У погибшего альпиниста"

	override val services: List<Service> = listOf(
		Service(
			name = "Короткий отдых",
			durationMinutes = 1440,
			effect = { /* +2 к здоровью всех юнитов */ }),
		Service(
			name = "Длинный отдых",
			durationMinutes = 4320,
			effect = { /* +6 к здоровью всех юнитов */ }),
		)

	override val slots: List<Slot> = List(5) { Slot(it) }

	override suspend fun tryEnter(npc: Npc, service: Service): EnterResult {
		TODO("Not yet implemented")
	}

}