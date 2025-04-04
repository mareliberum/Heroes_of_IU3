package com.example.heroesofiu3.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.heroesofiu3.LocalGameSavesRepository
import com.example.heroesofiu3.LocalSharedViewModel
import com.example.heroesofiu3.R
import com.example.heroesofiu3.Screen
import com.example.heroesofiu3.data.DataEntities.GameSave
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Preview(showSystemUi = true)
@Composable
fun SaveListScreenPreview() {
	MaterialTheme {
		Surface(
			modifier = Modifier.fillMaxSize(),
			color = MaterialTheme.colorScheme.background
		) {
			SaveListScreen()
		}
	}
}

@Composable
fun SaveListScreen(navController: NavController? = null) {

	val viewModel = LocalSharedViewModel.current
	val repository = LocalGameSavesRepository.current

	val gameState by viewModel.gameState.collectAsState()

	var showDeleteDialog by remember { mutableStateOf(false) }
	var selectedSave by remember { mutableStateOf<GameSave?>(null) }
	val context = LocalContext.current

	var saves by remember { mutableStateOf<List<GameSave>>(emptyList()) }
	var isLoading by remember { mutableStateOf(true) }
	var error by remember { mutableStateOf<String?>(null) }
	val coroutineScope = rememberCoroutineScope()

	fun loadSaves(){
		coroutineScope.launch(Dispatchers.IO) {
			try {
				saves = repository.getByPlayerName(context, viewModel.name)
				isLoading = false
			} catch (e: Exception) {
				error = e.message
				isLoading = false
			}
		}
	}

	LaunchedEffect(Unit) {
		loadSaves()
	}

	if (isLoading) {
		Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
			CircularProgressIndicator()
		}
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
	) {
		// Заголовок с иконкой
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(bottom = 24.dp)
		) {
			IconButton(
				modifier = Modifier.size(32.dp),
				onClick = { navController?.popBackStack() }
			) {
				Icon(
					modifier = Modifier.size(32.dp),
					imageVector = Icons.AutoMirrored.Filled.ArrowBack,
					contentDescription = "Сохранения",
					tint = MaterialTheme.colorScheme.primary,
				)
			}
			Spacer(modifier = Modifier.width(16.dp))
			Text(
				text = "Мои сохранения",
				style = MaterialTheme.typography.headlineSmall,
				color = MaterialTheme.colorScheme.onSurface
			)
		}

		// Список сохранений
		LazyColumn(
			modifier = Modifier.weight(1f),
			verticalArrangement = Arrangement.spacedBy(12.dp)
		) {
			items(saves) { save ->
				SaveItemCard(
					save = save,
					onLoadClick = {
						coroutineScope.launch(Dispatchers.IO) {
							// При обновлении запускает Launched Effect на обновление экрана
							gameState.loadedGameField = repository.loadGame(context, save.id)
							gameState.setScore(repository.getScore(context, save.id) ?: 0)
							withContext(Dispatchers.Main){
								navController?.navigate(Screen.GameScreen.route)
							}
						}
					},
					onDeleteClick = {
						selectedSave = save
						showDeleteDialog = true
					}
				)
			}
		}

		// Кнопка назад
		OutlinedButton(
			onClick = { navController?.popBackStack() },
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 16.dp),
			colors = ButtonDefaults.outlinedButtonColors(
				contentColor = MaterialTheme.colorScheme.onSurface
			)
		) {
			Text("Выйти")
		}
	}

	// Диалог подтверждения удаления
	if (showDeleteDialog) {
		AlertDialog(
			onDismissRequest = { showDeleteDialog = false },
			icon = {
				Icon(
					imageVector = Icons.Default.Warning,
					contentDescription = "Предупреждение",
					tint = MaterialTheme.colorScheme.error
				)
			},
			title = {
				Text("Удалить сохранение?")
			},
			text = {
				Text("Вы точно хотите удалить \"${selectedSave?.name}\"? Это действие нельзя отменить.")
			},
			confirmButton = {
				TextButton(
					onClick = {
						showDeleteDialog = false
						coroutineScope.launch(Dispatchers.IO){
							repository.deleteById(context, selectedSave?.id ?: -1)
							loadSaves()
						}
					}
				) {
					Text("Удалить", color = MaterialTheme.colorScheme.error)
				}
			},
			dismissButton = {
				TextButton(
					onClick = { showDeleteDialog = false }
				) {
					Text("Отмена")
				}
			}
		)
	}
}

@Composable
fun SaveItemCard(
	save: GameSave,
	onLoadClick: () -> Unit,
	onDeleteClick: () -> Unit
) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surfaceVariant
		)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			// Иконка сохранения
			Icon(
				painter = painterResource(R.drawable.save_icon),
				contentDescription = "Сохранение игры",
				modifier = Modifier.size(40.dp),
				tint = MaterialTheme.colorScheme.primary
			)

			Spacer(modifier = Modifier.width(16.dp))

			// Информация о сохранении
			Column(
				modifier = Modifier.weight(1f)
			) {
				Text(
					text = save.name,
					style = MaterialTheme.typography.titleMedium,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis
				)
				Text(
					text = save.player,
					style = MaterialTheme.typography.bodySmall,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}

			// Кнопки действий
			Row {
				IconButton(
					onClick = onLoadClick,
					modifier = Modifier.size(48.dp)
				) {
					Icon(
						imageVector = Icons.Default.PlayArrow,
						contentDescription = "Загрузить",
						tint = MaterialTheme.colorScheme.primary
					)
				}
				IconButton(
					onClick = onDeleteClick,
					modifier = Modifier.size(48.dp)
				) {
					Icon(
						imageVector = Icons.Default.Delete,
						contentDescription = "Удалить",
						tint = MaterialTheme.colorScheme.error
					)
				}
			}
		}
	}
}


