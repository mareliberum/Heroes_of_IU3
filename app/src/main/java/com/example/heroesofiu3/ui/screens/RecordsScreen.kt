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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun RecordsScreen(navController: NavController) {
	// Mock данные для рекордов (замените на реальные из вашего репозитория)
	val records = remember {
		listOf(
			Record("Игрок 1", 150, "12.05.2023"),
			Record("Вы", 200, "15.05.2023"),
			Record("Игрок 2", 100, "10.05.2023"),
			Record("Игрок 3", 180, "11.05.2023"),
			Record("Игрок 4", 90, "09.05.2023"),
			Record("Игрок 5", 210, "16.05.2023"),
		)
	}

	var sortBy by remember { mutableStateOf(SortBy.SCORE) }

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
				style = MaterialTheme.typography.headlineSmall
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
				key = { it.playerName + it.score }
			) { record ->
				RecordItem(
					record = record,
					isCurrentPlayer = record.playerName == "Вы"
				)
			}
		}

		// Кнопка назад
		Button(
			onClick = { navController.popBackStack() },
			modifier = Modifier.fillMaxWidth(),
		) {
			Text("Назад")
		}
	}
}

@Composable
fun RecordItem(record: Record, isCurrentPlayer: Boolean) {
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
					text = record.playerName,
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

data class Record(
	val playerName: String,
	val score: Int,
	val date: String
)

enum class SortBy {
	SCORE, DATE
}

@Preview(showSystemUi = true)
@Composable
fun RecordsScreenPreview() {
	MaterialTheme {
		RecordsScreen(rememberNavController())
	}
}