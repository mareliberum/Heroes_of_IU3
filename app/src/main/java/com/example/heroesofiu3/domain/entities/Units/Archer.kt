package com.example.heroesofiu3.domain.entities.Units

class Archer(
    name: String,
    isPlayer: Boolean,
    health: Int = 100,
    strength: Int = 40,
    maxDistance: Int = 3,
    attackDistance: Int = 4,

) : Unit(name, health, strength, maxDistance, isPlayer, attackDistance) {



    override fun toString(): String {
        return this.name + " health: " + this.health + " strength: " + this.strength
    }

}