package com.example.heroesofiu3.domain.entities.npc.buildingsForNPC

data class Service(
	val name: String,
	val durationMinutes : Long,
	val effect: () -> Unit,

)
