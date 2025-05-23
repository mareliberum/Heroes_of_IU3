package com.example.heroesofiu3.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.heroesofiu3.LocalSharedViewModel

@Composable
fun PlayerScreen(navController: NavHostController) {
	val viewModel = LocalSharedViewModel.current
	var playerName by remember { mutableStateOf(" ") }
	var isError by remember { mutableStateOf(false) }
	val focusManager = LocalFocusManager.current

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		// Поле ввода
		OutlinedTextField(
			value = playerName,
			onValueChange = {
				playerName = it
				isError = false
			},
			label = { Text("Ваше имя") },
			singleLine = true,
			isError = isError,
			keyboardOptions = KeyboardOptions(
				capitalization = KeyboardCapitalization.Words,
				imeAction = ImeAction.Done
			),
			keyboardActions = KeyboardActions(
				onDone = {
					if (playerName.isNotBlank()) {
						focusManager.clearFocus()
						viewModel.setName(playerName.trim())
					} else {
						isError = true
					}
				}
			),
			modifier = Modifier.fillMaxWidth(),
			trailingIcon = {
				if (playerName.isNotBlank()) {
					IconButton(onClick = { playerName = "" }) {
						Icon(Icons.Default.Close, "Очистить")
					}
				}
			}
		)

		// Сообщение об ошибке
		if (isError) {
			Text(
				text = "Введите имя игрока",
				color = MaterialTheme.colorScheme.error,
				style = MaterialTheme.typography.bodySmall,
				modifier = Modifier.padding(start = 16.dp, top = 4.dp)
			)
		}

		Spacer(modifier = Modifier.height(24.dp))

		// Кнопка подтверждения
		Button(
			onClick = {
				if (playerName.isNotBlank()) {
					viewModel.setName(playerName.trim())
					// сбросим текущую игру для нового игрока
					viewModel.resetGameState()
					navController.popBackStack()
				} else {
					isError = true
				}
			},
			modifier = Modifier.fillMaxWidth(),
			enabled = playerName.isNotBlank()
		) {
			Text("Сохранить")
		}
	}
}


