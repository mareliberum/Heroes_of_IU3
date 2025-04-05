package com.example.heroesofiu3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.heroesofiu3.LocalSharedViewModel
import com.example.heroesofiu3.Screen
import com.example.heroesofiu3.domain.entities.buildings.Castle
import com.example.heroesofiu3.domain.entities.gameField.Terrain
import com.example.heroesofiu3.domain.game.initializeField
import com.example.heroesofiu3.domain.viewModels.MapEditorViewModel
import com.example.heroesofiu3.ui.components.CellView

@Composable
fun MapEditorScreen(navController: NavHostController) {
	val sharedViewModel = LocalSharedViewModel.current
	val viewModel: MapEditorViewModel = viewModel()
	val gameState by viewModel.gameState.collectAsState()
	// TODO  потом поменять на просто gameField из view model
	val gameField = gameState.gameField
	// Инициализация поля только при первом запуске
	LaunchedEffect(Unit) {
		gameField.resetCellTypes()
	}
	var selectedTerrain by remember { mutableStateOf<Terrain?>(null) }
	var selectedCastleType by remember { mutableStateOf<Boolean?>(null) } // null - не выбран, true - игрок, false - враг, Unit - удалить

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
		verticalArrangement = Arrangement.Top
	) {
		// Заголовок "Редактор карты"
		Text(
			text = "Редактор карты",
			style = MaterialTheme.typography.titleLarge,
			modifier = Modifier.padding(bottom = 8.dp)
		)
		
		// Редактируемое игровое поле (адаптированное из GameScreen)
		LazyVerticalGrid(
			columns = GridCells.Fixed(gameField.width),
		) {
			items(gameField.width * gameField.height) { index ->
				val x = index % gameField.width
				val y = index / gameField.width
				val cell = gameField.getCell(x, y) ?: return@items

				Box(
					modifier = Modifier
						.aspectRatio(1f)
						.border(1.dp, Color.LightGray)
						.background(Color.Red.copy(alpha = 0.3f))
				) {
					CellView(
						cell = cell,
						isSelected = false,
						onClick = {
							println("DEBUG: Cell clicked at x=$x, y=$y")
							if (selectedTerrain != null) {
								cell.terrain = selectedTerrain!!
								println("it's terrain set to $selectedTerrain")
							}

							if (selectedCastleType == null) {
								cell.castle = null
							} else
								cell.castle = Castle(
									name = if (selectedCastleType == true) "Player Castle" else "Enemy Castle",
									isPlayer = selectedCastleType == true
								)
						},
						isAvailable = false
					)
				}
			}
		}
		// Панель инструментов редактора
		EditorToolbar(
			selectedTerrain = selectedTerrain,
			onTerrainSelected = { terrain ->
				selectedTerrain = terrain
				selectedCastleType = null // Сбрасываем выбор замка при выборе местности
			},
			selectedCastleType = selectedCastleType,
			onCastleTypeSelected = { isPlayer ->
				selectedCastleType = isPlayer
				selectedTerrain = null // Сбрасываем выбор местности при выборе замка
			},
			onSave = {
				initializeField(gameField)
				sharedViewModel.gameState.value.updateGameField(gameField)

				navController.navigate(Screen.GameScreen.route)
			},
			onReset = {
				gameField.standartMap()
			}
		)
	}
}

@Composable
fun EditorToolbar(
	selectedTerrain: Terrain?,
	onTerrainSelected: (Terrain) -> Unit,
	selectedCastleType: Boolean?,
	onCastleTypeSelected: (Boolean?) -> Unit,
	onSave: () -> Unit,
	onReset: () -> Unit,
) {
	Column(
		modifier = Modifier.padding(top = 8.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		// Выбор типа местности
		Column(
			verticalArrangement = Arrangement.spacedBy(4.dp)
		) {
			Text(
				"Тип местности:",
				style = MaterialTheme.typography.labelLarge,
				color = MaterialTheme.colorScheme.primary,
			)
			Row(
				horizontalArrangement = Arrangement.spacedBy(6.dp),
				modifier = Modifier.horizontalScroll(rememberScrollState())
			)  {
				listOf(Terrain.FRIENDLY, Terrain.ENEMY, Terrain.ROAD, Terrain.OBSTACLE).forEach { terrain ->
					FilterChip(
						selected = selectedTerrain == terrain,
						onClick = { onTerrainSelected(terrain) },
						label = { Text(terrain.name) }
					)
				}
			}
		}
		// Выбор типа замка
		Column(
			verticalArrangement = Arrangement.spacedBy(4.dp)
		) {
			Text(
				"Тип замка:",
				style = MaterialTheme.typography.labelLarge,
				color = MaterialTheme.colorScheme.primary,
			)
			Row(
				horizontalArrangement = Arrangement.spacedBy(6.dp),
				modifier = Modifier.horizontalScroll(rememberScrollState())
			) {
				FilterChip(
					selected = selectedCastleType == true,
					onClick = { onCastleTypeSelected(true) },
					label = { Text("Замок игрока") }
				)
				FilterChip(
					selected = selectedCastleType == false,
					onClick = { onCastleTypeSelected(false) },
					label = { Text("Замок врага") }
				)
				FilterChip(
					selected = selectedCastleType == null,
					onClick = { onCastleTypeSelected(null) },
					label = { Text("нет замка") }
				)
			}
		}
		Button(
			onClick = onReset,
			modifier = Modifier.fillMaxWidth()
		) {
			Text("Стандартная карта")
		}


		Spacer(Modifier.weight(1f))

		Button(
			onClick = onSave,
			modifier = Modifier.fillMaxWidth()
		) {
			Text("Сохранить карту")
		}

	}
}


@Preview(showSystemUi = true)
@Composable
fun Preview() {
	MapEditorScreen(navController = NavHostController(LocalContext.current))
}