package com.example.heroesofiu3.domain.Units

class Hero(
    name: String,
    isPlayer: Boolean,
    health: Int = 100,
    strength: Int = 50,
    maxDistance : Int = 3,
) : Unit(name, health, strength, maxDistance, isPlayer) {

    override fun toString(): String {
        return this.name + " health: " + this.health + " strength: " + this.strength
    }
}