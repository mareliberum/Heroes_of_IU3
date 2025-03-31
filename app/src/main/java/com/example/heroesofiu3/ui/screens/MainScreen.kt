package com.example.heroesofiu3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.heroesofiu3.LocalSharedViewModel
import com.example.heroesofiu3.Screen

@Composable
fun MainScreen(navController: NavController){
	val viewModel = LocalSharedViewModel.current
	val gameState by viewModel.gameState.collectAsState()

	Column(modifier = Modifier
		.fillMaxSize()
		.background(MaterialTheme.colorScheme.background),
		verticalArrangement = Arrangement.Center
	) {
		Button(onClick =
		{
			gameState.resetGame()
			navController.navigate(Screen.GameScreen.route)
		},
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp))
		{
			Text(
				"Новая игра"
			)
		}

		Button(onClick =
			{
				navController.navigate(Screen.GameScreen.route)
			},
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp))
		{
			Text(
				"Продолжить игру"
			)
		}
		Button(
			onClick =
			{
				navController.navigate(Screen.SaveListScreen.route)
			},
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp),
		)
		{
			Text("Загрузить")
		}

		Button(
			onClick =
			{
				navController.navigate(Screen.RecordsScreen.route)
			},
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp),
		)
		{
			Text("Рекорды")
		}

	}
}