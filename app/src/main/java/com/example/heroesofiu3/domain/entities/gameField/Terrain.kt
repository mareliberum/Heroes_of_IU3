package com.example.heroesofiu3.domain.entities.gameField

import kotlinx.serialization.Serializable

@Serializable
enum class Terrain {
    FRIENDLY, ENEMY, ROAD, OBSTACLE, WALL, GATE, UNREACHABLE
}