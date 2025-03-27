package com.example.heroesofiu3.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.heroesofiu3.LocalGameSavesRepository
import com.example.heroesofiu3.LocalSharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SaveMenu(navController: NavController) {
	val viewModel = LocalSharedViewModel.current
	val repository = LocalGameSavesRepository.current

	val gameState by viewModel.gameState.collectAsState()
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
					withContext(Dispatchers.Main){
						navController.popBackStack()
					}
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