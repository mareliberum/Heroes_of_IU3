package com.example.heroesofiu3.domain.entities.buildings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue

class Castle(
    val name: String,
    val isPlayer: Boolean,
    var isUnderSiege: Boolean = false,
    var health : Int = 250,
    var strength: Int = 40,
) {
    private val _buildings = mutableStateListOf<Building>()
    val buildings : List<Building> get() = _buildings

    private var _gold by mutableIntStateOf(0)
    val gold : Int get() = _gold




    fun build(building: Building){
        if (_gold >= building.cost && !(_buildings.any { it::class == building :: class })){
            spendMoney(building.cost)
            _buildings.add(building)
        }
        else{
            println("Not enough gold to build ${building.name} or building exist!")
        }
    }

    fun addBuiding(building: Building){
        if (!(_buildings.any { it::class == building :: class })){
            _buildings.add(building)
        }
    }

    fun spendMoney(cost : Int){
        _gold -= cost
    }
    fun addGold(amount : Int){
        _gold += amount
    }

    override fun toString(): String {
        return "[Castle $name], gold: $gold, health: $health"
    }
}
