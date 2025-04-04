package com.example.heroesofiu3.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.example.heroesofiu3.Screen
import com.example.heroesofiu3.data.getCurrentDateTime
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
					repository.saveGame(context, gameField, getCurrentDateTime(), gameState.score)
				}
			},
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp)
		) {
			Text("Быстрое сохранение")
		}

		Button(
			onClick = {
				coroutineScope.launch(Dispatchers.IO) {
					// При обновлении запускает Launched Effect на обновление экрана
					gameState.loadedGameField = repository.loadGame(context, 1)
					gameState.setScore(repository.getScore(context, 1) ?: 0)
					withContext(Dispatchers.Main){
						navController.popBackStack()
					}
				}

			},
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp)
		) {
			Text("Быстрая загрузка")
		}

		Button(
			modifier = Modifier
				.fillMaxWidth()
				.wrapContentHeight()
				.padding(16.dp),
			onClick = {
				navController.navigate(Screen.SaveListScreen.route)
			}
		) {
			Text(
				"Загрузить"
			)
		}

		Spacer(modifier = Modifier.weight(1f))

		Button(
			modifier = Modifier
				.fillMaxWidth()
				.wrapContentHeight()
				.padding(16.dp),
			onClick = {
				navController.navigate(Screen.GameScreen.route)
			}
		)
		{
			Text("Назад")
		}
	}
}