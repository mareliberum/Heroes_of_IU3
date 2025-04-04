package com.example.heroesofiu3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.heroesofiu3.LocalSharedViewModel
import com.example.heroesofiu3.Screen

@Composable
fun MainScreen(navController: NavController) {
	val viewModel = LocalSharedViewModel.current
	val gameState by viewModel.gameState.collectAsState()

	Box(modifier = Modifier.fillMaxSize()) {
		// Основное содержимое экрана
		Column(
			modifier = Modifier
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.background),
			verticalArrangement = Arrangement.Center
		) {
			// Ваши существующие кнопки
			Button(
				onClick = {
					gameState.resetGame()
					navController.navigate(Screen.GameScreen.route)
				},
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp)
			) {
				Text("Новая игра")
			}

			Button(
				onClick = {
					navController.navigate(Screen.GameScreen.route)
				},
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp)
			) {
				Text("Продолжить игру")
			}

			Button(
				onClick = {
					navController.navigate(Screen.SaveListScreen.route)
				},
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp),
			) {
				Text("Загрузить")
			}

			Button(
				onClick = {
					navController.navigate(Screen.RecordsScreen.route)
				},
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp),
			) {
				Text("Рекорды")
			}
		}

		// Контейнер для иконки и имени
		Row(
			modifier = Modifier
				.padding(16.dp)
				.align(Alignment.TopStart)
				.clickable { navController.navigate(Screen.PlayerScreen.route) },
			verticalAlignment = Alignment.CenterVertically,
		) {
			// Иконка профиля

			Icon(
				modifier = Modifier.size(36.dp),
				imageVector = Icons.Default.AccountCircle,
				contentDescription = "Профиль",
				tint = MaterialTheme.colorScheme.primary
			)


			// Имя игрока с отступом
			Spacer(modifier = Modifier.width(8.dp)) // Отступ 8dp между иконкой и именем

			Text(
				text = viewModel.name,
				style = MaterialTheme.typography.bodyLarge,
				color = MaterialTheme.colorScheme.onBackground,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)
		}
	}
}