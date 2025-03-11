package com.example.heroesofiu3.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.heroesofiu3.R
import com.example.heroesofiu3.domain.entities.Units.Archer
import com.example.heroesofiu3.domain.entities.Units.Hero
import com.example.heroesofiu3.domain.entities.Units.Knight
import com.example.heroesofiu3.domain.entities.gameField.Cell
import com.example.heroesofiu3.domain.entities.gameField.Terrain

@Composable
fun CellView(
    cell: Cell,
    isSelected: Boolean,
    onClick: () -> Unit,
    isAvailable: Boolean
) {
    val terrain = cell.terrain
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .border(
                1.dp,
                if (isSelected) Color.Red else if (isAvailable) Color.Green else Color.Black
            )
            .clickable(onClick = onClick)
            .cellBackground(terrain),
        contentAlignment = Alignment.Center
    ) {
        // Отображение содержимого ячейки
        if (cell.unit != null) {
            if (cell.unit is Knight) {
                Icon(
                    painterResource(R.drawable.knight),
                    "friendly castle",
                    tint = if (cell.unit!!.isPlayer) colorResource(id = R.color.teal_700) else colorResource(
                        id = R.color.maroon
                    )
                )
            }
            if (cell.unit is Hero) {
                Icon(
                    painterResource(R.drawable.hero),
                    "friendly castle",
                    tint = if (cell.unit!!.isPlayer) colorResource(id = R.color.teal_700) else colorResource(
                        id = R.color.maroon
                    )
                )
            }
            if (cell.unit is Archer) {
                Icon(
                    painterResource(R.drawable.archer),
                    "friendly castle",
                    tint = if (cell.unit!!.isPlayer) colorResource(id = R.color.teal_700) else colorResource(
                        id = R.color.maroon
                    )
                )
            }


        }

        if (cell.castle != null) {
            when (cell.castle!!.isPlayer) {
                true -> Icon(
                    painterResource(R.drawable.friendly_castle_24),
                    "friendly castle",
                    tint = colorResource(R.color.forestGreen)
                )

                false -> Icon(
                    painterResource(R.drawable.enemy_castle_24),
                    "castle",
                    tint = colorResource(R.color.maroon)
                )
            }
        }


    }
}

@Composable
fun Modifier.cellBackground(terrain: Terrain): Modifier {
    return this.background(
        when (terrain) {
            Terrain.FRIENDLY -> colorResource(R.color.darkSeeGreen)
            Terrain.ENEMY -> colorResource(R.color.tomato)
            Terrain.ROAD -> colorResource(R.color.khaki)
            Terrain.WALL -> colorResource(R.color.gray)
            Terrain.GATE -> colorResource(R.color.burlyWood)
            else -> colorResource(R.color.gray)
        }
    )
}