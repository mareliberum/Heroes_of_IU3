package com.example.heroesofiu3.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.heroesofiu3.data.GameSavesDbRepository
import com.example.heroesofiu3.presentation.GameState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SavesScreen(gameState: GameState, repository: GameSavesDbRepository) {
	val gameField = gameState.gameField

	val context = LocalContext.current
	val coroutineScope = rememberCoroutineScope()
	

	Column(
		modifier = Modifier
			.wrapContentHeight()
			.fillMaxWidth()
			.padding(top = 32.dp, start = 14.dp, end = 14.dp, bottom = 8.dp)
	) {

		Button(
			onClick = {
				coroutineScope.launch(Dispatchers.IO) {
					repository.saveGame(context, gameField, "save one")
				}
			},
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp)
		) {
			Text("SaveGame")
		}

		Button(
			onClick = {
				coroutineScope.launch(Dispatchers.IO) {
					// При обновлении запускает Launched Effect на обновление экрана
					gameState.loadedGameField = repository.loadGame(context, 1)
					println("game loaded")
				}
			},
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp)
		) {
			Text("Load last game")
		}

		Button(
			onClick = {
				coroutineScope.launch(Dispatchers.IO) {
					repository.deleteAll(context)
				}
			},
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp)
		) {
			Text("drop db")
		}
	}
}