package com.example.heroesofiu3.domain.entities.Units

abstract class Unit(
    val name: String,
    var health: Int,
    val strength: Int,
    var maxDistance: Int,
    val isPlayer : Boolean,
    val attackDistance: Int = 1,
    var isBoosted: Boolean = false,
    var hasMoved: Boolean = false,
    var hasAttacked: Boolean = false,
)