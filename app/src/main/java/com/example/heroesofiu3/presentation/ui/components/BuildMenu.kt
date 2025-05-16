package com.example.heroesofiu3.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.heroesofiu3.Screen
import com.example.heroesofiu3.domain.entities.buildings.Barracks
import com.example.heroesofiu3.domain.entities.buildings.Fort
import com.example.heroesofiu3.domain.entities.buildings.Stable
import com.example.heroesofiu3.domain.entities.buildings.Tavern
import com.example.heroesofiu3.domain.entities.gameField.Cell

@Composable
fun BuildMenu(selectedCell: Cell, navController: NavHostController) {
    val castle = selectedCell.castle!!

    val buildingList = listOf(
        Tavern(),
        Barracks(),
        Stable(),
        Fort()
    )

    Column {

        Text(
            text = castle.toString(),
            color = MaterialTheme.colorScheme.primary
        )


        buildingList.forEach { building ->

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onClick = { castle.build(building) }
            ) {
                Text(
                    "Build ${building.name} (Cost: ${building.cost})",
                    textAlign = TextAlign.Center
                )
            }
        }
        UsableBuildings(selectedCell)
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            onClick = { navController.navigate(Screen.DiceGameScreen.route) }
        ) {

            Icon(
                imageVector = Icons.Default.Casino,
                contentDescription = "dices",
            )

            Text(
                "Играть в кости",
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun UsableBuildings(selectedCell: Cell) {
    val playerCastle = selectedCell.castle!!

    val buildings = playerCastle.buildings

    LazyColumn {
        items(buildings) {

            Button(
                modifier = Modifier.fillMaxWidth(0.5f),
                onClick = { it.executeEffect(selectedCell) },
            ) {
                Text("Use ${it.name} (${it.costOfUse})", textAlign = TextAlign.Center)
            }
        }
    }
}