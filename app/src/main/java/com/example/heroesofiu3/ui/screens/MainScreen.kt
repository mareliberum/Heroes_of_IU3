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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.heroesofiu3.LocalGameSavesRepository
import com.example.heroesofiu3.Screen

@Composable
fun MainScreen(navController: NavController){
	val repository = LocalGameSavesRepository.current
	Column(modifier = Modifier
		.fillMaxSize()
		.background(MaterialTheme.colorScheme.background),
		verticalArrangement = Arrangement.Center
	) {
		Button(onClick =
			{navController.navigate(Screen.GameScreen.route)},
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp))

		{
			Text(
				"New Game"
			)
		}
		Button(onClick =
		{},
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp))

		{
			Text(
				"Load Game"
			)
		}

	}
}