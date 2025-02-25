package com.example.heroesofiu3

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.heroesofiu3.domain.GameState
import com.example.heroesofiu3.domain.entities.Units.Archer
import com.example.heroesofiu3.domain.entities.Units.Hero
import com.example.heroesofiu3.domain.entities.Units.Knight
import com.example.heroesofiu3.domain.entities.buildings.Barracks
import com.example.heroesofiu3.domain.entities.buildings.Castle
import com.example.heroesofiu3.domain.entities.buildings.Stable
import com.example.heroesofiu3.domain.entities.buildings.Tavern
import com.example.heroesofiu3.domain.entities.gameField.Cell
import com.example.heroesofiu3.domain.entities.gameField.GameField
import com.example.heroesofiu3.domain.entities.gameField.Terrain
import com.example.heroesofiu3.presentation.screens.GameOverScreen
import com.example.heroesofiu3.ui.theme.HeroesOfIU3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HeroesOfIU3Theme {
                MainScreen()
            }
        }
    }
}

private fun initializeField(field: GameField){
    // Игрок:
    field.getCell(1, 0)?.unit = Hero("Hero", true)
    field.getCell(1, 1)?.unit = Knight("Knight", true)
    field.getCell(4, 4)?.unit = Archer("Archer", true)
    field.getCell(3, 1)?.unit = Knight("Mega Knight", true, health = 1000, strength = 1000)
    field.getCell(1, 1)?.castle = Castle("Blue", true)

    // Бот:
    field.getCell(9, 9)?.unit = Hero("Hero", false)
    field.getCell(8, 9)?.unit = Knight("Knight", false)
    field.getCell(8, 8)?.castle = Castle("Red", false)
    field.getCell(8, 8)?.castle!!.addBuiding(Barracks())
}

@Preview(showSystemUi = true)
@Composable
fun MainScreen() {
    Surface {
        GameScreen()
    }
}

@Composable
fun GameScreen() {
    val context = LocalContext.current
    val gameState = remember { GameState(10, 10) }
    val gameField = gameState.gameField
    val selectedCell = gameState.selectedCell
    val availableMoves by gameState.availableMoves.collectAsState()
    val isGameOver = gameState.isGameOver

    // Инициализация поля
    LaunchedEffect(Unit) {
        initializeField(gameField)
    }

    // Отображение игрового поля
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, start = 14.dp, end = 14.dp, bottom = 8.dp),
    ) {
         LazyVerticalGrid(
            modifier = Modifier
                .wrapContentSize()
                .aspectRatio(1f),
            columns = GridCells.Fixed(gameField.width),
             horizontalArrangement = Arrangement.Center
        ){
            items(gameField.width * gameField.height) { index ->
                val x = index % gameField.width
                val y = index / gameField.width
                val currentCell = gameField.getCell(x, y)
                if (currentCell != null) {
                    CellView(
                        cell = currentCell,
                        isSelected = currentCell == selectedCell,
                        onClick = {
                            gameState.selectCell(currentCell, context)
                        },
                        isAvailable = availableMoves.contains(currentCell)
                    )
                }
            }
        }
        //  Вывод информации о клетке
        if (selectedCell != null) {
            CellInfo(selectedCell)
        }

        //  Вызов меню покупки зданий
        val selectedCastle = selectedCell?.castle
        if (selectedCastle != null && selectedCastle.isPlayer){
            BuildMenu(selectedCell)
        }

        // Кнопка "Завершить ход"
        Button(
            onClick = {
                gameState.makeBotMove(context) // Вызов хода бота
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text("Завершить ход")
        }
    }
    if(isGameOver != ""){
        GameOverScreen(isGameOver)
        println("game over = $isGameOver")
    }
}

@Composable
fun CellInfo(selectedCell : Cell){
    Column (modifier = Modifier.wrapContentSize()){

        if (selectedCell.unit != null){
            Text(selectedCell.unit.toString())
        }
    }
}

@SuppressLint("ResourceAsColor")
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
            if (cell.unit is Knight){
                Icon(
                    painterResource(R.drawable.knight),
                    "friendly castle",
                    tint = if (cell.unit!!.isPlayer) colorResource(id = R.color.teal_700) else colorResource(id = R.color.maroon)
                )
            }
            if (cell.unit is Hero){
                Icon(
                    painterResource(R.drawable.hero),
                    "friendly castle",
                    tint = if (cell.unit!!.isPlayer) colorResource(id = R.color.teal_700) else colorResource(id = R.color.maroon)
                )
            }
            if (cell.unit is Archer){
                Icon(
                    painterResource(R.drawable.archer),
                    "friendly castle",
                    tint = if (cell.unit!!.isPlayer) colorResource(id = R.color.teal_700) else colorResource(id = R.color.maroon)
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
fun BuildMenu(selectedCell: Cell) {
    val castle = selectedCell.castle!!

    val buildingList = listOf(
        Tavern(),
        Barracks(),
        Stable()
    )
    val gold = castle.gold
    Column {

        Text(castle.toString())
        Text("Gold: [$gold]", textAlign = TextAlign.Center)

        buildingList.forEach { building ->
            Button(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                onClick = { castle.build(building)}
            ) {
                Text("Build ${building.name} (Cost: ${building.cost})", textAlign = TextAlign.Center)
            }
        }
        UsableBuildings(selectedCell)

    }
}

@Composable
fun UsableBuildings(selectedCell: Cell) {
    val playerCastle = selectedCell.castle!!

    val buildings =  playerCastle.buildings

    LazyColumn{
        items(buildings){

            Button(
                modifier = Modifier.fillMaxWidth(0.5f),
                onClick = { it.executeEffect(selectedCell) },
            ) {
                Text("Use ${it.name} (${it.cost})", textAlign = TextAlign.Center)
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
            else -> colorResource(R.color.gray)
        }
    )
}


