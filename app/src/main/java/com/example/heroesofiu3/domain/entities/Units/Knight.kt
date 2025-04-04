package com.example.heroesofiu3.domain.entities.Units

//@Serializable
class Knight(
    name: String,
    isPlayer: Boolean,
    health: Int = 100,
    strength: Int = 40,
    private val armor: Int = 40,
    maxDistance : Int = 4,

    ) :
    Unit(name, health, strength, maxDistance, isPlayer)
{
    override fun toString(): String {
        return this.name +" health: " +this.health + " strength: " + this.strength + " armor:" + this.armor
    }

}
