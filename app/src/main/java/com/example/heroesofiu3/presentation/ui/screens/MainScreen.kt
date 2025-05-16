package com.example.heroesofiu3.presentation.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.heroesofiu3.LocalSharedViewModel
import com.example.heroesofiu3.Screen

@Composable
fun MainScreen(navController: NavController) {
	val viewModel = LocalSharedViewModel.current
	val gameState by viewModel.gameState.collectAsState()
	val scrollState = rememberScrollState()

	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(
				brush = Brush.verticalGradient(
					colors = listOf(
						MaterialTheme.colorScheme.primaryContainer,
						MaterialTheme.colorScheme.background
					)
				)
			)
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(scrollState),
			verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Spacer(modifier = Modifier.height(32.dp))

			// Заголовок
			Text(
				text = "Главное меню",
				style = MaterialTheme.typography.headlineLarge,
				color = MaterialTheme.colorScheme.primary,
				modifier = Modifier.padding(bottom = 24.dp)
			)

			// Кнопки с иконками
			MenuButton(
				onClick = {
					gameState.resetGame()
					navController.navigate(Screen.NewGameScreen.route)
				},
				text = "Новая игра",
				icon = Icons.Default.Add,
				modifier = Modifier.fillMaxWidth(0.8f)
			)

			MenuButton(
				onClick = {

					navController.navigate(Screen.GameScreen.route)
				},
				text = "Продолжить игру",
				icon = Icons.Default.PlayArrow,
				modifier = Modifier.fillMaxWidth(0.8f)
			)

			MenuButton(
				onClick = {
					navController.navigate(Screen.SaveListScreen.route)
				},
				text = "Загрузить игру",
				icon = Icons.Default.Download,
				modifier = Modifier.fillMaxWidth(0.8f)
			)

			MenuButton(
				onClick = {
					navController.navigate(Screen.RecordsScreen.route)
				},
				text = "Таблица рекордов",
				icon = Icons.Default.Leaderboard,
				modifier = Modifier.fillMaxWidth(0.8f)
			)

			// Добавляем кнопку для игры в кости
			MenuButton(
				onClick = {
					navController.navigate(Screen.DiceGameScreen.route)
				},
				text = "Игра в кости",
				icon = Icons.Default.Casino,
				modifier = Modifier.fillMaxWidth(0.8f)
			)

			Spacer(modifier = Modifier.height(32.dp))
		}

		// Профиль в AppBar
		Row(
			modifier = Modifier
				.padding(16.dp)
				.align(Alignment.TopStart)
				.clickable { navController.navigate(Screen.PlayerScreen.route) }
				.background(
					color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
					shape = RoundedCornerShape(20.dp)
				)
				.padding(horizontal = 12.dp, vertical = 8.dp),
			verticalAlignment = Alignment.CenterVertically,
		) {
			Icon(
				modifier = Modifier.size(28.dp),
				imageVector = Icons.Default.AccountCircle,
				contentDescription = "Профиль",
				tint = MaterialTheme.colorScheme.primary
			)

			Spacer(modifier = Modifier.width(8.dp))

			Text(
				text = viewModel.name,
				style = MaterialTheme.typography.titleMedium,
				color = MaterialTheme.colorScheme.onSurface,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)
		}
	}
}

@Composable
fun MenuButton(
	onClick: () -> Unit,
	text: String,
	icon: ImageVector,
	modifier: Modifier = Modifier
) {
	Button(
		onClick = onClick,
		modifier = modifier,
		colors = ButtonDefaults.buttonColors(
			containerColor = MaterialTheme.colorScheme.surface,
			contentColor = MaterialTheme.colorScheme.primary
		),
		elevation = ButtonDefaults.buttonElevation(
			defaultElevation = 4.dp,
			pressedElevation = 2.dp
		),
		shape = RoundedCornerShape(12.dp),
		border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(8.dp)
		) {
			Icon(
				imageVector = icon,
				contentDescription = text,
				modifier = Modifier.size(20.dp)
			)
			Text(
				text = text,
				style = MaterialTheme.typography.titleMedium
			)
		}
	}
}