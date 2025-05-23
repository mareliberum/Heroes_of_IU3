package com.example.heroesofiu3.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.heroesofiu3.LocalRecordsSavesRepository
import com.example.heroesofiu3.data.DataEntities.RecordSave
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RecordsScreen(navController: NavController) {
	val repository = LocalRecordsSavesRepository.current

	var records by remember { mutableStateOf<List<RecordSave>>(emptyList()) }

	var sortBy by remember { mutableStateOf(SortBy.SCORE) }
	val coroutineScope = rememberCoroutineScope()
	var isLoading by remember { mutableStateOf(true) }
	val context = LocalContext.current

	fun loadRecords(){
		coroutineScope.launch(Dispatchers.IO) {
			try {
				records = repository.getAll(context)
				isLoading = false
			} catch (e: Exception) {
				isLoading = false
			}
		}
	}

	LaunchedEffect(Unit) {
		loadRecords()
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
	) {
		// Заголовок
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(bottom = 16.dp)
		) {
			Icon(
				imageVector = Icons.Default.AccountBox,
				contentDescription = "Рекорды",
				modifier = Modifier.size(32.dp),
				tint = MaterialTheme.colorScheme.primary
			)
			Spacer(modifier = Modifier.width(8.dp))
			Text(
				text = "Таблица рекордов",
				style = MaterialTheme.typography.headlineSmall,
				color = MaterialTheme.colorScheme.primary
			)
		}

		// Переключатель сортировки
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(bottom = 8.dp),
			horizontalArrangement = Arrangement.SpaceEvenly
		) {
			SortChip(
				text = "По очкам",
				selected = sortBy == SortBy.SCORE,
				onClick = { sortBy = SortBy.SCORE }
			)
			SortChip(
				text = "По дате",
				selected = sortBy == SortBy.DATE,
				onClick = { sortBy = SortBy.DATE }
			)
		}

		// Список рекордов
		LazyColumn(
			modifier = Modifier.weight(1f)
		) {
			items(
				items = when (sortBy) {
					SortBy.SCORE -> records.sortedByDescending { it.score }
					SortBy.DATE -> records.sortedByDescending { it.date }
				},
			) { record ->
				RecordItem(
					record = record,
					isCurrentPlayer = record.name == "Вы"
				)
			}
		}
		// кнопка назад
		OutlinedButton(
			onClick = { navController.popBackStack() },
			modifier = Modifier.fillMaxWidth(),
		) {
			Text("Назад")
		}
	}
}

@Composable
fun RecordItem(record: RecordSave, isCurrentPlayer: Boolean) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 4.dp),
		colors = CardDefaults.cardColors(
			containerColor = if (isCurrentPlayer) MaterialTheme.colorScheme.primaryContainer
			else MaterialTheme.colorScheme.surfaceVariant
		)
	) {
		Row(
			modifier = Modifier.padding(16.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Column(modifier = Modifier.weight(1f)) {
				Text(
					text = record.name,
					style = MaterialTheme.typography.titleMedium,
					color = if (isCurrentPlayer) MaterialTheme.colorScheme.primary
					else MaterialTheme.colorScheme.onSurface
				)
				Text(
					text = record.date,
					style = MaterialTheme.typography.bodySmall,
					color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
				)
			}
			Text(
				text = record.score.toString(),
				style = MaterialTheme.typography.titleLarge,
				color = if (isCurrentPlayer) MaterialTheme.colorScheme.primary
				else MaterialTheme.colorScheme.onSurface
			)
		}
	}
}

@Composable
fun SortChip(text: String, selected: Boolean, onClick: () -> Unit) {
	FilterChip(
		selected = selected,
		onClick = onClick,
		label = { Text(text) },
		leadingIcon = if (selected) {
			{ Icon(Icons.Default.Check, null) }
		} else null,
		colors = FilterChipDefaults.filterChipColors(
			selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
		),
		modifier = Modifier.padding(end = 8.dp)
	)
}


enum class SortBy {
	SCORE, DATE
}
